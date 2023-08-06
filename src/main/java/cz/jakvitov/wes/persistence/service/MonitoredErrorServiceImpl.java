package cz.jakvitov.wes.persistence.service;

import cz.jakvitov.wes.dto.types.ErrorLevel;
import cz.jakvitov.wes.dto.types.ExternalServices;
import cz.jakvitov.wes.persistence.entity.MonitoredError;
import cz.jakvitov.wes.persistence.repository.MonitoredErrorRepository;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoredErrorServiceImpl implements MonitoredErrorService{

    private final MonitoredErrorRepository monitoredErrorRepository;

    @Autowired
    public MonitoredErrorServiceImpl(MonitoredErrorRepository monitoredErrorRepository) {
        this.monitoredErrorRepository = monitoredErrorRepository;
    }

    @Override
    public Long monitorError(Throwable exc, String message, ExternalServices externalService, ErrorLevel errorLevel) {
        MonitoredError monitoredError = new MonitoredError();
        monitoredError.setErrorLevel(errorLevel);
        monitoredError.setExternalService(externalService);
        monitoredError.setMessage(message);
        monitoredError.setStack(exc.getStackTrace().toString());
        monitoredError.setExceptionInfo(exc.getMessage());
        monitoredError.setTraceId(ThreadContext.get("traceId"));
        monitoredError.setService("WES");
        monitoredError = monitoredErrorRepository.save(monitoredError);
        return monitoredError.getErrorId();
    }

    @Override
    public Long monitorError(Throwable exc, String message, ErrorLevel errorLevel) {
        MonitoredError monitoredError = new MonitoredError();
        monitoredError.setErrorLevel(errorLevel);
        monitoredError.setMessage(message);
        monitoredError.setStack(exc.getStackTrace().toString());
        monitoredError.setExceptionInfo(exc.getMessage());
        monitoredError.setTraceId(ThreadContext.get("traceId"));
        monitoredError.setService("WES");
        monitoredError = monitoredErrorRepository.save(monitoredError);
        return monitoredError.getErrorId();
    }
}
