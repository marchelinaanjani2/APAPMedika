package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.model.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    Company addCompany(Company company);
    List<Company> getAllCompany();
    Company getCompanyById(UUID id);
    void deleteCompany(Company company);
    Company updateCompany(Company companyFromDTO);
    Long getTotalCoverage(Company company);
}
