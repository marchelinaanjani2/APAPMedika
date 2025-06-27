package apap.tk.profile.service;

import java.util.UUID;

import apap.tk.profile.model.Patient;

public interface PatientService {
    Patient addPatient(Patient patient);
    Patient getPatientById(UUID id);
    Long getLimitInsurance(Patient patient);
}
