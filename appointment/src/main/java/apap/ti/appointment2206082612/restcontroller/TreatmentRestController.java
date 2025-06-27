package apap.ti.appointment2206082612.restcontroller;

import apap.ti.appointment2206082612.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206082612.restdto.response.TreatmentResponseRestDTO;
import apap.ti.appointment2206082612.restservice.TreatmentRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/treatment")
public class TreatmentRestController {

    @Autowired
    private TreatmentRestService treatmentRestService;

    // Endpoint untuk mendapatkan semua treatment
    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<TreatmentResponseRestDTO>>> getAllTreatments() {
        List<TreatmentResponseRestDTO> treatments = treatmentRestService.getAllTreatments();

        var baseResponseDTO = new BaseResponseDTO<List<TreatmentResponseRestDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(treatments);
        baseResponseDTO.setMessage("Successfully retrieved all treatments");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    // Endpoint untuk mendapatkan treatment berdasarkan ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<TreatmentResponseRestDTO>> getTreatmentById(@PathVariable("id") Long id) {
        TreatmentResponseRestDTO treatment = treatmentRestService.getTreatmentById(id);

        var baseResponseDTO = new BaseResponseDTO<TreatmentResponseRestDTO>();

        if (treatment == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Treatment not found");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(treatment);
        baseResponseDTO.setMessage("Successfully retrieved treatment by ID");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}

