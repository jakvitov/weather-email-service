package cz.jakvitov.wes.client;

import cz.jakvitov.wes.dto.external.weather.OpenMeteoErrorResponseDto;
import cz.jakvitov.wes.dto.external.weather.OpenMeteoWeatherForecastResponseDto;
import cz.jakvitov.wes.dto.types.ExternalServices;
import cz.jakvitov.wes.dto.types.WeatherOptions;
import cz.jakvitov.wes.exception.ExternalServiceErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

@Service
public class WeatherApiClientServiceImpl implements WeatherApiClientService{

    private final String DEFAULT_API_URI = "https://api.open-meteo.com/v1/forecast";

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherApiClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "weatherCache", keyGenerator = "defaultKeyGenerator")
    public OpenMeteoWeatherForecastResponseDto getHourlyWeatherForecast(Double latitude, Double longitude, Integer days) {
        String uri = UriComponentsBuilder.fromHttpUrl(DEFAULT_API_URI)
                .queryParam(WeatherOptions.LATITUDE.getValue(), latitude)
                .queryParam(WeatherOptions.LONGITUDE.getValue(), longitude)
                .queryParam(WeatherOptions.DAYS.getValue(), days)
                .queryParam(WeatherOptions.HOURLY_OPTIONS.getValue(),
                        Arrays.asList(WeatherOptions.TEMPERATURE.getValue(), WeatherOptions.RAIN.getValue()
                                , WeatherOptions.WIND_SPEED.getValue(), WeatherOptions.WEATHER_CODE.getValue()))
                .toUriString();
        ResponseEntity<OpenMeteoWeatherForecastResponseDto> response = restTemplate.exchange(uri, HttpMethod.GET, null, OpenMeteoWeatherForecastResponseDto.class);
        if (response.getStatusCode().isError()){
            OpenMeteoErrorResponseDto openMeteoErrorResponseDto = restTemplate.getForObject(uri, OpenMeteoErrorResponseDto.class);
            throw new ExternalServiceErrorException(openMeteoErrorResponseDto.getReason(), response.getStatusCode(), ExternalServices.OPEN_METEO);
        }
        return response.getBody();
    }
}
