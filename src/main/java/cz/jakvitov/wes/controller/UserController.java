package cz.jakvitov.wes.controller;

import cz.jakvitov.wes.dto.controller.DeactivateUserResponse;
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
    public ResponseEntity<DeactivateUserResponse> deactivateUser(@PathVariable(value = "deactivationCode") String deactivationCode){
        DeactivateUserResponse deactivateUserResponse = new DeactivateUserResponse();
        try {
            deactivateUserResponse = userService.deactivateUserByDeactivationCode(deactivationCode);
            return new ResponseEntity<>(deactivateUserResponse, HttpStatus.valueOf(200));
        }
        catch (UserNotFoundException exc){
            deactivateUserResponse.setResponseState(ResponseState.USER_NOT_FOUND);
            return new ResponseEntity<>(deactivateUserResponse, HttpStatusCode.valueOf(404));
        }
        catch (Exception exception){
            deactivateUserResponse.setResponseState(ResponseState.ERROR);
            return new ResponseEntity<>(deactivateUserResponse, HttpStatusCode.valueOf(500));
        }
    }

}
