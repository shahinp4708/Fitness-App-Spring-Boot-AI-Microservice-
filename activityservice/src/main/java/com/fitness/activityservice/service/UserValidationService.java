package com.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public  boolean validateUser(String userId){
        log.info("Received user id for registering activity is " + userId);
        try {
            return Boolean.TRUE.equals(userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        }
        catch (WebClientResponseException e){
            log.error("WebClientResponseException occurred: {}", e.getMessage());
            if(e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("User validation failed: User does not exist " + userId);
            } else {
                throw new RuntimeException("User validation failed: " + e.getStatusCode(), e);
            }
        }
        catch (Exception e) {
            log.error("Unexpected exception during user validation", e);
            throw new RuntimeException("Unexpected error while validating user: " + userId, e);
        }
    }
}
