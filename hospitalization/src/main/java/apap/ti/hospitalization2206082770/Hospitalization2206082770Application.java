package apap.ti.hospitalization2206082770;


import apap.ti.hospitalization2206082770.model.Facility;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.service.NurseService;
import apap.ti.hospitalization2206082770.service.PatientService;
import apap.ti.hospitalization2206082770.service.FacilityService;
import apap.ti.hospitalization2206082770.service.RoomService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@SpringBootApplication
public class Hospitalization2206082770Application {

    public static void main(String[] args) {
        SpringApplication.run(Hospitalization2206082770Application.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner run(RoomService roomService, PatientService patientService, NurseService nurseService, FacilityService facilityService) {
        return args -> {
            @SuppressWarnings("deprecation")
            Faker faker = new Faker(new Locale("id-ID"));
           

            // Generate and save Room data
            // Daftar jenis nama dan kategori
            String[] flowerTypes = {"Mawar", "Melati", "Anggrek", "Kamboja", "Lily"};
            String[] roomTypes = {"Reguler", "Premium", "Deluxe"};

            for (int i = 0; i < 5; i++) {
                Room room = new Room();
                
                // Pilih jenis bunga secara acak
                String flowerType = flowerTypes[faker.number().numberBetween(0, flowerTypes.length)];
                
                // Jika jenis kamar adalah 'Reguler', tambahkan nomor untuk ruangan reguler
                String roomType = roomTypes[faker.number().numberBetween(0, roomTypes.length)];
                String roomName;
                
                if (roomType.equals("Reguler")) {
                    roomName = flowerType + " " + roomType + " " + (i + 1); // Menambahkan nomor untuk ruangan reguler
                } else {
                    roomName = flowerType + " " + roomType; // Hanya nama tanpa nomor untuk jenis kamar lainnya
                }
                
                room.setName(roomName);
                room.setDescription(faker.lorem().sentence());
                room.setMaxCapacity(faker.number().numberBetween(1, 2));
                room.setPricePerDay(faker.number().numberBetween(100000, 500000));

                roomService.createRoom(room);
            }
    
            
            String[] facilityNames = {"WiFi", "Breakfast", "Gym", "Swimming Pool", "Parking", "Spa"};

            for (int i = 0; i < 5; i++) {
                Facility facility = new Facility();
                
                // Pilih nama fasilitas secara acak dari array
                String facilityName = facilityNames[faker.number().numberBetween(0, facilityNames.length)];
                facility.setName(facilityName);
                
                facility.setFee(faker.number().numberBetween(50000, 300000));

                facilityService.createFacility(facility);
            }

        };
    }
    
}
