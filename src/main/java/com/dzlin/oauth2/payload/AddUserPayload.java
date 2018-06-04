package com.dzlin.oauth2.payload;

import javax.validation.constraints.*;

public class AddUserPayload {

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 20)
    @Pattern(regexp = "^[0-9a-zA-Z]{6,20}$")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
