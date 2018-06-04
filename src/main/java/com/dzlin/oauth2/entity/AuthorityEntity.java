package com.dzlin.oauth2.entity;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityEntity implements GrantedAuthority {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
