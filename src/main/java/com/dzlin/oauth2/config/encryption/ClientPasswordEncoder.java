package com.dzlin.oauth2.config.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;

public class ClientPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.contentEquals(rawPassword);
    }
}
