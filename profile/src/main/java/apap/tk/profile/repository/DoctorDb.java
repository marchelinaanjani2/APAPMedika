package apap.tk.profile.repository;

import apap.tk.profile.model.Doctor;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorDb extends JpaRepository<Doctor, UUID> {
    
    // Mengambil semua dokter dengan sorting
    List<Doctor> findAll();


    List<Doctor> findByIsDeletedFalse();
    Doctor findByIdAndIsDeletedFalse(UUID id);
    // Mencari dokter berdasarkan nama yang mengandung kata kunci (case insensitive)
    List<Doctor> findByNameContainingIgnoreCase(String name, Sort sort);
    
    // Mencari dokter berdasarkan email
    Doctor findByEmail(String email);
    
    // Mencari dokter berdasarkan spesialisasi
    List<Doctor> findBySpecialist(int specialist, Sort sort);
    
    // Kombinasi pencarian berdasarkan nama dan spesialisasi
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialist(String name, int specialist, Sort sort);
    
    boolean existsByEmail(String email);
    
}


