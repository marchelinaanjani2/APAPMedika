package apap.ti.hospitalization2206082770.repository;

import apap.ti.hospitalization2206082770.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FacilityDb extends JpaRepository<Facility, UUID> {
}
