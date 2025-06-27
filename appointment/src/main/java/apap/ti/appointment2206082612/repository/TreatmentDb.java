package apap.ti.appointment2206082612.repository;

import apap.ti.appointment2206082612.model.Treatment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentDb extends JpaRepository<Treatment, Long> {
    
    // Mengambil semua treatment dengan sorting
    List<Treatment> findAll(Sort sort);
    
    // Mencari treatment berdasarkan nama yang mengandung kata kunci (case insensitive)
    List<Treatment> findByNameContainingIgnoreCase(String name, Sort sort);
    
    // Mencari treatment berdasarkan harga
    List<Treatment> findByPrice(long price, Sort sort);
    
    // Kombinasi pencarian berdasarkan nama dan harga
    List<Treatment> findByNameContainingIgnoreCaseAndPrice(String name, long price, Sort sort);

    
}
