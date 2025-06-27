package apap.tk.profile.restservice;

import apap.tk.profile.restdto.request.CreateUserRequestDTO;
import apap.tk.profile.restdto.response.EndUserResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface EndUserRestService {
    EndUserResponseDTO getEndUserById(UUID id);
    List<EndUserResponseDTO> getAllUser() throws EntityNotFoundException;
    String hashPassword(String password);
    EndUserResponseDTO addUser(CreateUserRequestDTO requestDTO);
    EndUserResponseDTO getDetailUser(UUID id, String username, String password) throws EntityNotFoundException;
    List<EndUserResponseDTO> getUsersByRole(String role);
    EndUserResponseDTO getEndUserByUsername(String username);
    
} 
