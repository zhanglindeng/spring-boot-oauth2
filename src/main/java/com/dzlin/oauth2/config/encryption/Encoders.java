package com.dzlin.oauth2.config.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Encoders {
//    @Bean
//    public PasswordEncoder oauthClientPasswordEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }

    @Bean
    public PasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
