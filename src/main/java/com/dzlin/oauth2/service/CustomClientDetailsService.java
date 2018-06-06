package com.dzlin.oauth2.service;

import com.dzlin.oauth2.entity.OauthClientDetailsEntity;
import com.dzlin.oauth2.repository.OauthClientDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private OauthClientDetailsRepository oauthClientDetailsRepository;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public CustomClientDetailsService(OauthClientDetailsRepository oauthClientDetailsRepository,
                                      HttpServletRequest httpServletRequest) {

        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
        this.httpServletRequest = httpServletRequest;
    }

    // TODO 找到 client 的时候，这里运行了 4 次，为什么？
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        this.logger.info(this.httpServletRequest.getQueryString());

        OauthClientDetailsEntity client = this.oauthClientDetailsRepository.findByClientId(clientId);
        if (client != null) {

            this.logger.info(client.toString());

            return new CustomClientDetails(client);
        }

        this.logger.warn("clientId " + clientId);

        throw new ClientRegistrationException("client not found");
    }
}
