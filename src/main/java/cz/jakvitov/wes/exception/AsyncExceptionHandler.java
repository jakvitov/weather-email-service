package cz.jakvitov.wes.exception;

import cz.jakvitov.wes.dto.types.ErrorLevel;
import cz.jakvitov.wes.dto.types.ExternalServices;
import cz.jakvitov.wes.persistence.service.MonitoredErrorService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    Logger logger = LogManager.getLogger(AsyncExceptionHandler.class);
    private final MonitoredErrorService monitoredErrorService;

    @Autowired
    public AsyncExceptionHandler(MonitoredErrorService monitoredErrorService) {
        this.monitoredErrorService = monitoredErrorService;
    }

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (ex instanceof MessagingException){
            Long errorId = monitoredErrorService.monitorError(ex, ex.getMessage(), ExternalServices.SEZNAM_SMTP_CLIENT, ErrorLevel.WARN);
            logger.warn("Error with sending mail. Monitored: " + errorId);
        }
        //General other exception during an async call
        else {
            Long errorId = monitoredErrorService.monitorError(ex, ex.getMessage(), ErrorLevel.ERROR);
            logger.error("Error during async operation. Monitored: " + errorId);
        }
    }


}
