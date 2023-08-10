package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.dto.controller.DeactivateUserResponse;
import cz.jakvitov.wes.persistence.entity.UserEntity;

import java.util.List;

public interface UserService {

    public UserEntity createUser(String email, String cityName, String countryISO, String password);

    public UserEntity updateUserCity(String email, String cityName, String countryISO);

    public void deleteUser(String email);

    public List<UserEntity> getActiveUsers();

    public DeactivateUserResponse deactivateUserByDeactivationCode(String deactivationCode);

}
