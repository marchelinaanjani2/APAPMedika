package apap.tk.profile.restservice;

import apap.tk.profile.model.Patient;
import apap.tk.profile.repository.PatientDb;
import apap.tk.profile.restdto.response.PatientResponseDTO;
import apap.tk.profile.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientRestServiceImpl implements PatientRestService {

    @Autowired
    PatientDb patientDb;

    @Autowired
    PatientService patientService;


    @Override
    public List<PatientResponseDTO> getAllPatient() throws EntityNotFoundException {
        var listPatient = patientDb.findByIsDeletedFalse();

        var listPatientResponseDTO = new ArrayList<PatientResponseDTO>();
        listPatient.forEach(patient -> {
            var policyResponseDTO = patientToPatientResponseDTO(patient);
            listPatientResponseDTO.add(policyResponseDTO);
        });

        return listPatientResponseDTO;
    }

    @Override
    public PatientResponseDTO getPatientByNik(String nik) throws EntityNotFoundException {

        try {
            Integer.valueOf(nik);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Input must be a number");
        }

        var patient = patientDb.findByNikAndIsDeletedFalse(nik);

        if (patient == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        return patientToPatientResponseDTO(patient);
    }

    @Override
    public PatientResponseDTO getPatientById(UUID id) throws EntityNotFoundException {

        var patient = patientDb.findByIdAndIsDeletedFalse(id);

        if (patient == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        return patientToPatientResponseDTO(patient);
    }

    @Override
    public void upgradeClass(String nik, int pClass) throws EntityNotFoundException {

        try {
            Integer.valueOf(nik);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Input must be a number");
        }

        var patient = patientDb.findByNikAndIsDeletedFalse(nik);

        if (patient == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        var currentClass = patient.getPClass();

        if (pClass > currentClass) {
            throw new IllegalArgumentException("Patient class cannot be downgraded");
        }
        patient.setPClass(pClass);
        patientDb.save(patient);
    }

    private Long getLimit(String nik) {
        var patient = patientDb.findByNikAndIsDeletedFalse(nik);

        if (patient.getPClass() == 1) {
            return 100000000L;
        } else if (patient.getPClass() == 2) {
            return 50000000L;
        } else {
            return 25000000L;
        }
    }

    private PatientResponseDTO patientToPatientResponseDTO(Patient patient) {
        var patientResponseDTO = new PatientResponseDTO();
        long patientLimit = patientService.getLimitInsurance(patient);
        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setUsername(patient.getUsername());
        patientResponseDTO.setPassword(patient.getPassword());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setGender(patient.isGender());
        patientResponseDTO.setCreatedAt(patient.getCreatedAt());
        patientResponseDTO.setUpdatedAt(patient.getUpdatedAt());
        patientResponseDTO.setNik(patient.getNik());
        patientResponseDTO.setBirthPlace(patient.getBirthPlace());
        patientResponseDTO.setBirthDate(patient.getBirthDate());
        patientResponseDTO.setPClass(patient.getPClass());
        patientResponseDTO.setLimit(getLimit(patient.getNik()));
        patientResponseDTO.setLimitInsurance(patientLimit);

        return patientResponseDTO;
    }

//    @Override
//    public PatientResponseDTO getPatientById(UUID id) {
//        if (id == null) {
//            throw new IllegalArgumentException("ID cannot be null");
//        }
//
//        var patient = patientDb.findById(id).orElse(null);
//        if (patient == null) {
//            return null;
//        }
//
//        return patientToPatientResponseDTO(patient);
//    }

    @Override
    public PatientResponseDTO getPatientByUsername(String username) {
        var patient = patientDb.findByUsername(username);
        if (patient == null) {
            return null;
        }
        return patientToPatientResponseDTO(patient);
    }
}
