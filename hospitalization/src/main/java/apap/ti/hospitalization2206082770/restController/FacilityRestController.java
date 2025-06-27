package apap.ti.hospitalization2206082770.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.hospitalization2206082770.restService.FacilityRestService;
import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.FacilityResponseDTO;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/facility")
public class FacilityRestController {

    private final FacilityRestService facilityRestService;

    public FacilityRestController(FacilityRestService facilityRestService) {
        this.facilityRestService = facilityRestService;
    }

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<FacilityResponseDTO>>> listFacility() {
        List<FacilityResponseDTO> listFacility = facilityRestService.getAllFacility();

        var baseResponseDTO = new BaseResponseDTO<List<FacilityResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listFacility);
        baseResponseDTO.setMessage("List Facility berhasil diambil");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
