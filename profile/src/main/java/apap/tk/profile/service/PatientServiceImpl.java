package apap.tk.profile.service;

import apap.tk.profile.model.Patient;
import apap.tk.profile.repository.PatientDb;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientDb patientDb;

    @Autowired
    private EndUserService endUserService;
    
    private List<Patient> getAllPatient() {return patientDb.findByIsDeletedFalse();}

    @Override
    public Patient addPatient(Patient patient) {

        for (Patient p : getAllPatient()) {

            if (patient.getNik().equals(p.getNik())) {
                return null;
            }
        }
        patientDb.save(patient);
        endUserService.addEndUser(patient);

        return patient;
    }

    @Override
    public Patient getPatientById(UUID id) {
        var listPatient = getAllPatient();

        for (var patient : listPatient) {
            if (patient.getId().equals(id)) {
                return patient;
            }
            throw new EntityNotFoundException("Patient not found");
        }
        return null;
    }

    @Override
    public Long getLimitInsurance(Patient patient) {
        long patientLimit;
        if (patient.getPClass() ==  1) {
            patientLimit = 100000000L;
        } else if (patient.getPClass() ==  2) {
            patientLimit = 500000000L;
        }else {
            patientLimit = 250000000L;

        }
        return patientLimit;
    }
}
