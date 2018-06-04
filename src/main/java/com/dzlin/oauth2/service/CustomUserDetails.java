package com.dzlin.oauth2.service;

import com.dzlin.oauth2.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userEntity.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.userEntity.getAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.userEntity.getAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.userEntity.getCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.userEntity.getEnabled();
    }
}
