package cz.jakvitov.wes.persistence;

import cz.jakvitov.wes.persistence.repository.CityRepository;
import cz.jakvitov.wes.persistence.service.CityService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CityServiceTest {

    @Autowired
    CityService cityService;

    @Autowired
    CityRepository cityRepository;


    @Test
    @Disabled
    public void createCity(){
        cityService.createNewCity("Praha", "CZ");
    }
}
