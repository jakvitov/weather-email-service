package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.persistence.entity.CityEntity;

import java.util.Optional;

/**
 * Service for managing the city entities
 */
public interface CityService {

    public CityEntity createNewCity(String cityName, String countryISO);

    public Optional<CityEntity> findByNameAndCountry(String cityName, String countryISO);

}
