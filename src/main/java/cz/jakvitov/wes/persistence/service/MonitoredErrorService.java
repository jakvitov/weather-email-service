package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.dto.types.ErrorLevel;
import cz.jakvitov.wes.dto.types.ExternalServices;

/**
 * Service used for monitoring errors
 */
public interface MonitoredErrorService {

    public Long monitorError(Throwable exc, String message, ExternalServices externalService, ErrorLevel errorLevel);

    public Long monitorError(Throwable exc, String message, ErrorLevel errorLevel);

}
