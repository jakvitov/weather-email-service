package cz.jakvitov.wes.controller;

import cz.jakvitov.wes.dto.controller.ActivationUserResponse;
import cz.jakvitov.wes.dto.controller.UserCreationRequest;
import cz.jakvitov.wes.dto.controller.UserCreationResponse;
import cz.jakvitov.wes.dto.types.ErrorLevel;
import cz.jakvitov.wes.dto.types.ResponseState;
import cz.jakvitov.wes.exception.EmailAlreadyInDatabaseException;
import cz.jakvitov.wes.exception.UserNotFoundException;
import cz.jakvitov.wes.persistence.service.MonitoredErrorService;
import cz.jakvitov.wes.persistence.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller, that handles all user related logic
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final MonitoredErrorService monitoredErrorService;

    @Autowired
    public UserController(UserService userService, MonitoredErrorService monitoredErrorService) {
        this.userService = userService;
        this.monitoredErrorService = monitoredErrorService;
    }

    @PostMapping("/activation/deactivate/{deactivationCode}")
    public ResponseEntity<ActivationUserResponse> deactivateUser(@PathVariable(value = "deactivationCode") String deactivationCode) {
        ActivationUserResponse activationUserResponse = new ActivationUserResponse();
        try {
            activationUserResponse = userService.deactivateUserByDeactivationCode(deactivationCode);
            return new ResponseEntity<>(activationUserResponse, HttpStatus.valueOf(200));
        } catch (UserNotFoundException exc) {
            activationUserResponse.setResponseState(ResponseState.USER_NOT_FOUND);
            return new ResponseEntity<>(activationUserResponse, HttpStatusCode.valueOf(404));
        } catch (Exception exception) {
            Long errorId = monitoredErrorService.monitorError(exception, "Error during user deactivation.", ErrorLevel.ERROR);
            activationUserResponse.setErrorId(errorId);
            activationUserResponse.setResponseState(ResponseState.ERROR);
            return new ResponseEntity<>(activationUserResponse, HttpStatusCode.valueOf(500));
        }
    }

    @PostMapping("activation/activate/{activationCode}")
    public ResponseEntity<ActivationUserResponse> activateUser(@PathVariable(value = "activationCode") String activationCode) {
        ActivationUserResponse response = new ActivationUserResponse();
        try {
            response = userService.activateUserByActivatinCode(activationCode);
            return new ResponseEntity<>(response, HttpStatus.valueOf(200));
        } catch (UserNotFoundException exc) {
            response.setResponseState(ResponseState.USER_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        } catch (Exception exception) {
            Long errorId = monitoredErrorService.monitorError(exception, "Error during user activation.", ErrorLevel.ERROR);
            response.setResponseState(ResponseState.ERROR);
            response.setErrorId(errorId);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserCreationResponse> createUser(@RequestBody @Valid UserCreationRequest request){
        UserCreationResponse response = new UserCreationResponse();
        try {
            response = userService.crateUser(request);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }
        catch (EmailAlreadyInDatabaseException exc) {
            response.setResponseState(ResponseState.USER_ALREADY_EXISTS);
            response.setEmail(request.getEmail());
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(409));
        }
        catch (Exception exc){
            Long errorId = monitoredErrorService.monitorError(exc, "Error during user creation.", ErrorLevel.ERROR);
            response.setResponseState(ResponseState.ERROR);
            response.setErrorId(errorId);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }
}