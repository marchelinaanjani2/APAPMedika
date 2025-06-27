package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.model.Policy;
import apap.ti.insurance2206827592.repository.PolicyDb;
import apap.ti.insurance2206827592.restdto.response.BaseResponseDTO;
import apap.ti.insurance2206827592.restdto.response.PatientResponseDTO;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PolicyDb policyDb;

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
        var response = webClient.get().uri("/api/patient/viewall").retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<PatientResponseDTO>>>() {}).block();

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
        var response = webClient.get().uri("/api/patient/detail?nik=" + nik).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {}).block();

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
        var listPatient = getAllPatientFromRest();

        for (var patient : listPatient) {
            if (patient.getId().equals(id)) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public Long getAvailableLimit(PatientResponseDTO patient) {
        long patientLimit;

        if (patient.getPClass() == 1) {
            patientLimit = 100000000L;
        } else if (patient.getPClass() == 2) {
            patientLimit = 50000000L;
        } else {
            patientLimit = 25000000L;
        }
        Long totalCoverage = 0L;

        var listPolicy = policyDb.findByIsDeletedFalse();
        var listPatientPolicy = new ArrayList<Policy>();

        for (var policy : listPolicy) {
            if (policy.getPatientId() == patient.getId() && !policy.getIsDeleted()) {
                listPatientPolicy.add(policy);
            }
        }

        for (Policy policy : listPatientPolicy) {
            if (policy.getStatus() != 4) {
                totalCoverage += policy.getTotalCoverage();
            }
        }

        return patientLimit - totalCoverage;
    }
}
