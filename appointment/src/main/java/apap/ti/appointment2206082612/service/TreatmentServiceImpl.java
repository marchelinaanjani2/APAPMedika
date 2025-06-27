package apap.ti.appointment2206082612.service;

import apap.ti.appointment2206082612.dto.response.BaseResponseDTO;
import apap.ti.appointment2206082612.dto.response.TreatmentResponseDTO;
import apap.ti.appointment2206082612.model.Treatment;
import apap.ti.appointment2206082612.repository.TreatmentDb;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.ArrayList;

@Service
public class TreatmentServiceImpl implements TreatmentService {

    @Autowired
    private TreatmentDb treatmentDb;
    

    private final WebClient webClient;

    private final Dotenv dotenv;
    public TreatmentServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("TREATMENT_API_URL")).build();
    }

    @Override
    public List<Treatment> getAllTreatments() {
        return treatmentDb.findAll();
    }

    @Override
    public TreatmentResponseDTO getTreatmentById(Long id) throws Exception {
        var response = webClient
                .get()
                .uri("/treatment?id=" + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<TreatmentResponseDTO>>() {})
                .block();

        if (response == null) {
            throw new Exception("Failed to consume API getTreatmentById");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    @Override
    public List<Treatment> getTreatmentsByIds(List<Long> treatmentIds) {
        return treatmentDb.findAllById(treatmentIds);
    }
}
