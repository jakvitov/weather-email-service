package cz.jakvitov.wes.client;

import cz.jakvitov.wes.dto.external.geocoding.GeocodingCityInfoDto;
import cz.jakvitov.wes.dto.external.geocoding.GeocodingErrorResponseDto;
import cz.jakvitov.wes.dto.types.ExternalServices;
import cz.jakvitov.wes.exception.ExternalServiceErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;


/**
 * Service for communication with the Ninjas api geocoding service for city name to geocode translation
 */

@Service
public class GeocodingApiClientServiceImpl implements GeocodingApiClientService{

    private final RestTemplate restTemplate;

    @Value("${api.keys.ninjas-api}")
    private String API_KEY;

    private final String DEFAULT_URI = "https://api.api-ninjas.com/v1/geocoding";

    @Autowired
    public GeocodingApiClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Cacheable(value = "geocodesCache", keyGenerator = "defaultKeyGenerator")
    public GeocodingCityInfoDto[] getGeocodesForCity(String city, String countryISO) {
        String uri = UriComponentsBuilder.fromHttpUrl(DEFAULT_URI)
                .queryParam("city", city)
                .queryParam("country", countryISO).toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", API_KEY);
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);
        ResponseEntity<GeocodingCityInfoDto[]> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, GeocodingCityInfoDto[].class);
        if (response.getStatusCode().isError()){
            GeocodingErrorResponseDto errorResponseDto = restTemplate.getForObject(uri, GeocodingErrorResponseDto.class);
            throw new ExternalServiceErrorException(errorResponseDto.getError() + errorResponseDto.getMessage(), response.getStatusCode(), ExternalServices.NINJAS_GEOCODING_API);
        }
        return response.getBody();
    }
}
