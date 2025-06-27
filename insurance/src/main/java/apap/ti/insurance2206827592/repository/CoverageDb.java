package apap.ti.insurance2206827592.repository;

import apap.ti.insurance2206827592.model.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverageDb extends JpaRepository<Coverage, Long> {
    List<Coverage> findAll();
}
