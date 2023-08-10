package cz.jakvitov.wes.dto.controller;

public class ActivationUserResponse extends AbstractControllerResponse{

    public String userEmail;

    public ActivationUserResponse() {
        super();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
