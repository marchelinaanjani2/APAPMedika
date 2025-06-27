package apap.tk.profile.restservice;

import apap.tk.profile.model.Doctor;
import apap.tk.profile.model.EndUser;
import apap.tk.profile.model.Nurse;
import apap.tk.profile.model.Patient;
import apap.tk.profile.repository.DoctorDb;
import apap.tk.profile.repository.EndUserDb;
import apap.tk.profile.repository.PatientDb;
import apap.tk.profile.repository.RoleDb;
import apap.tk.profile.restdto.request.CreateUserRequestDTO;
import apap.tk.profile.restdto.response.EndUserResponseDTO;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class EndUserRestServiceImpl implements EndUserRestService {
    @Autowired
    EndUserDb endUserDb;
    @Autowired
    private PatientDb patientDb;
    @Autowired
    private DoctorDb doctorDb;
    @Autowired
    private RoleDb roleDb;


    @Override
    public List<EndUserResponseDTO> getAllUser() throws EntityNotFoundException {
        var endUsers = endUserDb.findAll();

        var listEndUserResponseDTO = new ArrayList<EndUserResponseDTO>();
        endUsers.forEach(endUser -> {
            var endUserResponseDTO = endUserToEndUserResponseDTO(endUser);
            listEndUserResponseDTO.add(endUserResponseDTO);
        });

        return listEndUserResponseDTO;
    }

    @Override
    public EndUserResponseDTO getEndUserById(UUID id) {
        var endUser = endUserDb.findById(id).orElse(null);
        if (endUser == null) {
            return null;
        }

        return endUserToEndUserResponseDTO(endUser);
    }

    @Override
    public EndUserResponseDTO addUser(CreateUserRequestDTO requestDTO) {
        if (endUserDb.findByEmail(requestDTO.getEmail()) != null || endUserDb.findByUsername(requestDTO.getUsername()) != null) {
            throw new EntityExistsException("User already exists");
        }

        EndUser endUser = new EndUser();
        endUser.setName(requestDTO.getName());
        endUser.setUsername(requestDTO.getUsername());
        endUser.setPassword(hashPassword(requestDTO.getPassword()));
        endUser.setEmail(requestDTO.getEmail());
        endUser.setGender(requestDTO.isGender());
        endUser.setCreatedAt(new Date());
        endUser.setUpdatedAt(new Date());
        endUser.setIsDeleted(false);
        endUser.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));

        if (requestDTO.getRole().equalsIgnoreCase("ADMIN")) {
            endUserDb.save(endUser);
        } else if (requestDTO.getRole().equalsIgnoreCase("NURSE")) {
            Nurse nurse = new Nurse();
            nurse.setName(requestDTO.getName());
            nurse.setUsername(requestDTO.getUsername());
            nurse.setPassword(hashPassword(requestDTO.getPassword()));
            nurse.setEmail(requestDTO.getEmail());
            nurse.setGender(requestDTO.isGender());
            nurse.setCreatedAt(new Date());
            nurse.setUpdatedAt(new Date());
            nurse.setIsDeleted(false);
            nurse.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));
            endUser.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));
            endUser.setId(nurse.getId());

            endUserDb.save(nurse);
        } else if (requestDTO.getRole().equalsIgnoreCase("PATIENT")) {
            Patient patient = new Patient();
            patient.setName(requestDTO.getName());
            patient.setUsername(requestDTO.getUsername());
            patient.setPassword(hashPassword(requestDTO.getPassword()));
            patient.setEmail(requestDTO.getEmail());
            patient.setGender(requestDTO.isGender());
            patient.setCreatedAt(new Date());
            patient.setUpdatedAt(new Date());
            patient.setIsDeleted(false);
            patient.setNik(requestDTO.getNik());
            patient.setBirthPlace(requestDTO.getBirthPlace());
            patient.setBirthDate(requestDTO.getBirthDate());
            patient.setPClass(requestDTO.getPClass());
            patient.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));
            endUser.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));
            endUser.setId(patient.getId());

            patientDb.save(patient);
        } else if (requestDTO.getRole().equalsIgnoreCase("DOCTOR")) {
            Doctor doctor = new Doctor();
            doctor.setName(requestDTO.getName());
            doctor.setUsername(requestDTO.getUsername());
            doctor.setPassword(hashPassword(requestDTO.getPassword()));
            doctor.setEmail(requestDTO.getEmail());
            doctor.setGender(requestDTO.isGender());
            doctor.setCreatedAt(new Date());
            doctor.setUpdatedAt(new Date());
            doctor.setIsDeleted(false);
            doctor.setSpecialist(requestDTO.getSpecialist());
            doctor.setYearsOfExperience(requestDTO.getYearsOfExperience());
            doctor.setFee(requestDTO.getFee());
            doctor.setSchedules(requestDTO.getSchedules());
            doctor.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));
            endUser.setRole(roleDb.findByRole(requestDTO.getRole().toUpperCase()).orElse(null));
            endUser.setId(doctor.getId());

            doctorDb.save(doctor);
        }

        return endUserToEndUserResponseDTO(endUser);
    }

    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public EndUserResponseDTO getDetailUser(UUID id, String username, String email) {
        EndUser endUserId = null;

        if (id != null) {
            endUserId = endUserDb.findById(id).orElse(null);
        }

        EndUser endUserUsername = endUserDb.findByUsername(username);
        EndUser endUserEmail = endUserDb.findByEmail(email);

        int userCount = 0;

        if (endUserId != null) {
            userCount++;
        }
        if (endUserUsername != null) {
            userCount++;
        }
        if (endUserEmail != null) {
            userCount++;
        }

        if (userCount > 1) {
            throw new ConstraintViolationException(
                    String.format("Multiple Users Found"),
                    null
            );
        }

        if (endUserId != null) {
            return endUserDetailToEndUserDetailReponseDTO(endUserId);
        } else if (endUserUsername != null) {
            return endUserDetailToEndUserDetailReponseDTO(endUserUsername);
        } else if (endUserEmail != null) {
            return endUserDetailToEndUserDetailReponseDTO(endUserEmail);
        }

        throw new EntityNotFoundException("User not found");
    }

    private EndUserResponseDTO endUserDetailToEndUserDetailReponseDTO(EndUser endUser) {
        EndUserResponseDTO endUserResponseDTO = new EndUserResponseDTO();

        endUserResponseDTO.setId(endUser.getId());
        endUserResponseDTO.setName(endUser.getName());
        endUserResponseDTO.setUsername(endUser.getUsername());
        endUserResponseDTO.setEmail(endUser.getEmail());
        endUserResponseDTO.setPassword(endUser.getPassword());
        endUserResponseDTO.setGender(endUser.isGender());
        endUserResponseDTO.setIsDeleted(endUser.getIsDeleted());
        endUserResponseDTO.setCreatedAt(endUser.getCreatedAt());
        endUserResponseDTO.setUpdatedAt(endUser.getUpdatedAt());
        endUserResponseDTO.setRole(endUser.getRole().getRole());
        endUserResponseDTO.setPassword(hashPassword(endUser.getPassword()));

        return endUserResponseDTO;
    }

    @Override
    public List<EndUserResponseDTO> getUsersByRole(String role) {
        var endUsers = endUserDb.findAllByRole(roleDb.findByRole(role.toUpperCase()).orElse(null));
        
        if (endUsers.isEmpty()) {
            throw new EntityNotFoundException("No users found with the given role");
        }
        
        var listEndUserResponseDTO = new ArrayList<EndUserResponseDTO>();
        endUsers.forEach(endUser -> {
            var endUserResponseDTO = endUserToEndUserResponseDTO(endUser);
            listEndUserResponseDTO.add(endUserResponseDTO);
        });

        return listEndUserResponseDTO;
    }

    @Override
    public EndUserResponseDTO getEndUserByUsername(String username) {
        var endUser = endUserDb.findByUsername(username);
        if (endUser == null) {
            return null;
        }

        return endUserToEndUserResponseDTO(endUser);
    }

    private EndUserResponseDTO endUserToEndUserResponseDTO(EndUser endUser) {
        var endUserResponseDTO = new EndUserResponseDTO();

        endUserResponseDTO.setId(endUser.getId());
        endUserResponseDTO.setName(endUser.getName());
        endUserResponseDTO.setUsername(endUser.getUsername());
        endUserResponseDTO.setPassword(endUser.getPassword());
        endUserResponseDTO.setEmail(endUser.getEmail());
        endUserResponseDTO.setGender(endUser.isGender());
        endUserResponseDTO.setCreatedAt(endUser.getCreatedAt());
        endUserResponseDTO.setUpdatedAt(endUser.getUpdatedAt());
        endUserResponseDTO.setRole(endUser.getRole().getRole());

        return endUserResponseDTO;
    }
}
