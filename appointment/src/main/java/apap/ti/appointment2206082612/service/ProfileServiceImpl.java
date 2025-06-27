package apap.ti.appointment2206082612.service;

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

import apap.ti.appointment2206082612.restdto.response.PatientResponseDTO;
import io.github.cdimascio.dotenv.Dotenv;
import apap.ti.appointment2206082612.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206082612.restdto.response.DoctorResponseDTO;
import apap.ti.appointment2206082612.restdto.response.EndUserResponseDTO;

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

    @Override
    public List<PatientResponseDTO> getAllPatientFromRest() {
        var response = webClient.get()
                                .uri("/api/patient/viewall")
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<PatientResponseDTO>>>() {})
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
    public PatientResponseDTO getPatientByNIKFromRest(String nik) {
        var response = webClient.get()
                                .uri("/api/patient/detail?nik=" + nik)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {})
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
    public PatientResponseDTO getPatientById(UUID id) throws EntityNotFoundException {
        var response = webClient.get()
                                .uri("/api/patient/" + id) 
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {})
                                .block();

        if (response == null) {
            throw new EntityNotFoundException("Patient not found di service");
        }

        if (response.getStatus() != 200) {
            throw new EntityNotFoundException("Patient not found");
        }

        return response.getData();
    }

    @Override
    public DoctorResponseDTO getDoctorById(UUID id) throws EntityNotFoundException {
        var response = webClient.get()
                                .uri("/api/doctor/id/" + id) 
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<DoctorResponseDTO>>() {})
                                .block();

        if (response == null) {
            throw new EntityNotFoundException("Doctor not found di service");
        }

        if (response.getStatus() != 200) {
            
            throw new EntityNotFoundException("Doctor not found");
        }

        return response.getData();
    }

}
