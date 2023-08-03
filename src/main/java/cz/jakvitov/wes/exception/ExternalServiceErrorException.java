package cz.jakvitov.wes.exception;

import cz.jakvitov.wes.dto.types.ExternalServices;
import org.springframework.http.HttpStatusCode;

public class ExternalServiceErrorException extends RuntimeException{

    HttpStatusCode httpStatusCode;

    ExternalServices externalService;

    public ExternalServiceErrorException(HttpStatusCode httpStatusCode, ExternalServices externalService) {
        this.httpStatusCode = httpStatusCode;
        this.externalService = externalService;
    }

    public ExternalServiceErrorException(String message, HttpStatusCode httpStatusCode, ExternalServices externalService) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.externalService = externalService;
    }

    public ExternalServiceErrorException(String message, Throwable cause, HttpStatusCode httpStatusCode, ExternalServices externalService) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
        this.externalService = externalService;
    }

    public ExternalServiceErrorException(Throwable cause, HttpStatusCode httpStatusCode, ExternalServices externalService) {
        super(cause);
        this.httpStatusCode = httpStatusCode;
        this.externalService = externalService;
    }

    public ExternalServiceErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatusCode httpStatusCode, ExternalServices externalService) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatusCode = httpStatusCode;
        this.externalService = externalService;
    }
}
