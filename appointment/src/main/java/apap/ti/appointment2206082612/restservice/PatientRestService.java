package apap.ti.appointment2206082612.restservice;

import apap.ti.appointment2206082612.restdto.request.CreatePatientRequestRestDTO;
import apap.ti.appointment2206082612.restdto.response.PatientResponseRestDTO;

import java.util.List;
import java.util.UUID;

public interface PatientRestService {
    PatientResponseRestDTO addPatient(CreatePatientRequestRestDTO patientDTO);
    PatientResponseRestDTO getPatientById(UUID id);
    PatientResponseRestDTO getPatientByNik(String nik);
    List<PatientResponseRestDTO> getAllPatients();
}
