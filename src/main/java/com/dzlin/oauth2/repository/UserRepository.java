package com.dzlin.oauth2.repository;

import com.dzlin.oauth2.entity.UserEntity;
import com.dzlin.oauth2.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserRepository {

    private UserMapper userMapper;
    private AuthorityRepository authorityRepository;

    public UserRepository(UserMapper userMapper, AuthorityRepository authorityRepository) {
        this.userMapper = userMapper;
        this.authorityRepository = authorityRepository;
    }

    public Long grantAuthority(Long userId, ArrayList<Long> authorityIds) {
        return this.userMapper.grantAuthority(new HashMap<String, Object>() {{
            put("user_id", userId);
            put("authority_ids", authorityIds);
        }});
    }

    public Boolean existById(Long id) {
        return this.findById(id) != null;
    }

    public UserEntity findById(Long id) {
        return this.userMapper.findById(id);
    }

    public Long add(UserEntity userEntity) {
        return this.userMapper.add(userEntity);
    }

    public UserEntity findByUsername(String username) {
        UserEntity userEntity = this.userMapper.findByUsername(username);
        if (userEntity != null) {
            userEntity.setAuthorities(this.authorityRepository.findByUserId(userEntity.getId()));
            return userEntity;
        }

        return null;
    }
}
