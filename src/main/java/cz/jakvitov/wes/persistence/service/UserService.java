package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.persistence.entity.UserEntity;

public interface UserService {

    public UserEntity createUser(String email, String cityName, String countryISO, String password);

    public UserEntity updateUserCity(String email, String cityName, String countryISO);

    public void deleteUser(String email, String password);

}
