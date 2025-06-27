package com.bill.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.bill.restdto.response.AppointmentResponseDTO;
import com.bill.restdto.response.BaseResponseDTO;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;


public class AppointmentServiceImpl implements AppointmentService{
    private final WebClient webClient;


    private final Dotenv dotenv;
    public AppointmentServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("APPOINTMENT_API_URL")).build();
    }



    @Override
    public List<AppointmentResponseDTO> getAllApointment() {
        var response = webClient.get()
                                .uri("/api/appointment/all")
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<AppointmentResponseDTO>>>() {})
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
