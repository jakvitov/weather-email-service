package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.dto.controller.*;
import cz.jakvitov.wes.dto.types.ResponseState;
import cz.jakvitov.wes.exception.EmailAlreadyInDatabaseException;
import cz.jakvitov.wes.exception.UserNotFoundException;
import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.UserEntity;
import cz.jakvitov.wes.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.OutputKeys;
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

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CityService cityService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cityService = cityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity createUser(String email, String cityName, String countryISO) {
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
        userEntity.setActive(false);
        userEntity.setEmail(email);
        userEntity.setChanged(LocalDateTime.now());
        userEntity.setCity(city);
        userEntity.setDeactivationCode(null);
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
        if (userEntities.size() > 1){
            throw new RuntimeException("Multiple users found with one deactivation code:"  + email.hashCode());
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
        user.setDeactivationCode(null);
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
        user.setActivationCode(null);
        user.setDeactivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        response.setUserEmail(user.getEmail());
        response.setResponseState(ResponseState.OK);
        return response;
    }

    @Override
    public UserCreationResponse crateUser(UserCreationRequest userCreationRequest) {
        UserEntity user = this.createUser(userCreationRequest.getEmail(), userCreationRequest.getCityName(), userCreationRequest.getCountryISO());
        UserCreationResponse response = new UserCreationResponse();
        response.setEmail(user.getEmail());
        response.setResponseState(ResponseState.OK);
        return response;
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        UserEntity user = this.updateUserCity(request.getEmail(), request.getCityName(), request.getCountryISO());
        UpdateUserResponse response = new UpdateUserResponse();
        response.setLatitude(user.getCity().getCityId().getLatitude());
        response.setLongitude(user.getCity().getCityId().getLongitude());
        response.setCityName(user.getCity().getName());
        response.setCountryISO(user.getCity().getCountryISO());
        response.setResponseState(ResponseState.OK);
        return response;
    }

    @Override
    public ActivationUserResponse deleteUser(DeleteUserRequest request) {
        String email = request.getEmail();
        this.deleteUser(request.getEmail());
        ActivationUserResponse response = new ActivationUserResponse();
        response.setUserEmail(email);
        response.setResponseState(ResponseState.OK);
        return response;
    }

    @Override
    public UserInfoResponse getUserInfo(String email) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        List<UserEntity> userEntities = this.userRepository.findAllByEmail(email);
        if (userEntities.isEmpty()){
            userInfoResponse.setResponseState(ResponseState.USER_NOT_FOUND);
            return userInfoResponse;
        }
        if (userEntities.size() > 1){
            throw new RuntimeException("Multiple users found with email: "  + email.hashCode());
        }
        UserEntity user = userEntities.get(0);
        userInfoResponse.setActive(user.getActive());
        userInfoResponse.setCity(user.getCity().getName());
        userInfoResponse.setLatitude(user.getCity().getCityId().getLatitude());
        userInfoResponse.setLongitude(user.getCity().getCityId().getLongitude());
        userInfoResponse.setCountryISO(user.getCity().getCountryISO());
        userInfoResponse.setResponseState(ResponseState.OK);
        return userInfoResponse;
    }
}
