package apap.ti.hospitalization2206082770.service;

import java.util.List;


import java.util.UUID;

import apap.ti.hospitalization2206082770.restdto.response.NurseResponseDTO;
import jakarta.persistence.EntityNotFoundException;

public interface NurseService {
    List<NurseResponseDTO> getAllNurseFromRest();

    NurseResponseDTO getNurseById(UUID id) throws EntityNotFoundException;

}
