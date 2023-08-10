package cz.jakvitov.wes.controller;

import cz.jakvitov.wes.dto.controller.ActivationUserResponse;
import cz.jakvitov.wes.dto.types.ResponseState;
import cz.jakvitov.wes.exception.UserNotFoundException;
import cz.jakvitov.wes.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller, that handles all user related logic
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/deactivate/{deactivationCode}")
    public ResponseEntity<ActivationUserResponse> deactivateUser(@PathVariable(value = "deactivationCode") String deactivationCode) {
        ActivationUserResponse activationUserResponse = new ActivationUserResponse();
        try {
            activationUserResponse = userService.deactivateUserByDeactivationCode(deactivationCode);
            return new ResponseEntity<>(activationUserResponse, HttpStatus.valueOf(200));
        } catch (UserNotFoundException exc) {
            activationUserResponse.setResponseState(ResponseState.USER_NOT_FOUND);
            return new ResponseEntity<>(activationUserResponse, HttpStatusCode.valueOf(404));
        } catch (Exception exception) {
            activationUserResponse.setResponseState(ResponseState.ERROR);
            return new ResponseEntity<>(activationUserResponse, HttpStatusCode.valueOf(500));
        }
    }

    @PostMapping("/activate/{activationCode}")
    public ResponseEntity<ActivationUserResponse> activateUser(@PathVariable(value = "activationCode") String activationCode) {
        ActivationUserResponse response = new ActivationUserResponse();
        try {
            response = userService.activateUserByActivatinCode(activationCode);
            return new ResponseEntity<>(response, HttpStatus.valueOf(200));
        } catch (UserNotFoundException exc) {
            response.setResponseState(ResponseState.USER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        } catch (Exception exception) {
            response.setResponseState(ResponseState.ERROR);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }
}