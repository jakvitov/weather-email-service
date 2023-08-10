package cz.jakvitov.wes.external.api;

import cz.jakvitov.wes.client.GeocodingApiClientService;
import cz.jakvitov.wes.dto.external.geocoding.GeocodingCityInfoDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class GeocodingApiClientServiceTest {

    @Autowired
    private GeocodingApiClientService geocodingApiClientService;

    @Test
    @Disabled
    public void geocodingApiClientServiceTest(){
        GeocodingCityInfoDto[] geocodingCityInfoDto = geocodingApiClientService.getGeocodesForCity("Praha", "CZ");
        assert geocodingCityInfoDto.length > 0;
    }

}
