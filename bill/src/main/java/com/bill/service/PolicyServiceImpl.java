package com.bill.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bill.restdto.response.AppointmentResponseDTO;
import com.bill.restdto.response.BaseResponseDTO;
import com.bill.restdto.response.PolicyResponseDTO;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class PolicyServiceImpl implements PolicyService  {
    private final WebClient webClient;


    private final Dotenv dotenv;
    public PolicyServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("POLICY_API_URL")).build();
    }



    @Override
    public PolicyResponseDTO getPolicyById(String id) {
        var response = webClient.get()
                                .uri("/api/policy/" + id)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PolicyResponseDTO>>() {})
                                .block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }
}
