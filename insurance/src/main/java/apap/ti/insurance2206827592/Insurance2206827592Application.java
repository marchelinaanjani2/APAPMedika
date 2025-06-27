package apap.ti.insurance2206827592;

import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.model.Coverage;
import apap.ti.insurance2206827592.model.Policy;
import apap.ti.insurance2206827592.repository.CoverageDb;
import apap.ti.insurance2206827592.service.CompanyService;
import apap.ti.insurance2206827592.service.PatientService;
import apap.ti.insurance2206827592.service.PolicyService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Insurance2206827592Application {

	public static void main(String[] args) {
		SpringApplication.run(Insurance2206827592Application.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(PolicyService policyService, CompanyService companyService, PatientService patientService, CoverageDb coverageDb) {
		return args -> {
			var faker = new Faker(new Locale("in-ID"));
			Random random = new Random();

			var existingPatients = patientService.getAllPatientFromRest();

			for (int i = 0; i < 2; i++) {

				var patient = existingPatients.get(random.nextInt(existingPatients.size()));

				var company = new Company();
				company.setName(faker.company().name());
				company.setContact(faker.phoneNumber().phoneNumber());
				company.setEmail(faker.internet().emailAddress());
				company.setAddress(faker.address().fullAddress());
				company.setCreatedAt(new Date());
				company.setUpdatedAt(new Date());

				List<Coverage> listCoverage = new ArrayList<>();

				for (int j= 0; j < random.nextInt(1, 11); j++) {
					listCoverage.add(coverageDb.findAll().get(random.nextInt(34)));
				}
				company.setListCoverage(listCoverage);

				var newCompany = companyService.addCompany(company);

				var policy = new Policy();

				policy.setCompanyId(newCompany.getId());
				policy.setPatientId(patient.getId());
				policy.setExpiryDate(faker.date().future(365, TimeUnit.DAYS));
				policy.setTotalCoverage(newCompany.calculateTotalCoverageLong());
				policy.setTotalCovered(0L);
				policy.setCreatedAt(new Date());
				policy.setUpdatedAt(new Date());
				policy.setCreatedBy("neina");
				policy.setUpdatedBy("neina");
				policy.setIsDeleted(false);
				policy.setId(policyService.generatePolicyId(patient.getName(), newCompany.getName()));

				policyService.addPolicy(policy);
			}
		};
	}
}
