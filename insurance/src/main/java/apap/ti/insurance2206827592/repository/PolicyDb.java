package apap.ti.insurance2206827592.repository;

import apap.ti.insurance2206827592.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyDb extends JpaRepository<Policy, String> {
    List<Policy> findByIsDeletedFalse();

    Policy findByIdAndIsDeletedFalse(String id);
    
    List<Policy> findByTotalCoverageBetweenAndIsDeletedFalse(Long minCoverageToLong, Long maxCoverageToLong);

    List<Policy> findByStatusAndTotalCoverageBetweenAndIsDeletedFalse(int statusToInt, Long minCoverageToLong, Long maxCoverageToLong);

    List<Policy> findByStatusAndIsDeletedFalse(int statusToInt);
}
