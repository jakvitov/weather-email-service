package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.client.GeocodingApiClientService;
import cz.jakvitov.wes.dto.external.geocoding.GeocodingCityInfoDto;
import cz.jakvitov.wes.exception.CityNotFoundException;
import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.CityId;
import cz.jakvitov.wes.persistence.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService{

    private final CityRepository cityRepository;

    private final GeocodingApiClientService geocodingApiClientService;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, GeocodingApiClientService geocodingApiClientService) {
        this.cityRepository = cityRepository;
        this.geocodingApiClientService = geocodingApiClientService;
    }

    @Override
    public CityEntity createNewCity(String cityName, String countryISO) {
        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(cityName);
        cityEntity.setCountryISO(countryISO);
        ArrayList<GeocodingCityInfoDto> response = geocodingApiClientService.getGeocodesForCity(cityName, countryISO);
        if (response.isEmpty()){
            throw new CityNotFoundException(cityName, countryISO);
        }
        GeocodingCityInfoDto geocodingCityInfoDto = response.get(0);
        CityId cityId = new CityId();
        cityId.setLatitude(geocodingCityInfoDto.getLatitude());
        cityId.setLongitude(geocodingCityInfoDto.getLongitude());
        cityEntity.setCityId(cityId);
        return cityEntity;
    }

    @Override
    public Optional<CityEntity> findByNameAndCountry(String cityName, String countryISO) {
        return cityRepository.findCityByNameAndCountry(cityName, countryISO);
    }
}
