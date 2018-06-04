package com.dzlin.oauth2.repository;

import com.dzlin.oauth2.entity.AuthorityEntity;
import com.dzlin.oauth2.mapper.AuthorityMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthorityRepository {

    private AuthorityMapper authorityMapper;

    public AuthorityRepository(AuthorityMapper authorityMapper) {
        this.authorityMapper = authorityMapper;
    }

    public Boolean exist(String name) {
        return this.authorityMapper.findByName(name) != null;
    }

    public Collection<AuthorityEntity> findByIds(ArrayList<Long> ids) {
        return this.authorityMapper.findByIds(ids);
    }

    public Long add(AuthorityEntity authorityEntity) {
        return this.authorityMapper.add(authorityEntity);
    }

    public Collection<AuthorityEntity> findByUserId(Long userId) {
        return this.authorityMapper.findByUserId(userId);
    }
}
