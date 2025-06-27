package apap.ti.hospitalization2206082770.service;



import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.PatientResponseDTO;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Service
public class PatientServiceImpl implements PatientService {
    private final WebClient webClient;

    private final Dotenv dotenv;
    public PatientServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("PROFILE_API_URL")).build();
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


   
}
