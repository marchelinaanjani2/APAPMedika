package apap.tk.profile.restservice;

import apap.tk.profile.restdto.response.PatientResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface PatientRestService {
    List<PatientResponseDTO> getAllPatient() throws EntityNotFoundException;

    PatientResponseDTO getPatientByNik(String nik) throws EntityNotFoundException;

    PatientResponseDTO getPatientById(UUID id) throws EntityNotFoundException;

    void upgradeClass(String nik, int pClass) throws EntityNotFoundException;
    PatientResponseDTO getPatientByUsername(String username);
}
