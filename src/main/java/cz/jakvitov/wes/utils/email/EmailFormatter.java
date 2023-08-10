package cz.jakvitov.wes.utils.email;

import cz.jakvitov.wes.config.FreeMarkerConfig;
import cz.jakvitov.wes.dto.external.weather.HourlyInformationDto;
import cz.jakvitov.wes.dto.external.weather.OpenMeteoWeatherForecastResponseDto;
import cz.jakvitov.wes.dto.internal.DayInfoForEmailDto;
import cz.jakvitov.wes.dto.internal.EmailDto;
import cz.jakvitov.wes.dto.types.WeatherCode;
import cz.jakvitov.wes.persistence.entity.UserEntity;
import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static cz.jakvitov.wes.dto.types.WeatherCode.*;

/**
 * Class that handles the filling of the email template
 */

@Component
public class EmailFormatter {

    //Codes, that we want to inform about primary
    private static List<WeatherCode> IMPORTANT_CODES = Arrays.asList(FOG, DESPOSING_FOG, MODERATE_DRIZZLE, DENSE_DRIZZLE, LIGHT_FREEZING_DRIZZLE, HEAVY_FREEZING_DRIZZLE, LIGHT_RAIN, MEDIUM_RAIN, HEAVY_RAIN, LIGHT_FREEZING_RAIN, HEAVY_FREEZING_RAIN, SNOW_GRAINS, LIGHT_RAIN_SHOWER, MODERATE_RAIN_SHOWER, HEAVY_RAIN_SHOWER, THUNDERSTORM, THUNDERSTORM_LIGHT_HAIL, THUNDERSTORM_HEAVY_HAIL);

    private final Integer MORNING_START = 7;
    private final Integer MORNING_END = 12;
    private final Integer NOON_END = 18;
    private final Integer AFTERNOON_END = 23;

    @Value("${url.redirect}")
    private String baseURL;

    private final FreeMarkerConfig freeMarkerConfig;

    private final ServerProperties serverProperties;

    @Autowired
    public EmailFormatter(FreeMarkerConfig freeMarkerConfig, ServerProperties serverProperties) {
        this.freeMarkerConfig = freeMarkerConfig;
        this.serverProperties = serverProperties;
    }

    //Proccess a FreeMarker template a produce a utf-8 string from it
    private String processTemplate(Template template,  Map root) throws TemplateException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);
        Environment environment = template.createProcessingEnvironment(root, writer);
        environment.setOutputEncoding("UTF-8");
        environment.process();
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    private DayInfoForEmailDto getInfoForDayFromWeatherApiResponse(OpenMeteoWeatherForecastResponseDto openMeteoWeatherForecastResponseDto){
        HourlyInformationDto hourlyInformationDtos = openMeteoWeatherForecastResponseDto.getHourly();
        int tomorrowStartIndex = 24;
        int tomorrowEndIndex = 48;
        DayInfoForEmailDto dayInfoForEmailDto = new DayInfoForEmailDto();
        Map<String, ArrayList<LocalDateTime>> importantWeatherCodeHours = new HashMap<>();
        for (int i = tomorrowStartIndex; i < tomorrowEndIndex; i++){
            int k = i - 24;
            WeatherCode weatherCode = numberToWeatherCode(openMeteoWeatherForecastResponseDto.getHourly().getWeatherCode().get(i));
            if (IMPORTANT_CODES.contains(weatherCode)){
                if (importantWeatherCodeHours.containsKey(weatherCode.getTranslation())){
                    importantWeatherCodeHours.get(weatherCode.getTranslation()).add(hourlyInformationDtos.getTime().get(i));
                }
                else {
                    importantWeatherCodeHours.put(weatherCode.getTranslation(), new ArrayList<>(Collections.singletonList(hourlyInformationDtos.getTime().get(i))));
                }
            }
            //Currently iterating over morning temperature
            if (k > MORNING_START && k <= MORNING_END){
                dayInfoForEmailDto.setAverageMoningTemperature(dayInfoForEmailDto.getAverageMoningTemperature() + hourlyInformationDtos.getTemperature2m().get(i));
            }
            else if (k > MORNING_END && k <= NOON_END){
                dayInfoForEmailDto.setAverageNoonTemperature(dayInfoForEmailDto.getAverageNoonTemperature() + hourlyInformationDtos.getTemperature2m().get(i));
            }
            else if (k > NOON_END && k <= AFTERNOON_END){
                dayInfoForEmailDto.setAverageAfternoonTemperature(dayInfoForEmailDto.getAverageAfternoonTemperature() + hourlyInformationDtos.getTemperature2m().get(i));
            }
            //Temperature > < max/min of the day
            if (hourlyInformationDtos.getTemperature2m().get(i) < dayInfoForEmailDto.getMinTemperature()){
                dayInfoForEmailDto.setMinTemperature(hourlyInformationDtos.getTemperature2m().get(i));
            }
            if (hourlyInformationDtos.getTemperature2m().get(i) > dayInfoForEmailDto.getMaxTemperature()){
                dayInfoForEmailDto.setMaxTemperature(hourlyInformationDtos.getTemperature2m().get(i));
            }
        }
        //Calculate average with devision of the previous sum
        dayInfoForEmailDto.setAverageMoningTemperature(dayInfoForEmailDto.getAverageMoningTemperature() / (MORNING_END - MORNING_START));
        dayInfoForEmailDto.setAverageNoonTemperature(dayInfoForEmailDto.getAverageNoonTemperature() / (NOON_END - MORNING_END));
        dayInfoForEmailDto.setAverageAfternoonTemperature(dayInfoForEmailDto.getAverageAfternoonTemperature() / (AFTERNOON_END - NOON_END));
        dayInfoForEmailDto.setImportantWeatherCodes(importantWeatherCodeHours);
        dayInfoForEmailDto.setDay(LocalDate.from(hourlyInformationDtos.getTime().get(0)));
        return dayInfoForEmailDto;
    }

    private String formatImportantCodeHoursOfDay(Map<String, ArrayList<LocalDateTime>> importantWeatherCodes){
        String result = "";
        for (String weatherCodeName : importantWeatherCodes.keySet()){
            result += "<li>";
            result += weatherCodeName + ": ";
            for (int i = 0; i < importantWeatherCodes.get(weatherCodeName).size(); i ++){
                //If the item is the last in the list - we end it with "."
                if (i == importantWeatherCodes.get(weatherCodeName).size() + 1){
                    result += importantWeatherCodes.get(weatherCodeName).get(i).getHour() + ":00.";
                    break;
                }
                //Next hours are comming as well - so we add just ","
                result += importantWeatherCodes.get(weatherCodeName).get(i).getHour() + ":00, ";
            }
            result += "</li>";
        }
        return result;
    }

    private String formatEmailTextFromForecast(OpenMeteoWeatherForecastResponseDto openMeteoWeatherForecastResponseDto, UserEntity user) throws IOException, TemplateException {
        //Day, that we will provide detailed info for - 1 is tomorrow, 0 today and so on
        int day = 1;
        DayInfoForEmailDto dayInfoForEmailDto = getInfoForDayFromWeatherApiResponse(openMeteoWeatherForecastResponseDto);
        Template template = freeMarkerConfig.getConfig().getTemplate("weather_email_text_format.ftl");
        Map root = new HashMap();
        root.put("day", LocalDate.from(openMeteoWeatherForecastResponseDto.getHourly().getTime().get(day * 24)));
        root.put("city", user.getCity().getName());
        root.put("importantWeatherCodes", formatImportantCodeHoursOfDay(dayInfoForEmailDto.getImportantWeatherCodes()));
        root.put("morningTemperature", dayInfoForEmailDto.getAverageMoningTemperature());
        root.put("noonTemperature", dayInfoForEmailDto.getAverageNoonTemperature());
        root.put("afternoonTemperature", dayInfoForEmailDto.getAverageAfternoonTemperature());
        root.put("unsubscribeUrl", baseURL + "/user/deactivate/" + user.getDeactivationCode());
        return this.processTemplate(template, root);
    }

    private String formatSpecialWeatherCodesForHeader(DayInfoForEmailDto dayInfoForEmailDto){
        String result = "";
        Iterator<String> weatherCodesIterator = dayInfoForEmailDto.getImportantWeatherCodes().keySet().iterator();
        while (weatherCodesIterator.hasNext()){
            result += weatherCodesIterator.next();
            //If we have the last weather code, we do not append "," but "."
            if (!weatherCodesIterator.hasNext()){
                result += ".";
                break;
            }
            result += ", ";
        }
        return result;
    }

    //The email headers for the one city are the same, so we can cache them
    @Cacheable(value = "cityEmailHeaderCache", keyGenerator = "defaultKeyGenerator")
    public String formatHeaderTextForEmail(DayInfoForEmailDto dayInfoForEmailDto, String cityName) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getConfig().getTemplate("weather_email_header_format.ftl");
        Map root = new HashMap();
        root.put("city", cityName);
        root.put("day", dayInfoForEmailDto.getDay());
        root.put("minTemperature", dayInfoForEmailDto.getMinTemperature());
        root.put("maxTemperature", dayInfoForEmailDto.getMaxTemperature());
        root.put("specialWeatherCodes", formatSpecialWeatherCodesForHeader(dayInfoForEmailDto));
        return this.processTemplate(template, root);
    }

    public EmailDto fillEmailDtoWithWeather(OpenMeteoWeatherForecastResponseDto openMeteoWeatherForecastResponseDto, UserEntity user) throws TemplateException, IOException {
        EmailDto emailDto = new EmailDto();
        DayInfoForEmailDto dayInfoForEmailDto = getInfoForDayFromWeatherApiResponse(openMeteoWeatherForecastResponseDto);
        emailDto.setText(formatEmailTextFromForecast(openMeteoWeatherForecastResponseDto, user));
        emailDto.setSubject(formatHeaderTextForEmail(dayInfoForEmailDto, user.getCity().getName()));
        return emailDto;
    }

}
