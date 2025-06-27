package apap.ti.appointment2206082612;

import java.util.ArrayList;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;

import apap.ti.appointment2206082612.model.Treatment;
import apap.ti.appointment2206082612.repository.TreatmentDb;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class Appointment2206082612Application {

	public static void main(String[] args) {
		SpringApplication.run(Appointment2206082612Application.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(TreatmentDb treatmentDb) {
		return args -> {
			String[] treatments = {
				"X-ray", "CT Scan", "MRI", "Ultrasound", "Blood Clotting Test", "Blood Glucose Test",
				"Liver Function Test", "Complete Blood Count", "Urinalysis", "COVID-19 testing", "Cholesterol Test",
				"Inpatient care", "Surgery", "ICU", "ER", "Flu shot", "Hepatitis vaccine", "COVID-19 Vaccine",
				"MMR Vaccine", "HPV Vaccine", "Pneumococcal Vaccine", "Herpes Zoster Vaccine", "Physical exam",
				"Mammogram", "Colonoscopy", "Dental X-ray", "Fillings", "Dental scaling", "Physical therapy",
				"Occupational therapy", "Speech therapy", "Psychiatric evaluation", "Natural delivery", "C-section"
			};
	
			int[] prices = {
				150000, 1000000, 2500000, 300000, 50000, 30000, 75000, 50000, 40000, 150000, 60000, 
				1000000, 7000000, 2000000, 500000, 100000, 200000, 200000, 350000, 800000, 900000, 
				1500000, 250000, 500000, 3000000, 200000, 400000, 500000, 250000, 300000, 300000, 
				600000, 3500000, 12000000
			};

			// Generate treatments
			for (int i = 0; i < 34; i++) {  // Assuming there are 34 treatments to insert
				var treatment = new Treatment();
				treatment.setName(treatments[i]);  // You can adjust this to generate realistic treatment names
				treatment.setPrice(prices[i]); // Set price within a range

				// For timestamps
				treatment.setAppointments(new ArrayList<>());

				// Save treatment data to DB
				treatmentDb.save(treatment);
			}
		};
	}


}
