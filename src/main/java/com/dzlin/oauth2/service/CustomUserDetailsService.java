package com.dzlin.oauth2.service;

import com.dzlin.oauth2.entity.UserEntity;
import com.dzlin.oauth2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.userRepository.findByUsername(username);
        if (user == null) {

            this.logger.warn("username " + username);

            throw new UsernameNotFoundException("user not fond");
        }

        this.logger.info(user.toString());

        return new CustomUserDetails(user);
    }

}
