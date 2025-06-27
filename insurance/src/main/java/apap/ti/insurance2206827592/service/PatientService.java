package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.restdto.response.PatientResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    Long getAvailableLimit(PatientResponseDTO patient);

    List<PatientResponseDTO> getAllPatientFromRest();

    PatientResponseDTO getPatientByNIKFromRest(String nik);

    PatientResponseDTO getPatientById(UUID id) throws EntityNotFoundException;
}
