package cz.jakvitov.wes.dto.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class DeleteUserRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    public DeleteUserRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
