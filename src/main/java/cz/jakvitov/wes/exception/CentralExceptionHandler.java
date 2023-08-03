package cz.jakvitov.wes.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CentralExceptionHandler {

    Logger logger = LogManager.getLogger(CentralExceptionHandler.class);

    @ExceptionHandler(ExternalServiceErrorException.class)
    public void handleExternalServiceError(ExternalServiceErrorException exc){
        logger.warn("External service error " + exc.externalService + ". Http status code " + exc.getHttpStatusCode()
                + ". Message: " + exc.getMessage());
    }

    @ExceptionHandler(CityNotFoundException.class)
    public void handleCityNotFoundException(CityNotFoundException cityNotFound){
        logger.warn("City not found: " + cityNotFound.getCityName() + ", country: " + cityNotFound.countryISO);
    }

    @ExceptionHandler(Exception.class)
    public void handleGeneralException(Exception exc){
        logger.error("Exception occured: " + exc);
    }

    @ExceptionHandler(EmailAlreadyInDatabaseException.class)
    public void handleUserAlreadyInDatabase(EmailAlreadyInDatabaseException exc){
        logger.info("User already in database: " + exc.email);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public void userNotFoundExceptionHandler(UserNotFoundException exc){
        logger.info("User not found in database.");
    }
}
