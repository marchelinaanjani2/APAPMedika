package apap.tk.profile.restcontroller;

import apap.tk.profile.restdto.response.BaseResponseDTO;
import apap.tk.profile.restdto.response.DoctorResponseDTO;
import apap.tk.profile.restservice.DoctorRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctor")
public class DoctorRestController {
    @Autowired
    DoctorRestService doctorRestService;

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<DoctorResponseDTO>>> listDoctor() {
        List<DoctorResponseDTO> listDoctor = doctorRestService.getAllDoctor();

        var baseResponseDTO = new BaseResponseDTO<List<DoctorResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listDoctor);
        baseResponseDTO.setMessage("Successfully retrieved doctor list");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BaseResponseDTO<DoctorResponseDTO>> getDoctorById(@PathVariable("id") UUID id) {
        DoctorResponseDTO doctorResponse = doctorRestService.getDoctorById(id);

        var baseResponseDTO = new BaseResponseDTO<DoctorResponseDTO>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(doctorResponse);
        baseResponseDTO.setMessage("Successfully find doctor by id");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
