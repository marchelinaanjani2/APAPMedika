package apap.ti.insurance2206827592.restservice;

import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.model.Coverage;
import apap.ti.insurance2206827592.repository.CompanyDb;
import apap.ti.insurance2206827592.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206827592.restdto.response.CoverageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CompanyRestServiceImpl implements CompanyRestService {

    @Autowired
    CompanyDb companyDb;

    @Override
    public List<CoverageResponseDTO> getCompanyCoverageList(UUID idCompany) {
        Company company = companyDb.findById(idCompany).orElse(null);

        if (company == null) {
            throw new EntityNotFoundException("Company could not be found.");
        }

        List<Coverage> listCoverage = company.getListCoverage();
        List<CoverageResponseDTO> listCoverageResponseDTO = new ArrayList<>();

        for (Coverage coverage : listCoverage) {
            var coverageResponseDTO = coverageToCoverageResponseDTO(coverage);
            listCoverageResponseDTO.add(coverageResponseDTO);
        }

        return listCoverageResponseDTO;
    }

    @Override
    public List<CompanyResponseDTO> getAllCompany() {
        List<Company> listCompany = companyDb.findAll();

        List<CompanyResponseDTO> listCompanyResponseDTO = new ArrayList<>();
        for (Company company : listCompany) {
            var companyResponseDTO = companyToCompanyResponseDTO(company);
            listCompanyResponseDTO.add(companyResponseDTO);
        }
        return listCompanyResponseDTO;
    }

    private CoverageResponseDTO coverageToCoverageResponseDTO(Coverage coverage){
        var coverageResponseDTO = new CoverageResponseDTO();

        coverageResponseDTO.setId(coverage.getId());
        coverageResponseDTO.setName(coverage.getName());
        coverageResponseDTO.setCoverageAmount(coverage.getCoverageAmount());
        coverageResponseDTO.setCreatedAt(coverage.getCreatedAt());
        coverageResponseDTO.setUpdatedAt(coverage.getUpdatedAt());
        coverageResponseDTO.setListCompany(new ArrayList<UUID>());

        return coverageResponseDTO;
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
        companyResponseDTO.setListCoverage(getCompanyCoverageList(company.getId()));
        return companyResponseDTO;
    }
}
