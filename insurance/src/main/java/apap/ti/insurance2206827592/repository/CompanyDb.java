package apap.ti.insurance2206827592.repository;

import apap.ti.insurance2206827592.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyDb extends JpaRepository<Company, UUID> {
    List<Company> findByDeletedAtNull();
}
