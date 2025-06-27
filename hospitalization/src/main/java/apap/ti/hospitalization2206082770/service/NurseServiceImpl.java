package apap.ti.hospitalization2206082770.service;


import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.NurseResponseDTO;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;


@Service
public class NurseServiceImpl implements NurseService {

    private final WebClient webClient;

    private final Dotenv dotenv;
    public NurseServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("PROFILE_API_URL")).build();
    }


    @Override
    public List<NurseResponseDTO> getAllNurseFromRest() {
        var response = webClient.get().uri("/api/nurse/viewall").retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<NurseResponseDTO>>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }


    @Override
    public NurseResponseDTO getNurseById(UUID id) throws EntityNotFoundException {
        var response = webClient.get()
                                .uri("/api/nurse/" + id) 
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<NurseResponseDTO>>() {})
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
