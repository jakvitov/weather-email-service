package cz.jakvitov.wes.dto.controller;

public class UserCreationResponse extends AbstractControllerResponse{

    private String email;

    public UserCreationResponse() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
