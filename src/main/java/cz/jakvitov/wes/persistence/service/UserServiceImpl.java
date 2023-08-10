package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.dto.controller.ActivationUserResponse;
import cz.jakvitov.wes.dto.types.ResponseState;
import cz.jakvitov.wes.exception.EmailAlreadyInDatabaseException;
import cz.jakvitov.wes.exception.UserNotFoundException;
import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.UserEntity;
import cz.jakvitov.wes.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing the user entities
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CityService cityService;

    private final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CityService cityService) {
        this.userRepository = userRepository;
        this.cityService = cityService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(String email, String cityName, String countryISO, String password) {
        if (!userRepository.findAllByEmail(email).isEmpty()){
            throw new EmailAlreadyInDatabaseException(email);
        }
        Optional<CityEntity> cityResult = cityService.findByNameAndCountry(cityName, countryISO);
        CityEntity city = null;
        //If city is not found, we need to create it in the database
        if (cityResult.isEmpty()){
            city = cityService.createNewCity(cityName, countryISO);
        }
        else {
            city = cityResult.get();
        }
        UserEntity userEntity = new UserEntity();
        //For development purposes, the user is active when added
        userEntity.setActive(true);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setChanged(LocalDateTime.now());
        userEntity.setCity(city);
        userEntity.setDeactivationCode(UUID.randomUUID().toString());
        userEntity.setActivationCode(UUID.randomUUID().toString());
        city.getUsers().add(userEntity);
        userEntity = userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity updateUserCity(String email, String cityName, String countryISO) {
        List<UserEntity> userList = userRepository.findAllByEmail(email);
        if (userList.isEmpty()) {
            throw new UserNotFoundException(email);
        }
        UserEntity user = userList.get(0);
        user.getCity().getUsers().remove(user);
        cityService.updateCity(user.getCity());
        Optional<CityEntity> cityResult = cityService.findByNameAndCountry(cityName, countryISO);
        CityEntity city = null;
        if (cityResult.isEmpty()){
            city = cityService.createNewCity(cityName, countryISO);
        }
        else {
            city = cityResult.get();
        }
        user.setCity(city);
        city.getUsers().add(user);
        cityService.updateCity(city);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        List<UserEntity> userEntities = userRepository.findAllByEmail(email);
        if (userEntities.isEmpty()){
            throw new UserNotFoundException(email);
        }
        userRepository.delete(userEntities.get(0));
    }

    @Override
    public List<UserEntity> getActiveUsers() {
        return userRepository.findAllActiveUsers();
    }

    @Override
    public ActivationUserResponse deactivateUserByDeactivationCode(String deactivationCode) {
        ActivationUserResponse response = new ActivationUserResponse();

        List<UserEntity> userEntities = userRepository.findUserEntityByDeactivationCode(deactivationCode);
        if (userEntities.isEmpty()){
            throw new UserNotFoundException();
        }
        if (userEntities.size() > 1){
            throw new RuntimeException("Multiple users found with one deactivation code:"  + deactivationCode);
        }
        UserEntity user = userEntities.get(0);
        if (!user.getActive()){
            response.setResponseState(ResponseState.OK);
            response.setUserEmail(user.getEmail());
            return response;
        }
        user.setActive(false);
        //We generate new activation and deactivation codes
        user.setActivationCode(UUID.randomUUID().toString());
        user.setDeactivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        response.setUserEmail(user.getEmail());
        response.setResponseState(ResponseState.OK);
        return response;
    }

    @Override
    public ActivationUserResponse activateUserByActivatinCode(String activationCode) {
        ActivationUserResponse response = new ActivationUserResponse();
        List<UserEntity> userEntities = userRepository.findUserEntityByActivationCode(activationCode);
        if (userEntities.isEmpty()){
            throw new UserNotFoundException();
        }
        if (userEntities.size() > 1){
            throw new RuntimeException("Multiple users found with one activation code:"  + activationCode);
        }
        UserEntity user = userEntities.get(0);
        if (user.getActive()){
            response.setResponseState(ResponseState.OK);
            response.setUserEmail(user.getEmail());
            return response;
        }
        user.setActive(true);
        //We generate new activation and deactivation codes
        user.setActivationCode(UUID.randomUUID().toString());
        user.setDeactivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        response.setUserEmail(user.getEmail());
        response.setResponseState(ResponseState.OK);
        return response;
    }
}
