package apap.tk.profile.repository;


import apap.tk.profile.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NurseDb extends JpaRepository<Nurse, UUID> {
    List<Nurse> findAll();

    List<Nurse> findByIsDeletedFalse();

}

