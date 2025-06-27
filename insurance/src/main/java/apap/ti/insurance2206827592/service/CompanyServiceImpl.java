package apap.ti.insurance2206827592.service;

import apap.ti.insurance2206827592.model.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.repository.CompanyDb;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyDb companyDb;

    @Override
    public Company addCompany(Company company) { return companyDb.save(company); }

    @Override
    public List<Company> getAllCompany() { return companyDb.findByDeletedAtNull(); }

    @Override
    public Company getCompanyById(UUID id) {
        for (Company company : getAllCompany()) {
            if (company.getId().equals(id)) {
                return company;
            }
        }
        return null;
    }

    @Override
    public void deleteCompany(Company company) {

        Company getCompany = getCompanyById(company.getId());
        getCompany.setDeletedAt(new Date());

        companyDb.save(getCompany);
    }

    @Override
    public Company updateCompany(Company company) {

        Company getCompany = getCompanyById(company.getId());

        if (getCompany != null) {
            getCompany.setName(company.getName());
            getCompany.setAddress(company.getAddress());
            getCompany.setEmail(company.getEmail());
            getCompany.setContact(company.getContact());
            getCompany.setListCoverage(company.getListCoverage());

            companyDb.save(getCompany);

            return getCompany;
        }
        return null;
    }

    @Override
    public Long getTotalCoverage(Company company) {
        List<Coverage> listCoverage = company.getListCoverage();
        Long total = 0L;

        for (Coverage coverage: listCoverage) {
            total += coverage.getCoverageAmount();
        }

        return total;
    }
}
