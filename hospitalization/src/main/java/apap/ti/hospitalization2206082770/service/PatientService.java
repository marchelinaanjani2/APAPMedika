package apap.ti.hospitalization2206082770.service;


import java.util.List;
import java.util.UUID;

import apap.ti.hospitalization2206082770.restdto.response.PatientResponseDTO;
import jakarta.persistence.EntityNotFoundException;


public interface PatientService {
    List<PatientResponseDTO> getAllPatientFromRest();

    PatientResponseDTO getPatientByNIKFromRest(String nik);

    PatientResponseDTO getPatientById(UUID id) throws EntityNotFoundException;
}


    

