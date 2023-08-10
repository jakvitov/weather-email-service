package cz.jakvitov.wes.scheduler;

import cz.jakvitov.wes.client.WeatherApiClientService;
import cz.jakvitov.wes.dto.external.weather.OpenMeteoWeatherForecastResponseDto;
import cz.jakvitov.wes.dto.internal.EmailDto;
import cz.jakvitov.wes.email.EmailService;
import cz.jakvitov.wes.persistence.entity.UserEntity;
import cz.jakvitov.wes.persistence.service.UserService;
import cz.jakvitov.wes.utils.email.EmailFormatter;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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
    @Scheduled(cron = "0 0 18 * * ?")
    public void sendEmailsToActiveUsers() throws TemplateException, IOException, MessagingException, InterruptedException {
        List<UserEntity> activeUsers = userService.getActiveUsers();
        for (UserEntity user : activeUsers){
            //Due to redis caching, we can just ask for weather for each user instead of grouping the users by city
            OpenMeteoWeatherForecastResponseDto weatherForecastResponseDto = weatherApiClientService.getHourlyWeatherForecast(user.getCity().getCityId().getLatitude(), user.getCity().getCityId().getLongitude(), 2);
            EmailDto emailDto = this.emailFormatter.fillEmailDtoWithWeather(weatherForecastResponseDto, user.getCity().getName());
            emailDto.setFrom(emailBotName);
            emailDto.setDest(user.getEmail());
            this.emailService.sendEmailWithRetryCount(emailDto, retryCount, retryDelay);
        }
    }

}
