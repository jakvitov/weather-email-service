package cz.jakvitov.wes.persistence.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import cz.jakvitov.wes.exception.EmailAlreadyInDatabaseException;
import cz.jakvitov.wes.exception.UserNotFoundException;
import cz.jakvitov.wes.persistence.entity.CityEntity;
import cz.jakvitov.wes.persistence.entity.UserEntity;
import cz.jakvitov.wes.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing the user entities
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CityService cityService;

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
        CityEntity city = cityResult.get();
        //If city is not found, we need to create it in the database
        if (cityResult.isEmpty()){
            city = cityService.createNewCity(cityName, countryISO);
        }
        UserEntity userEntity = new UserEntity();
        //For development purposes, the user is active when added
        userEntity.setActive(true);
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setChanged(LocalDateTime.now());
        userEntity.setCity(city);
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
    public void deleteUser(String email, String password) {
    }
}
