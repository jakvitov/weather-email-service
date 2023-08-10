package cz.jakvitov.wes.dto.controller;

public class DeactivateUserResponse extends AbstractControllerResponse{

    public String userEmail;

    public DeactivateUserResponse() {
        super();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
