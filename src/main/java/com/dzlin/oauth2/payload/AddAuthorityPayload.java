package com.dzlin.oauth2.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AddAuthorityPayload {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[0-9a-zA-Z._]{1,50}$")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AddAuthorityPayload{" +
                "name='" + name + '\'' +
                '}';
    }
}
