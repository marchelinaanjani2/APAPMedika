package apap.ti.hospitalization2206082770.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.repository.ReservationDb;
import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import jakarta.transaction.Transactional;
import apap.ti.hospitalization2206082770.restdto.response.EndUserResponseDTO; 
import org.springframework.http.HttpHeaders;


@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDb reservationDb;
    private final WebClient webClient;

    public ReservationServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }


    @Override
    public EndUserResponseDTO getCurrentUser(String token) {
        try {
            var response = webClient.get()
                .uri("/api/auth/currentUser")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) 
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<EndUserResponseDTO>>() {})
                .block();  
    
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
    
   

    @Override
    public int getTotalReservationCount() {
        List<Reservation> reservations = reservationDb.findByIsDeletedFalse(); 
        return reservations.size(); 
    }

    @Override
    public boolean isReservationIdExists(String reservationId) {
        return reservationDb.existsById(reservationId);
    }

    @Override
    public Reservation getReservationById(String reservationId) {
        for (Reservation reservation : reservationDb.findByIsDeletedFalse()) {
            if (reservation.getId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

}
