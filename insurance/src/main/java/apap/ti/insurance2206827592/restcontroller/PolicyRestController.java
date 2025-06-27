package apap.ti.insurance2206827592.restcontroller;

import apap.ti.insurance2206827592.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206827592.restdto.request.ListTreatmentRequestRestDTO;
import apap.ti.insurance2206827592.restdto.request.UpdatePolicyRequestRestDTO;
import apap.ti.insurance2206827592.restdto.response.BaseResponseDTO;
import apap.ti.insurance2206827592.restdto.response.PolicyResponseDTO;
import apap.ti.insurance2206827592.restservice.PolicyRestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController

@RequestMapping("/api/policy")
public class PolicyRestController {
    @Autowired
    PolicyRestService policyRestService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPolicyDetail(@PathVariable("id") String id) {
        try {
            var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();
            PolicyResponseDTO policy = policyRestService.getPolicyById(id);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(policy);
            baseResponseDTO.setMessage(String.format("Policy with ID %s found", policy.getId()));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Policy data could not be found"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPolicy(@Valid @RequestBody AddPolicyRequestRestDTO policyDTO, BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        PolicyResponseDTO policy = policyRestService.addPolicy(policyDTO);

        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(policy);
        baseResponseDTO.setMessage(String.format("Successfully added policy with ID %s", policy.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<PolicyResponseDTO>>> listPolicy() {
        List<PolicyResponseDTO> listPolicy = policyRestService.getAllPolicy();

        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage("Successfully retrieved policy list");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/viewall/{nik}")
    public ResponseEntity<BaseResponseDTO<List<PolicyResponseDTO>>> listPatientPolicy(@PathVariable("nik") String nik) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        try {
            List<PolicyResponseDTO> listPatientPolicy = policyRestService.getPatientPolicy(nik);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPatientPolicy);
            baseResponseDTO.setMessage("Successfully retrieved policy list");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/viewall/filtered")
    public ResponseEntity<BaseResponseDTO<List<PolicyResponseDTO>>> listPolicyFiltered(@RequestParam(name="status", defaultValue = "") String status, @RequestParam("minCoverage") String minCoverage, @RequestParam("maxCoverage") String maxCoverage) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        try {
            List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyFiltered(status, minCoverage, maxCoverage);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage("Successfully retrieved policy list");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/viewall/{nik}/filtered")
    public ResponseEntity<BaseResponseDTO<List<PolicyResponseDTO>>> listPatientPolicyFiltered(@PathVariable("nik") String nik, @RequestParam(name="status", defaultValue = "") String status, @RequestParam("minCoverage") String minCoverage, @RequestParam("maxCoverage") String maxCoverage) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        try {
            List<PolicyResponseDTO> listPolicy = policyRestService.getPatientPolicyFiltered(nik, status, minCoverage, maxCoverage);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage("Successfully retrieved policy list");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{nik}/detail")
    public ResponseEntity<?> getPatientPolicyDetail(@PathVariable("nik") String nik, @RequestParam("id") String id) {
        try {
            var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();
            PolicyResponseDTO policy = policyRestService.getPatientPolicyById(nik, id);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(policy);
            baseResponseDTO.setMessage(String.format("Policy with ID %s found", policy.getId()));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{policyId}/update-expiry-date")
    public ResponseEntity<?> updateExpiryDate(@PathVariable("policyId") String policyId, @RequestBody UpdatePolicyRequestRestDTO policyDTO) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policy = policyRestService.updateExpiryDate(policyDTO, policyId);
        if (policy == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Policy with ID %s not found", policyId));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policy);
        baseResponseDTO.setMessage(String.format("Expiry date of policy with ID %s is changed", policy.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus() {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        List<PolicyResponseDTO> policy = policyRestService.updateStatus();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policy);
        baseResponseDTO.setMessage(String.format("Successfully updated policy status"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/{idPolicy}/cancel")
    public ResponseEntity<?> cancelPolicy(@PathVariable("idPolicy") String idPolicy) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        try {
            policyRestService.cancelPolicy(idPolicy);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Successfully cancelled policy with ID %s", idPolicy));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(String.format(e.getMessage()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);

        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format(e.getMessage()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);

        }
    }

    @PutMapping("/{idPolicy}/delete")
    public ResponseEntity<?> deletePolicy(@PathVariable("idPolicy") String idPolicy) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        try {
            policyRestService.deletePolicy(idPolicy);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Successfully deleted policy with ID %s", idPolicy));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

        } catch (ConstraintViolationException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(String.format(e.getMessage()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);

        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format(e.getMessage()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/viewall-by-coverage")
    public ResponseEntity<BaseResponseDTO<List<PolicyResponseDTO>>> listPolicyByCoverage(@RequestBody ListTreatmentRequestRestDTO listTreatment) {
        List<PolicyResponseDTO> listPolicy = policyRestService.getListByCoverage(listTreatment);

        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage("Successfully retrieved policy list");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
