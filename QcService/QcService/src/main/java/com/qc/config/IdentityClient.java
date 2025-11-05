package com.qc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IdentityClient {
    private final RestTemplate restTemplate;

    @Value("${identity.service.url:http://IDENTITY-SERVICE}")
    private String IDENTITY_BASE_URL;

    @Autowired
    public IdentityClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> validateToken(String token) {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                IDENTITY_BASE_URL + "/auth/validate?token=" + token, Map.class);
        return response.getBody();
    }
}
