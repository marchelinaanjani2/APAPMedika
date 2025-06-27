package apap.ti.insurance2206827592.restcontroller;

import apap.ti.insurance2206827592.restdto.response.BaseResponseDTO;
import apap.ti.insurance2206827592.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206827592.restdto.response.CoverageResponseDTO;
import apap.ti.insurance2206827592.restservice.CompanyRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
public class CompanyRestController {

    @Autowired
    CompanyRestService companyRestService;

    @GetMapping("/get/{id}/coverage")
    public ResponseEntity<?> companyListCoverage(@PathVariable UUID id) {
        var baseResponseDTO = new BaseResponseDTO<List<CoverageResponseDTO>>();
        List<CoverageResponseDTO> listCoverage = companyRestService.getCompanyCoverageList(id);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listCoverage);
        baseResponseDTO.setMessage(String.format("Coverage list is found."));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/viewall")
    public ResponseEntity<?> getAllCompany() {
        List<CompanyResponseDTO> listCompany = companyRestService.getAllCompany();

        var baseResponseDTO = new BaseResponseDTO<List<CompanyResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listCompany);
        baseResponseDTO.setMessage(String.format("Company list is found."));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
