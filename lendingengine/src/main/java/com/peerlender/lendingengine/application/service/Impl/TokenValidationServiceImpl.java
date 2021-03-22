package com.peerlender.lendingengine.application.service.Impl;

import com.peerlender.lendingengine.domain.exception.UserNotFoundException;
import com.peerlender.lendingengine.domain.model.User;
import com.peerlender.lendingengine.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenValidationServiceImpl implements TokenValidationService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String securityContextBaseUrl;

    @Autowired
    public TokenValidationServiceImpl(final UserRepository userRepository, @Value("${security.baseurl}") final String securityContextBaseUrl) {
        this.userRepository = userRepository;
        this.securityContextBaseUrl = securityContextBaseUrl;
    }

    public User validateTokenAndGetUser(final String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(httpHeaders.AUTHORIZATION , token);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        ResponseEntity<String> response   = restTemplate
                .exchange(securityContextBaseUrl + "/user/validate", HttpMethod.POST
                , httpEntity ,String.class);

        if (response.getStatusCode().equals(HttpStatus.OK)){
            return userRepository.findById(response.getBody())
                    .orElseThrow(() -> new UserNotFoundException(response.getBody()));
        }else {
            throw new RuntimeException("Invalid Token");
        }
        
    }


}
