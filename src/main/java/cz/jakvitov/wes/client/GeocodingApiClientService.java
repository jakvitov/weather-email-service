package cz.jakvitov.wes.client;

import cz.jakvitov.wes.dto.external.geocoding.GeocodingCityInfoDto;

import java.util.ArrayList;

public interface GeocodingApiClientService {

    public GeocodingCityInfoDto[] getGeocodesForCity(String city, String countryISO);

}
