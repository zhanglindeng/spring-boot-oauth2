package com.dzlin.oauth2.config;

import com.dzlin.oauth2.config.encryption.ClientPasswordEncoder;
import com.dzlin.oauth2.entity.OauthClientDetailsEntity;
import com.dzlin.oauth2.repository.OauthClientDetailsRepository;
import com.dzlin.oauth2.service.CustomClientDetails;
import com.dzlin.oauth2.service.CustomClientDetailsService;
import com.dzlin.oauth2.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Qualifier("dataSource")
    private DataSource dataSource;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService userDetailsService;
    private OauthClientDetailsRepository oauthClientDetailsRepository;
    private HttpServletRequest httpServletRequest;
    //    private PasswordEncoder oauthClientPasswordEncoder;
//    private CustomClientDetailsService customClientDetailsService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public OAuth2AuthorizationServerConfig(DataSource dataSource,
                                           AuthenticationManager authenticationManager,
                                           CustomUserDetailsService userDetailsService,
//                                           PasswordEncoder oauthClientPasswordEncoder,
//                                           CustomClientDetailsService customClientDetailsService,
                                           OauthClientDetailsRepository oauthClientDetailsRepository,
                                           HttpServletRequest httpServletRequest) {
        this.dataSource = dataSource;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
//        this.oauthClientPasswordEncoder = oauthClientPasswordEncoder;
//        this.customClientDetailsService = customClientDetailsService;
        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
        this.httpServletRequest = httpServletRequest;
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
        clients.withClientDetails(clientDetailsService());
        // clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    private ClientDetailsService clientDetailsService() {
        return clientId -> {

            this.logger.info(this.httpServletRequest.getQueryString());

            OauthClientDetailsEntity client = this.oauthClientDetailsRepository.findByClientId(clientId);
            if (client != null) {

                this.logger.info(client.toString());

                return new CustomClientDetails(client);
            }

            this.logger.warn("client not found: " + clientId);

            throw new ClientRegistrationException("client not found");

        };
    }
}
