package cz.jakvitov.wes.dto.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCreationRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String cityName;

    @NotNull
    @Size(min = 2, max = 3)
    private String countryISO;

    public UserCreationRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }
}
