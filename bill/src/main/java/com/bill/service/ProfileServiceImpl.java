package com.bill.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bill.restdto.response.BaseResponseDTO;
import com.bill.restdto.response.EndUserResponseDTO;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.http.HttpHeaders;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Service
public class ProfileServiceImpl implements ProfileService {

    private final WebClient webClient;

    private final Dotenv dotenv;
    public ProfileServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("PROFILE_API_URL")).build();
    }



    @Override
    public EndUserResponseDTO getCurrentUser(String token) {
        try {
            // Call the backend API to get current user information
            var response = webClient.get()
                .uri("/api/auth/currentUser")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) 
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<EndUserResponseDTO>>() {})
                .block();  
    
            // Check if the response is null or status code is not 200
            if (response == null || response.getStatus() != 200) {
                System.err.println("Error: " + (response != null ? response.getMessage() : "Unknown error"));
                return null;
            }
    
            return response.getData(); 
        } catch (WebClientResponseException e) {
            
            System.err.println("Error response: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error response: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // Handle other unexpected errors
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }
}