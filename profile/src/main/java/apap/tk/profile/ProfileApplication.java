package apap.tk.profile;

import apap.tk.profile.model.*;
import apap.tk.profile.repository.*;
import apap.tk.profile.restservice.EndUserRestService;
import apap.tk.profile.service.DoctorService;
import apap.tk.profile.service.NurseService;
import apap.tk.profile.service.PatientService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfileApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner run(RoleDb roleDb, PatientDb patientDb, PatientService patientService, DoctorDb doctorDb, DoctorService doctorService, NurseService nurseService, NurseDb nurseDb, EndUserDb endUserDb
    , EndUserRestService endUserRestService) {
        return args -> {
            var faker = new Faker(new Locale("in-ID"));

            if(roleDb.findByRole("ADMIN").orElse(null) == null) {
                Role role = new Role();
                role.setRole("ADMIN");
                roleDb.save(role);
            }
            if(roleDb.findByRole("PATIENT").orElse(null) == null) {
                Role role = new Role();
                role.setRole("PATIENT");
                roleDb.save(role);
            }
            if(roleDb.findByRole("NURSE").orElse(null) == null) {
                Role role = new Role();
                role.setRole("NURSE");
                roleDb.save(role);
            }
            if(roleDb.findByRole("DOCTOR").orElse(null) == null) {
                Role role = new Role();
                role.setRole("DOCTOR");
                roleDb.save(role);
            }
            if(roleDb.findByRole("PHARMACIST").orElse(null) == null) {
                Role role = new Role();
                role.setRole("PHARMACIST");
                roleDb.save(role);
            }

            if (endUserDb.findByUsername("admin") == null) {
                EndUser admin = new EndUser();
                admin.setName("Admin");
                admin.setUsername("admin");
                admin.setPassword(endUserRestService.hashPassword("admin"));
                admin.setEmail("admin@admin.com");
                admin.setGender(false);
                admin.setCreatedAt(new Date());
                admin.setUpdatedAt(new Date());
                admin.setRole(roleDb.findByRole("ADMIN").orElse(null));
                endUserDb.save(admin);
            }

            // Generate Patients
            for (int i = 0; i < 3; i++) {  
                var patient = new Patient();
                patient.setName(faker.name().fullName());
                patient.setUsername(faker.name().username());
                patient.setPassword(faker.internet().password());
                patient.setEmail(faker.internet().emailAddress());
                patient.setGender(faker.bool().bool());
                patient.setCreatedAt(new Date());
                patient.setUpdatedAt(new Date());
                patient.setIsDeleted(false);
                patient.setNik(String.valueOf(patientDb.findAll().size() + 1));
                patient.setBirthPlace(faker.address().city());
                patient.setBirthDate(faker.date().birthday());
                patient.setPClass(faker.number().numberBetween(0, 4));
                patient.setRole(roleDb.findByRole("PATIENT").orElse(null));

                patientService.addPatient(patient);
                endUserDb.save(patient);
            }


			//Generate Nurse
			 for (int i = 0; i < 3; i++) {  
                var nurse = new Nurse();
                nurse.setName(faker.name().fullName());
                nurse.setUsername(faker.name().username());
                nurse.setPassword(faker.internet().password());
                nurse.setEmail(faker.internet().emailAddress());
                nurse.setGender(faker.bool().bool());
                nurse.setCreatedAt(new Date());
                nurse.setUpdatedAt(new Date());
                nurse.setIsDeleted(false);
                nurse.setRole(roleDb.findByRole("NURSE").orElse(null));
                
                nurseService.createNurse(nurse);
            }


            // Generate Doctors
            for (int i = 0; i < 3; i++) {  
                var doctor = new Doctor();
                doctor.setName(faker.name().fullName());
                doctor.setUsername(faker.name().username());
                doctor.setPassword(faker.internet().password());
                doctor.setEmail(faker.internet().emailAddress());
                doctor.setGender(faker.bool().bool());
                doctor.setCreatedAt(new Date());
                doctor.setUpdatedAt(new Date());
                doctor.setIsDeleted(false);

                int specialistCode = faker.number().numberBetween(0, 16);
                String specialist = switch (specialistCode) {
                    case 0 -> "UMM";
                    case 1 -> "PDL";
                    case 2 -> "GGI";
                    case 3 -> "PRU";
                    case 4 -> "ANK";
                    case 5 -> "THT";
                    case 6 -> "BDH";
                    case 7 -> "KSJ";
                    case 8 -> "PRE";
                    case 9 -> "ANS";
                    case 10 -> "JPD";
                    case 11 -> "NRO";
                    case 12 -> "KKL";
                    case 13 -> "URO";
                    case 14 -> "MTA";
                    case 15 -> "OBG";
                    case 16 -> "RAD";
                    default -> "UNK";
                };
                doctor.setSpecialist(specialistCode);

                doctor.setYearsOfExperience(faker.number().numberBetween(1, 10));
                doctor.setFee(faker.number().numberBetween(50000, 300000));

                List<Integer> schedules = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    long timestamp = faker.date().future(40, TimeUnit.DAYS).getTime() / 1000L;
                    schedules.add(faker.number().numberBetween(1, 7));
                }
                doctor.setSchedules(schedules);
                doctor.setRole(roleDb.findByRole("DOCTOR").orElse(null));

                doctorService.addDoctor(doctor);
            }
        };
    }
}
