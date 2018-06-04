package com.dzlin.oauth2.config;

import com.dzlin.oauth2.config.encryption.ClientPasswordEncoder;
import com.dzlin.oauth2.service.CustomClientDetailsService;
import com.dzlin.oauth2.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Qualifier("dataSource")
    private DataSource dataSource;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userDetailsService;
    //    private PasswordEncoder oauthClientPasswordEncoder;
    private CustomClientDetailsService customClientDetailsService;

    @Autowired
    public OAuth2AuthorizationServerConfig(DataSource dataSource,
                                           AuthenticationManager authenticationManager,
                                           CustomUserDetailsService userDetailsService,
//                                           PasswordEncoder oauthClientPasswordEncoder,
                                           CustomClientDetailsService customClientDetailsService) {
        this.dataSource = dataSource;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
//        this.oauthClientPasswordEncoder = oauthClientPasswordEncoder;
        this.customClientDetailsService = customClientDetailsService;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(new ClientPasswordEncoder())
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);
        // clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }
}
