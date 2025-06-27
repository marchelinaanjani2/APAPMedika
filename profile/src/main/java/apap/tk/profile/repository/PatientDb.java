package apap.tk.profile.repository;

import apap.tk.profile.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PatientDb extends JpaRepository<Patient, UUID> {
    List<Patient> findAll();

    List<Patient> findByIsDeletedFalse();

    Patient findByNikAndIsDeletedFalse(String nik);

    Patient findByIdAndIsDeletedFalse(UUID id);
    Patient findByUsername(String username);

}
