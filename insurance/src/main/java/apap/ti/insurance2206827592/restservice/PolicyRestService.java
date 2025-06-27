package apap.ti.insurance2206827592.restservice;

import apap.ti.insurance2206827592.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206827592.restdto.request.ListTreatmentRequestRestDTO;
import apap.ti.insurance2206827592.restdto.request.UpdatePolicyRequestRestDTO;
import apap.ti.insurance2206827592.restdto.response.PolicyResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface PolicyRestService {
    List<PolicyResponseDTO> getPatientPolicyFiltered(String nik, String status, String minCoverage, String maxCoverage);

    PolicyResponseDTO getPolicyById(String id) throws EntityNotFoundException;

    List<PolicyResponseDTO> getPatientPolicy(String nik) throws EntityNotFoundException;

    List<PolicyResponseDTO> getPolicyFiltered(String status, String minCoverage, String maxCoverage);

    PolicyResponseDTO getPatientPolicyById(String nik, String id) throws EntityNotFoundException;

    PolicyResponseDTO addPolicy(AddPolicyRequestRestDTO policyDTO);

    List<PolicyResponseDTO> getAllPolicy();

    PolicyResponseDTO updateExpiryDate(UpdatePolicyRequestRestDTO policyDTO, String id) throws EntityNotFoundException;

    List<PolicyResponseDTO> updateStatus() throws EntityNotFoundException;

    void cancelPolicy(String policyId) throws EntityNotFoundException;

    void deletePolicy(String policyId) throws EntityNotFoundException;

    List<PolicyResponseDTO> getListByCoverage(ListTreatmentRequestRestDTO listTreatment) throws EntityNotFoundException;
}
