package apap.tk.profile.restcontroller;

import apap.tk.profile.restdto.response.BaseResponseDTO;
import apap.tk.profile.restdto.response.PatientResponseDTO;
import apap.tk.profile.restservice.PatientRestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patient")
public class PatientRestController {

    @Autowired
    PatientRestService patientRestService;

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<PatientResponseDTO>>> listPatient() {
        List<PatientResponseDTO> listPatient = patientRestService.getAllPatient();

        var baseResponseDTO = new BaseResponseDTO<List<PatientResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPatient);
        baseResponseDTO.setMessage("Successfully retrieved patient list");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


    @GetMapping("/detail")
    public ResponseEntity<?> getPolicyDetail(@RequestParam("nik") String nik) {
        try {
            var baseResponseDTO = new BaseResponseDTO<PatientResponseDTO>();
            
            PatientResponseDTO policy = patientRestService.getPatientByNik(nik);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(policy);
            baseResponseDTO.setMessage(String.format("Patient with ID %s found", policy.getId()));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            var baseResponseDTO = new BaseResponseDTO<PatientResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Patient data could not be found"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/detail-by-id")
    public ResponseEntity<?> getPolicyDetailById(@RequestParam("id") UUID id) {
        try {
            var baseResponseDTO = new BaseResponseDTO<PatientResponseDTO>();
            PatientResponseDTO policy = patientRestService.getPatientById(id);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(policy);
            baseResponseDTO.setMessage(String.format("Patient with ID %s found", policy.getId()));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            var baseResponseDTO = new BaseResponseDTO<PatientResponseDTO>();
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Patient data could not be found"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/upgrade")
    public ResponseEntity<?> upgradeClass(@RequestParam("nik") String nik, @RequestParam("pClass") int pClass) {
        var baseResponseDTO = new BaseResponseDTO<List<PatientResponseDTO>>();

        try {
            patientRestService.upgradeClass(nik, pClass);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Successfully upgraded patient with NIK %s", nik));
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

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<PatientResponseDTO>> detailPatient(@PathVariable("id") UUID id) {
        var baseResponseDTO = new BaseResponseDTO<PatientResponseDTO>();
        var patient = patientRestService.getPatientById(id);
        
        if (patient == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data patient tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(patient);
        baseResponseDTO.setMessage(String.format("Patient dengan ID %s berhasil ditemukan", id));
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


    @GetMapping("/patientProfile")
    public ResponseEntity<?> getPatientDetails() {
        var baseResponseDTO = new BaseResponseDTO<PatientResponseDTO>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            PatientResponseDTO patient = patientRestService.getPatientByUsername(username);

            if (patient == null) {
                throw new UsernameNotFoundException("Patient not found");
            }
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(patient);
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage("Patient login berhasil diambil");
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Error: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }
}
