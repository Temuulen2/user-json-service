package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class SoapAuthClient {

    @Value("${soap.service.url:http://localhost:8080}")
    private String soapServiceUrl;

    public boolean validateToken(String token) {
        if (token == null) return false;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> entity = new HttpEntity<>(token, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                soapServiceUrl + "/validate", entity, String.class);
            return "true".equals(response.getBody());
        } catch (Exception e) {
            return false;
        }
    }
}