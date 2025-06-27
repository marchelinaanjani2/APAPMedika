package apap.ti.insurance2206827592.restservice;

import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.model.Coverage;
import apap.ti.insurance2206827592.model.Policy;
import apap.ti.insurance2206827592.repository.CompanyDb;
import apap.ti.insurance2206827592.repository.PolicyDb;
import apap.ti.insurance2206827592.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206827592.restdto.request.ListTreatmentRequestRestDTO;
import apap.ti.insurance2206827592.restdto.request.UpdatePolicyRequestRestDTO;
import apap.ti.insurance2206827592.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206827592.restdto.response.PolicyResponseDTO;
import apap.ti.insurance2206827592.service.PatientService;
import apap.ti.insurance2206827592.service.PolicyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
@Transactional
public class PolicyRestServiceImpl implements PolicyRestService {

    @Autowired
    PolicyDb policyDb;

    @Autowired
    CompanyDb companyDb;

    @Autowired
    PolicyService policyService;

    @Autowired
    PatientService patientService;

    @Autowired
    CompanyRestService companyRestService;

    @Override
    public List<PolicyResponseDTO> getAllPolicy() throws EntityNotFoundException {
        var listPolicy = policyDb.findByIsDeletedFalse();
        var listPolicyResponseDTO = new ArrayList<PolicyResponseDTO>();
        listPolicy.forEach(policy -> {
            var policyResponseDTO = policyToPolicyResponseDTO(policy);
            listPolicyResponseDTO.add(policyResponseDTO);
        });

        return listPolicyResponseDTO;
    }

    @Override
    public List<PolicyResponseDTO> getPatientPolicy(String nik) throws EntityNotFoundException {
        var patientToView = patientService.getPatientByNIKFromRest(nik);

        if (patientToView == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        var listPolicy = policyDb.findByIsDeletedFalse();
        var listPolicyResponseDTO = new ArrayList<PolicyResponseDTO>();

        for (var policy : listPolicy) {
            if (policy.getPatientId().equals(patientToView.getId()) && !policy.getIsDeleted()) {
                var policyResponseDTO = policyToPolicyResponseDTO(policy);
                listPolicyResponseDTO.add(policyResponseDTO);
            }
        }

        return listPolicyResponseDTO;
    }

    @Override
    public List<PolicyResponseDTO> getPolicyFiltered(String status, String minCoverage, String maxCoverage) {
        var listPolicy = policyService.getAllPolicyFiltered(status, minCoverage, maxCoverage);

        var listPolicyResponseDTO = new ArrayList<PolicyResponseDTO>();
        listPolicy.stream()
                .filter(policy -> !policy.getIsDeleted())
                .forEach(policy -> {
                    var policyResponseDTO = policyToPolicyResponseDTO(policy);
                    listPolicyResponseDTO.add(policyResponseDTO);
                });

        return listPolicyResponseDTO;
    }

    @Override
    public List<PolicyResponseDTO> getPatientPolicyFiltered(String nik, String status, String minCoverage, String maxCoverage) {
        var patient = patientService.getPatientByNIKFromRest(nik);

        if (patient == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        var listPolicy = policyService.getAllPolicyFiltered(status, minCoverage, maxCoverage);

        var listPolicyResponseDTO = new ArrayList<PolicyResponseDTO>();

        for (Policy policy : listPolicy) {
            var policyResponseDTO = policyToPolicyResponseDTO(policy);

            if (policy.getPatientId().equals(patient.getId())) {
                listPolicyResponseDTO.add(policyResponseDTO);
            }
        }
        return listPolicyResponseDTO;
    }

    @Override
    public PolicyResponseDTO getPolicyById(String id) throws EntityNotFoundException {
        Policy policyToView = policyDb.findByIdAndIsDeletedFalse(id);

        if (policyToView == null) {
            throw new EntityNotFoundException("Policy not found");
        }

        return policyToPolicyResponseDTO(policyToView);
    }

    @Override
    public PolicyResponseDTO getPatientPolicyById(String nik, String id) throws EntityNotFoundException {
        var patientToView = patientService.getPatientByNIKFromRest(nik);

        if (patientToView == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        var listPolicy = policyDb.findByIsDeletedFalse();

        for (var policy : listPolicy) {
            if (policy.getPatientId().equals(patientToView.getId()) && policy.getId().equals(id) && !policy.getIsDeleted()) {
                return policyToPolicyResponseDTO(policy);
            }
        }
        throw new EntityNotFoundException("Policy not found");
    }

    @Override
    public PolicyResponseDTO addPolicy(AddPolicyRequestRestDTO policyDTO) {
        Policy newPolicy = new Policy();

        var patient = patientService.getPatientById(policyDTO.getPatientId());
        if (patient == null) {
            throw new EntityNotFoundException("Patient not found");
        }

        Company company = companyDb.findById(policyDTO.getCompanyId()).orElse(null);
        if (company == null) {
            throw new EntityNotFoundException("Company not found");
        }
        if (company.calculateTotalCoverageLong() > patientService.getAvailableLimit(patient)) {
            throw new IllegalArgumentException("Available limit of the patient is insufficient for new policy.");
        }

        var listPolicy = policyDb.findByIsDeletedFalse();
        var listPatientPolicy = new ArrayList<Policy>();

        for (var policy : listPolicy) {
            if (policy.getPatientId().equals(patient.getId())) {
                listPatientPolicy.add(policy);
            }
        }

        for (Policy policy : listPatientPolicy) {
            if (policy.getCompanyId().equals(company.getId()) && policy.getStatus() != 4) {
                throw new IllegalArgumentException("Patient already have a policy with this company");
            }
        }

        newPolicy.setId(policyService.generatePolicyId(patientService.getPatientById(policyDTO.getPatientId()).getName(), companyDb.findById(policyDTO.getCompanyId()).orElse(null).getName()));
        newPolicy.setCompanyId(policyDTO.getCompanyId());
        newPolicy.setPatientId(policyDTO.getPatientId());
        newPolicy.setStatus(0);
        newPolicy.setExpiryDate(policyDTO.getExpiryDate());
        newPolicy.setTotalCoverage(company.calculateTotalCoverageLong());
        newPolicy.setTotalCovered(0L);
        newPolicy.setCreatedAt(new Date());
        newPolicy.setUpdatedAt(new Date());
        newPolicy.setIsDeleted(false);
        newPolicy.setUpdatedBy("neina");
        newPolicy.setCreatedBy("neina");

        policyDb.save(newPolicy);
        return policyToPolicyResponseDTO(newPolicy);
    }

    @Override
    public PolicyResponseDTO updateExpiryDate(UpdatePolicyRequestRestDTO policyDTO, String policyId) throws EntityNotFoundException {
        Policy policyToUpdate = policyDb.findByIdAndIsDeletedFalse(policyId);

        if (policyToUpdate == null) {
            throw new EntityNotFoundException("Policy not found");
        }

        policyToUpdate.setExpiryDate(policyDTO.getExpiryDate());

        return policyToPolicyResponseDTO(policyToUpdate);
    }

    @Override
    public List<PolicyResponseDTO> updateStatus() throws EntityNotFoundException {

        var listPolicy = policyDb.findByIsDeletedFalse();
        var listPolicyResponseDTO = new ArrayList<PolicyResponseDTO>();
        LocalDate today = LocalDate.now();

        for (Policy policy : listPolicy) {
            LocalDate expiryDate = policy.getExpiryDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (expiryDate.isBefore(today)) {
                policy.setStatus(3);
            } else if (policy.getTotalCovered().equals(policy.getTotalCoverage())) {
                policy.setStatus(2);
            } else if (policy.getTotalCovered() == 0) {
                policy.setStatus(0);
            } else if (policy.getTotalCovered() > 0) {
                policy.setStatus(1);
            }

            policyDb.save(policy);
            listPolicyResponseDTO.add(policyToPolicyResponseDTO(policy));
        }

        return listPolicyResponseDTO;
    }

    @Override
    public void cancelPolicy(String policyId) throws EntityNotFoundException {
        Policy policyToUpdate = policyDb.findByIdAndIsDeletedFalse(policyId);

        if (policyToUpdate == null) {
            throw new EntityNotFoundException("Policy not found");
        }

        if (policyToUpdate.getStatus() != 0) {
            throw new IllegalArgumentException("Policy with status other than 0 cannot be cancelled");
        }

        policyToUpdate.setStatus(4);
        policyDb.save(policyToUpdate);
    }

    @Override
    public void deletePolicy(String policyId) throws EntityNotFoundException {
        Policy policyToDelete = policyDb.findByIdAndIsDeletedFalse(policyId);

        if (policyToDelete == null) {
            throw new EntityNotFoundException("Policy not found");
        }

        if (policyToDelete.getStatus() != 0) {
            throw new IllegalArgumentException("Policy with status other than 0 cannot be deleted");
        }

        policyToDelete.setIsDeleted(true);
        policyDb.save(policyToDelete);
    }

    @Override
    public List<PolicyResponseDTO> getListByCoverage(ListTreatmentRequestRestDTO listTreatment) throws EntityNotFoundException {
        var listPolicy = policyDb.findByIsDeletedFalse();

        var listPolicyResponseDTO = new ArrayList<PolicyResponseDTO>();

        for (Policy policy : listPolicy) {
            Company company = companyDb.findById(policy.getCompanyId()).orElse(null);

            if (company != null) {
                List<Long> allCoverageId = company.getListCoverageId();

                for (Long treatment : listTreatment.getListIdTreatment()) {
                    boolean isUsed = false;
                    for (Coverage coverageUsed : policy.getListCoverageUsed()) {
                        if (listTreatment.getListIdTreatment().contains(coverageUsed.getId())) {
                            isUsed = true;
                            break;
                        }
                    }
                    if (isUsed) {
                        continue;
                    }
                    if (allCoverageId.contains(treatment)) {
                        listPolicyResponseDTO.add(policyToPolicyResponseDTO(policy));
                        break;
                    }
                }
            }
        }
        return listPolicyResponseDTO;
    }

    private PolicyResponseDTO policyToPolicyResponseDTO(Policy policy) {
        var policyResponseDTO = new PolicyResponseDTO();

        policyResponseDTO.setId(policy.getId());
        policyResponseDTO.setCompany(companyToCompanyResponseDTO(policyService.getPolicyCompany(policy.getCompanyId())));
        policyResponseDTO.setPatient(policyService.getPolicyPatient(policy.getPatientId()));
        policyResponseDTO.setStatus(policy.getStatus());

        LocalDateTime newExpiryDate = policy.getExpiryDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime newExpiryDateTime = newExpiryDate.withHour(12);
        Date adjustedExpiryDate = Date.from(newExpiryDateTime.atZone(ZoneId.systemDefault()).toInstant());

        policyResponseDTO.setExpiryDate(adjustedExpiryDate);
        policyResponseDTO.setTotalCoverage(policy.getTotalCoverage());
        policyResponseDTO.setTotalCovered(policy.getTotalCovered());
        policyResponseDTO.setCreatedAt(policy.getCreatedAt());
        policyResponseDTO.setUpdatedAt(policy.getUpdatedAt());
        policyResponseDTO.setCreatedBy(policy.getCreatedBy());
        policyResponseDTO.setUpdatedBy(policy.getUpdatedBy());
        policyResponseDTO.setListIdCoverageUsed(policyService.getCoverageUsed(policy.getId()));

        return policyResponseDTO;
    }

    private CompanyResponseDTO companyToCompanyResponseDTO(Company company) {
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();

        companyResponseDTO.setId(company.getId());
        companyResponseDTO.setName(company.getName());
        companyResponseDTO.setContact(company.getContact());
        companyResponseDTO.setEmail(company.getEmail());
        companyResponseDTO.setAddress(company.getAddress());
        companyResponseDTO.setCreatedAt(company.getCreatedAt());
        companyResponseDTO.setUpdatedAt(company.getUpdatedAt());
        companyResponseDTO.setListCoverage(companyRestService.getCompanyCoverageList(company.getId()));
        return companyResponseDTO;
    }
}
