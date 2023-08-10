package cz.jakvitov.wes.dto.controller;

import cz.jakvitov.wes.dto.types.ResponseState;

import java.time.LocalDateTime;

/**
 * Abstract class, from which the controller responses are derived from
 */
public abstract class AbstractControllerResponse {

    private LocalDateTime generatedAt;

    private ResponseState responseState;

    public AbstractControllerResponse() {
        this.generatedAt = LocalDateTime.now();
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public ResponseState getResponseState() {
        return responseState;
    }

    public void setResponseState(ResponseState responseState) {
        this.responseState = responseState;
    }
}
