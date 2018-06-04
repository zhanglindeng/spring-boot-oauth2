package com.dzlin.oauth2.mapper;

import com.dzlin.oauth2.entity.AuthorityEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public interface AuthorityMapper {
    Collection<AuthorityEntity> findByUserId(Long userId);

    Long add(AuthorityEntity authorityEntity);

    AuthorityEntity findByName(String name);

    Collection<AuthorityEntity> findByIds(ArrayList<Long> ids);
}
