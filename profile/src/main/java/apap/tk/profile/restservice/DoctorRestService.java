package apap.tk.profile.restservice;

import java.util.List;
import java.util.UUID;

import apap.tk.profile.restdto.response.DoctorResponseDTO;
import jakarta.persistence.EntityNotFoundException;


public interface DoctorRestService {
    List<DoctorResponseDTO> getAllDoctor() throws EntityNotFoundException;
    DoctorResponseDTO getDoctorById(UUID id);
    

}
