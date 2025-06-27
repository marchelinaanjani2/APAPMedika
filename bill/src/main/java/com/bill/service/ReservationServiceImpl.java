package com.bill.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.bill.restdto.response.AppointmentResponseDTO;
import com.bill.restdto.response.BaseResponseDTO;
import com.bill.restdto.response.ReservationResponseDTO;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityNotFoundException;

public class ReservationServiceImpl implements ReservationService{
    private final WebClient webClient;


    private final Dotenv dotenv;
    public ReservationServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("RESERVATION_API_URL")).build();
    }



    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        var response = webClient.get()
                                .uri("/api/reservations/viewall")
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<ReservationResponseDTO>>>() {})
                                .block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }

    
    @Override
    public ReservationResponseDTO getReservationById(String id) throws EntityNotFoundException {
        var response = webClient.get()
                                .uri("/api/reservations/" + id) 
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<ReservationResponseDTO>>() {})
                                .block();

        if (response == null) {
            throw new EntityNotFoundException("Reservation not found di service");
        }

        if (response.getStatus() != 200) {
            throw new EntityNotFoundException("Reservation not found");
        }

        return response.getData();
    }


   
}
