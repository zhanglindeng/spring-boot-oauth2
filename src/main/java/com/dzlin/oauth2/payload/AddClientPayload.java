package com.dzlin.oauth2.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AddClientPayload {

    @JsonAlias(value = "resource_ids")
    private String resourceIds;

    @NotBlank
    private String scope;

    @NotBlank
    @JsonAlias(value = "authorized_grant_types")
    @Pattern(regexp = "(client_credentials|password|authorization_code|refresh_token|implicit)+", message = "无效的authorized_grant_types")
    private String authorizedGrantTypes;

    @URL
    @JsonAlias(value = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @NotBlank
    private String authorities;

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "AddClientPayload{" +
                "resourceIds='" + resourceIds + '\'' +
                ", scope='" + scope + '\'' +
                ", authorizedGrantTypes='" + authorizedGrantTypes + '\'' +
                ", webServerRedirectUri='" + webServerRedirectUri + '\'' +
                ", authorities='" + authorities + '\'' +
                '}';
    }
}
