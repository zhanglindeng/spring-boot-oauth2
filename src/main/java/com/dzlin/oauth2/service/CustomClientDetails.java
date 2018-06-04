package com.dzlin.oauth2.service;

import com.dzlin.oauth2.entity.AuthorityEntity;
import com.dzlin.oauth2.entity.OauthClientDetailsEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

public class CustomClientDetails implements ClientDetails {

    private OauthClientDetailsEntity oauthClientDetailsEntity;

    public CustomClientDetails(OauthClientDetailsEntity oauthClientDetailsEntity) {
        this.oauthClientDetailsEntity = oauthClientDetailsEntity;
    }

    @Override
    public String getClientId() {
        return this.oauthClientDetailsEntity.getClientId();
    }

    @Override
    public Set<String> getResourceIds() {
        String resourceIds = this.oauthClientDetailsEntity.getResourceIds();
        if (StringUtils.isNotEmpty(resourceIds)) {
            return new HashSet<>(Arrays.asList(StringUtils.split(resourceIds, ",")));
        }
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return false;
    }

    @Override
    public String getClientSecret() {
        return this.oauthClientDetailsEntity.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        String resourceIds = this.oauthClientDetailsEntity.getScope();
        if (StringUtils.isNotEmpty(resourceIds)) {
            return new HashSet<>(Arrays.asList(StringUtils.split(resourceIds, ",")));
        }
        return null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        String resourceIds = this.oauthClientDetailsEntity.getAuthorizedGrantTypes();
        if (StringUtils.isNotEmpty(resourceIds)) {
            return new HashSet<>(Arrays.asList(StringUtils.split(resourceIds, ",")));
        }
        return null;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        String resourceIds = this.oauthClientDetailsEntity.getWebServerRedirectUri();
        if (StringUtils.isNotEmpty(resourceIds)) {
            return new HashSet<>(Arrays.asList(StringUtils.split(resourceIds, ",")));
        }
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        String authorities = this.oauthClientDetailsEntity.getAuthorities();
        if (StringUtils.isNotEmpty(authorities)) {
            String[] abc = StringUtils.split(authorities, ",");
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            AuthorityEntity temp;
            for (Integer i = 0; i < abc.length; i++) {
                temp = new AuthorityEntity();
                temp.setId(0L);
                temp.setName(abc[i]);
                grantedAuthorities.add(temp);
            }

            return grantedAuthorities;
        }
        return null;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.oauthClientDetailsEntity.getAccessTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.oauthClientDetailsEntity.getRefreshTokenValidity();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
