package com.dzlin.oauth2.controller;

import com.alibaba.fastjson.JSONObject;
import com.dzlin.oauth2.entity.AuthorityEntity;
import com.dzlin.oauth2.entity.OauthClientDetailsEntity;
import com.dzlin.oauth2.entity.UserEntity;
import com.dzlin.oauth2.payload.AddAuthorityPayload;
import com.dzlin.oauth2.payload.AddClientPayload;
import com.dzlin.oauth2.payload.AddUserPayload;
import com.dzlin.oauth2.payload.GrantAuthorityPayload;
import com.dzlin.oauth2.repository.AuthorityRepository;
import com.dzlin.oauth2.repository.OauthClientDetailsRepository;
import com.dzlin.oauth2.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping(value = "/helper")
public class HelperController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private OauthClientDetailsRepository oauthClientDetailsRepository;

    public HelperController(UserRepository userRepository,
                            AuthorityRepository authorityRepository,
                            OauthClientDetailsRepository oauthClientDetailsRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
    }

    @PostMapping(value = "/client/add")
    public Object addClient(@Valid @RequestBody AddClientPayload payload) {

        final String logPrefix = "[addClient]";

        this.logger.info(logPrefix + payload.toString());

        this.oauthClientDetailsRepository.add(new OauthClientDetailsEntity() {{
            setResourceIds(payload.getResourceIds());
            setScope(payload.getScope());
            setAuthorizedGrantTypes(payload.getAuthorizedGrantTypes());
            setWebServerRedirectUri(payload.getWebServerRedirectUri());
            setAuthorities(payload.getAuthorities());
            setClientId(UUID.randomUUID().toString());
            setClientSecret(RandomStringUtils.randomAlphanumeric(60, 80));
            setAccessTokenValidity(3600);
            setRefreshTokenValidity(7200);
            setAdditionalInformation("");
            setAutoApprove("");
        }});

        return this.success();
    }


    @PostMapping(value = "/authority/grant")
    public Object grantAuthority(@Valid @RequestBody GrantAuthorityPayload payload) {

        final String logPrefix = "[grantAuthority]";

        this.logger.info(logPrefix + payload.toString());

        // find user
        UserEntity userEntity = this.userRepository.findById(payload.getUserId());
        if (userEntity == null) {

            this.logger.warn(logPrefix + "user not fond: " + payload.getUserId());

            return this.failed("user not fond");
        }

        // check user status
        try {
            this.checkUserStatus(userEntity);
        } catch (Exception e) {
            this.logger.warn(logPrefix + e.getMessage());

            return this.failed(e.getMessage());
        }

        // 检查 authority_ids
        if (payload.getAuthorityIds().size() == 0) {

            this.logger.warn(logPrefix + "authority_ids is empty");

            return this.failed("authority_ids is empty");
        }
        Collection<AuthorityEntity> authorityEntities = this.authorityRepository.findByIds(payload.getAuthorityIds());
        if (authorityEntities.isEmpty()) {
            this.logger.warn(logPrefix + "all authority_ids invalid");

            return this.failed("all authority_ids invalid");
        }
        ArrayList<Long> authorityIds = new ArrayList<>();
        for (AuthorityEntity authorityEntity : authorityEntities) {
            authorityIds.add(authorityEntity.getId());
        }

        this.logger.info(logPrefix + authorityIds.toString());

        // 删除已经有的
        Collection<AuthorityEntity> userAuthorities = this.authorityRepository.findByUserId(payload.getUserId());
        for (AuthorityEntity authorityEntity : userAuthorities) {
            authorityIds.remove(authorityEntity.getId());
        }

        if (authorityIds.size() == 0) {

            this.logger.info(logPrefix + "no insert");

            return this.success();
        }

        if (this.userRepository.grantAuthority(payload.getUserId(), authorityIds) > 0) {
            return this.success();
        }

        this.logger.warn(logPrefix + "grant authority failed");

        return this.failed("grant authority failed");
    }

    @PostMapping(value = "/authority/add")
    public Object addAuthority(@Valid @RequestBody AddAuthorityPayload payload) {
        final String logPrefix = "[addAuthority]";

        this.logger.info(logPrefix + payload.toString());

        // exist
        if (this.authorityRepository.exist(payload.getName())) {

            this.logger.warn(logPrefix + "authority existed");

            return this.failed("authority existed: " + payload.getName());
        }

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setName(payload.getName());

        if (this.authorityRepository.add(authorityEntity) > 0) {

            this.logger.info(logPrefix + authorityEntity.toString());

            return this.success();
        }

        this.logger.warn(logPrefix + "add authority failed");

        return this.failed("add authority failed");
    }

    @GetMapping(value = "/user/{username}")
    public Object findUserById(@PathVariable("username") String username) {

        UserEntity userEntity = this.userRepository.findByUsername(username);

        if (userEntity == null) {
            return this.failed("user not fond");
        }

        return userEntity;
    }

    @PostMapping(value = "/user/add")
    public Object addUser(@Valid @RequestBody AddUserPayload payload) {

        final String logPrefix = "[addUser]";

        this.logger.info(logPrefix + payload.toString());

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(payload.getUsername());
        userEntity.setPassword(this.encodePassword(payload.getPassword()));

        if (this.userRepository.add(userEntity) > 0) {

            this.logger.info(logPrefix + userEntity.toString());

            return userEntity;
        }

        this.logger.warn(logPrefix + "add user failed");

        return this.failed("add user failed");
    }

    private String encodePassword(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }

    private Object failed(String message) {
        return this.responseJson(1, message);
    }

    private Object success() {
        return this.responseJson(0, "OK");
    }

    private Object responseJson(Integer code, String message) {
        return new JSONObject() {{
            put("code", code);
            put("message", message);
        }};
    }

    private void checkUserStatus(UserEntity userEntity) throws Exception {

        if (!userEntity.getEnabled()) {
            throw new Exception("user disabled");
        }

        if (userEntity.getAccountLocked()) {
            throw new Exception("user locked");
        }

        if (userEntity.getAccountExpired()) {
            throw new Exception("user expired");
        }
    }
}
