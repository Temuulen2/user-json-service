package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Proxies /auth/* requests to the SOAP service.
 * This lets the HTTPS frontend avoid Mixed-Content errors.
 *
 * POST /auth/register  { "username": "...", "password": "..." }
 * POST /auth/login     { "username": "...", "password": "..." }
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthProxyController {

    @Value("${soap.service.url:http://localhost:8080}")
    private String soapServiceUrl;

    private final RestTemplate rest = new RestTemplate();

    public static class AuthRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String u) { this.username = u; }
        public String getPassword() { return password; }
        public void setPassword(String p) { this.password = p; }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest req) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AuthRequest> entity = new HttpEntity<>(req, headers);
            ResponseEntity<String> res = rest.postForEntity(soapServiceUrl + "/register", entity, String.class);
            return ResponseEntity.status(res.getStatusCode()).body(res.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Cannot reach SOAP service: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest req) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AuthRequest> entity = new HttpEntity<>(req, headers);
            ResponseEntity<String> res = rest.postForEntity(soapServiceUrl + "/login", entity, String.class);
            return ResponseEntity.status(res.getStatusCode()).body(res.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Cannot reach SOAP service: " + e.getMessage());
        }
    }
}
