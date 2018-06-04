package com.dzlin.oauth2.service;

import com.dzlin.oauth2.entity.OauthClientDetailsEntity;
import com.dzlin.oauth2.repository.OauthClientDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private OauthClientDetailsRepository oauthClientDetailsRepository;

    public CustomClientDetailsService(OauthClientDetailsRepository oauthClientDetailsRepository) {

        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        OauthClientDetailsEntity client = this.oauthClientDetailsRepository.findByClientId(clientId);
        if (client != null) {

            this.logger.info(client.toString());

            return new CustomClientDetails(client);
        }

        this.logger.warn("clientId " + clientId);

        throw new ClientRegistrationException("client not found");
    }
}
