package com.dzlin.oauth2.mapper;

import com.dzlin.oauth2.entity.OauthClientDetailsEntity;
import org.springframework.stereotype.Component;

@Component
public interface OauthClientDetailsMapper {
    OauthClientDetailsEntity findByClientId(String clientId);

    void add(OauthClientDetailsEntity entity);
}
