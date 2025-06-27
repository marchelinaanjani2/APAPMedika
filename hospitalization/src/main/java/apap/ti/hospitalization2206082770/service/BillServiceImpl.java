package apap.ti.hospitalization2206082770.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.BillResponseDTO;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BillServiceImpl{
    
    private final WebClient webClient;


    private final Dotenv dotenv;
    public BillServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("BILL_API_URL")).build();
    }


    public BillResponseDTO createBill(String reservationId, double reservationFee, UUID patientId, String token) {
        Map<String, Object> billRequest = new HashMap<>();
        billRequest.put("reservationId", reservationId);
        billRequest.put("reservationFee", reservationFee);
        billRequest.put("patientId", patientId); 
        System.out.println("Request body: " + billRequest);

        var response = webClient.post() 
                                .uri("/api/bill/add/forReservation")
                                .header("Authorization", "Bearer " + token)
                                .bodyValue(billRequest) 
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<BillResponseDTO>>() {})
                                .block();
    
        if (response == null || response.getStatus() != 200) {
            return null; 
        }
    
        return response.getData(); 
    }
}
