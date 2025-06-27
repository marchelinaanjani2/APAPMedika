package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.model.Coverage;
import apap.ti.insurance2206827592.model.Policy;
import apap.ti.insurance2206827592.repository.CompanyDb;
import apap.ti.insurance2206827592.repository.PolicyDb;
import apap.ti.insurance2206827592.restdto.response.PatientResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    PolicyDb policyDb;

    @Autowired
    CompanyDb companyDb;

    @Autowired
    PatientService patientService;

    @Override
    public List<Policy> getAllPolicy() {return policyDb.findByIsDeletedFalse();}

    @Override
    public List<Policy> getAllPolicyFiltered(String status, String minCoverage, String maxCoverage) {
        List<Policy> listPolicy = policyDb.findByIsDeletedFalse();

        if (minCoverage.equals("")) {
            minCoverage = "0";
        }
        if (maxCoverage.equals("")) {
            maxCoverage = "100000000";
        }

        var minCoverageToLong = Long.valueOf(minCoverage);
        var maxCoverageToLong = Long.valueOf(maxCoverage);

        if (status.equals("")) {
            return policyDb.findByTotalCoverageBetweenAndIsDeletedFalse(minCoverageToLong, maxCoverageToLong);
        }

        var statusToInt = Integer.parseInt(status);

        if (!status.isEmpty() && !minCoverage.isEmpty() && !maxCoverage.isEmpty()) {
            return policyDb.findByStatusAndTotalCoverageBetweenAndIsDeletedFalse(statusToInt, minCoverageToLong, maxCoverageToLong);
        } else if (!status.isEmpty()) {
            return policyDb.findByStatusAndIsDeletedFalse(statusToInt);
        }
        return listPolicy;
    }

    @Override
    public Policy addPolicy(Policy policy) {return policyDb.save(policy);}

    @Override
    public Policy getPolicyById(String id) {
        for (Policy policy : getAllPolicy()) {
            if (policy.getId().equals(id)) {
                return policy;
            }
        }
        return null;
    }

    @Override
    public String generatePolicyId(String patientName, String companyName) {
        String prefix = "POL";

        String[] nameParts = patientName.split(" ");
        String patientInitials = "";

        if (nameParts.length > 1) {
            patientInitials = (nameParts[0].substring(0, 1) + nameParts[1].substring(0, 1)).toUpperCase();
        } else {
            patientInitials = nameParts[0].substring(0, 2).toUpperCase();
        }

        String companyInitials = companyName.replace(" ", "").substring(0, 3).toUpperCase();

        int totalPolicies = policyDb.findAll().size();

        String policyNumber = String.format("%04d", totalPolicies + 1);

        return prefix + patientInitials + companyInitials + policyNumber;
    }

    public Company getPolicyCompany(UUID companyId) {
        return companyDb.findById(companyId).orElse(null);
    }

    public PatientResponseDTO getPolicyPatient(UUID patientId) {
        return patientService.getPatientById(patientId);
    }

    public List<Long> getCoverageUsed(String policyId) {
        Policy policy = policyDb.findByIdAndIsDeletedFalse(policyId);

        var listCoverageUsedId = new ArrayList<Long>();

        var listCoverageUsed = policy.getListCoverageUsed();

        for (Coverage coverage : listCoverageUsed) {
            listCoverageUsedId.add(coverage.getId());
        }

        return listCoverageUsedId;
    }
}
