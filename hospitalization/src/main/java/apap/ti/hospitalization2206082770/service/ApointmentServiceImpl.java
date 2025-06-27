package apap.ti.hospitalization2206082770.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import apap.ti.hospitalization2206082770.restdto.response.AppointmentResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import io.github.cdimascio.dotenv.Dotenv;


@Service
public class ApointmentServiceImpl implements ApointmentService{
    
    private final WebClient webClient;

    private final Dotenv dotenv;
    public ApointmentServiceImpl(WebClient.Builder webClientBuilder) {
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
