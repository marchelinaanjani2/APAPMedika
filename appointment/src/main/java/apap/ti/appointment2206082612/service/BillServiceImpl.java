package apap.ti.appointment2206082612.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import apap.ti.appointment2206082612.restdto.response.BillResponseDTO;
import apap.ti.appointment2206082612.restdto.response.DoctorResponseDTO;
import apap.ti.appointment2206082612.restdto.response.EndUserResponseDTO;

import org.springframework.http.HttpHeaders;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Service
public class BillServiceImpl implements BillService {

    private final WebClient webClient;

    private final Dotenv dotenv;
    public BillServiceImpl(WebClient.Builder webClientBuilder) {
        this.dotenv = Dotenv.configure()
                .directory("./")
                .load();
        this.webClient = webClientBuilder.baseUrl(dotenv.get("BILL_API_URL")).build();
    }

    @Override
    public BillResponseDTO addBill(String idAppointment, String token, UUID idPatient) {

        Map<String, Object> billRequest = new HashMap<>();
        billRequest.put("appointmentId", idAppointment);
        billRequest.put("patientId", idPatient);

    
        // Menggunakan POST untuk membuat Bill baru
        var response = webClient.post()
                                 .uri("/api/bill/add/forAppointment")  // URL API
                                 .header("Authorization", "Bearer " + token)
                                 .bodyValue(billRequest)  // Mengirimkan body dengan payload
                                 .retrieve()  // Mengambil response
                                 .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<BillResponseDTO>>() {})
                                 .block();  // Menunggu responsenya
    
        // Memeriksa apakah response null atau statusnya tidak OK
        if (response == null || response.getStatus() != 200) {
            return null;  // Jika response null atau statusnya tidak OK (200), return null
        }
    
        // Mengembalikan data BillResponseDTO jika semuanya baik-baik saja
        return response.getData();
    }
}