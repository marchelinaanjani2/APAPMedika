package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.model.Policy;
import apap.ti.insurance2206827592.restdto.response.PatientResponseDTO;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PolicyService {
    List<Policy> getAllPolicyFiltered(String status, String minCoverage, String maxCoverage);

    List<Policy> getAllPolicy();

    Policy addPolicy(Policy policy);

    Policy getPolicyById(String id);

    String generatePolicyId(String patientName, String companyName);

    Company getPolicyCompany(UUID companyId);

    PatientResponseDTO getPolicyPatient(UUID patientId);

    List<Long> getCoverageUsed(String policyId);
}
