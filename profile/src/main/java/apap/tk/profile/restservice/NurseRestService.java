package apap.tk.profile.restservice;

import apap.tk.profile.restdto.response.NurseResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface NurseRestService {
    NurseResponseDTO getNurseById(UUID id);
    List<NurseResponseDTO> getAllNurse() throws EntityNotFoundException;
    
    
} 
