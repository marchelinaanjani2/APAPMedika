package apap.tk.profile.restservice;



import apap.tk.profile.model.Nurse;
import apap.tk.profile.repository.NurseDb;
import apap.tk.profile.restdto.response.NurseResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

import java.util.List;

import java.util.UUID;


@Service
public class NurseRestServiceImpl implements NurseRestService {
    @Autowired
    NurseDb nurseDb;


    @Override
    public List<NurseResponseDTO> getAllNurse() throws EntityNotFoundException {
        var nurses = nurseDb.findAll();

        var listNurseResponseDTO = new ArrayList<NurseResponseDTO>();
        nurses.forEach(nurse -> {
            var nurseResponseDTO = nurseToNurseResponseDTO(nurse);
            listNurseResponseDTO.add(nurseResponseDTO);
        });
    
        return listNurseResponseDTO;
    }

    @Override
    public NurseResponseDTO getNurseById(UUID id) {
        var nurse = nurseDb.findById(id).orElse(null);
        if (nurse == null) {
            return null; 
        }

       return nurseToNurseResponseDTO(nurse);
    }


    private NurseResponseDTO nurseToNurseResponseDTO(Nurse nurse) {
        var nurseResponseDTO = new NurseResponseDTO();

        nurseResponseDTO.setId(nurse.getId());
        nurseResponseDTO.setName(nurse.getName());
        nurseResponseDTO.setUsername(nurse.getUsername());
        nurseResponseDTO.setPassword(nurse.getPassword());
        nurseResponseDTO.setEmail(nurse.getEmail());
        nurseResponseDTO.setGender(nurse.isGender());
        nurseResponseDTO.setCreatedAt(nurse.getCreatedAt());
        nurseResponseDTO.setUpdatedAt(nurse.getUpdatedAt());
        
        return nurseResponseDTO;
    }
}
