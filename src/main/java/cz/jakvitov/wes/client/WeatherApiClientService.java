package cz.jakvitov.wes.client;

import cz.jakvitov.wes.dto.external.weather.OpenMeteoWeatherForecastResponseDto;

/**
 * Client for calling the open meteo api to get weather for a city
 */

public interface WeatherApiClientService {

    public OpenMeteoWeatherForecastResponseDto getHourlyWeatherForecast(Double latitude, Double longitude, Integer days);

}
