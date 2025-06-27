package apap.ti.insurance2206827592.restservice;

import apap.ti.insurance2206827592.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206827592.restdto.response.CoverageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CompanyRestService {
    List<CoverageResponseDTO> getCompanyCoverageList(UUID idCompany);

    List<CompanyResponseDTO> getAllCompany();
}
