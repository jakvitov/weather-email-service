package cz.jakvitov.wes.scheduler;

import cz.jakvitov.wes.client.WeatherApiClientService;
import cz.jakvitov.wes.dto.external.weather.OpenMeteoWeatherForecastResponseDto;
import cz.jakvitov.wes.dto.internal.EmailDto;
import cz.jakvitov.wes.email.EmailService;
import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.UserEntity;
import cz.jakvitov.wes.persistence.service.UserService;
import cz.jakvitov.wes.utils.email.EmailFormatter;
import cz.jakvitov.wes.utils.user.UserUtils;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ScheduledEmailServiceImpl implements ScheduledEmailService{

    private final UserService userService;

    private final EmailFormatter emailFormatter;

    private final WeatherApiClientService weatherApiClientService;

    private final EmailService emailService;

    @Value("${email.client.username}")
    private String emailBotName;

    @Value("${email.client.retry.count}")
    private Integer retryCount;

    @Value("${email.client.retry.delay}")
    private Integer retryDelay;

    @Autowired
    public ScheduledEmailServiceImpl(UserService userService, EmailFormatter emailFormatter, WeatherApiClientService weatherApiClientService, EmailService emailService) {
        this.userService = userService;
        this.emailFormatter = emailFormatter;
        this.weatherApiClientService = weatherApiClientService;
        this.emailService = emailService;
    }

    @Override
    public void sendEmailsToActiveUsers() throws TemplateException, IOException, MessagingException, InterruptedException {
        List<UserEntity> activeUsers = userService.getActiveUsers();
        Map<CityEntity, Set<UserEntity>> usersByCity = UserUtils.sortUsersByCity(activeUsers);
        for (CityEntity city : usersByCity.keySet()){
            EmailDto emailDto = new EmailDto();
            OpenMeteoWeatherForecastResponseDto cityForecast = weatherApiClientService.getHourlyWeatherForecast(city.getCityId().getLatitude(), city.getCityId().getLongitude(), 2);
            emailFormatter.fillEmailDtoWithWeather(cityForecast, emailDto, city.getName());
            for (UserEntity user : usersByCity.get(city)){
                emailDto.setDest(user.getEmail());
                emailDto.setFrom(emailBotName);
                emailService.sendEmailWithRetryCount(emailDto, retryCount, retryDelay);
            }
        }
    }

}
