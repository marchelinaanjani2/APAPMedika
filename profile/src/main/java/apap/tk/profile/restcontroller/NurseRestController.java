package apap.tk.profile.restcontroller;

import apap.tk.profile.restdto.response.BaseResponseDTO;
import apap.tk.profile.restdto.response.NurseResponseDTO;
import apap.tk.profile.restservice.NurseRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nurse")
public class NurseRestController {

    @Autowired
    private NurseRestService nurseRestService;

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<NurseResponseDTO>>> listNurse() {
        List<NurseResponseDTO> listNurse = nurseRestService.getAllNurse();

        var baseResponseDTO = new BaseResponseDTO<List<NurseResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listNurse);
        baseResponseDTO.setMessage("Successfully retrieved nurses list");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<NurseResponseDTO>> detailNurse(@PathVariable("id") UUID id) {
        var baseResponseDTO = new BaseResponseDTO<NurseResponseDTO>();
        var nurse = nurseRestService.getNurseById(id);
        
        if (nurse == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data nurse tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(nurse);
        baseResponseDTO.setMessage(String.format("Nurse dengan ID %s berhasil ditemukan", id));
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
