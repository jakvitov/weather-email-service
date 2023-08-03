package cz.jakvitov.wes.external_api_test;

import cz.jakvitov.wes.client.WeatherApiClientService;
import cz.jakvitov.wes.dto.external.OpenMeteoWeatherForecastResponseDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenMeteoClientTest {

    @Autowired
    private WeatherApiClientService weatherApiClientService;

    @Test
    @Disabled
    public void getPragueWeather(){
        OpenMeteoWeatherForecastResponseDto openMeteoWeatherForecastResponseDto = weatherApiClientService.getHourlyWeatherForecast(50.4, 14.1, 2);
        assert openMeteoWeatherForecastResponseDto.getHourly() != null;
    }

}
