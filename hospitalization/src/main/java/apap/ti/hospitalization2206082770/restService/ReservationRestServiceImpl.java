package apap.ti.hospitalization2206082770.restService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import apap.ti.hospitalization2206082770.model.Facility;
import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.repository.FacilityDb;
import apap.ti.hospitalization2206082770.repository.ReservationDb;
import apap.ti.hospitalization2206082770.repository.RoomDb;
import apap.ti.hospitalization2206082770.restdto.request.AddReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.request.UpdateReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.response.AppointmentResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.BillResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.EndUserResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082770.service.ApointmentService;
import apap.ti.hospitalization2206082770.service.BillServiceImpl;
import apap.ti.hospitalization2206082770.service.NurseService;
import apap.ti.hospitalization2206082770.service.PatientService;
import apap.ti.hospitalization2206082770.service.ReservationService;
import apap.ti.hospitalization2206082770.service.RoomCapacityService;
import apap.ti.hospitalization2206082770.service.RoomService;

@Service
@Transactional
public class ReservationRestServiceImpl implements ReservationRestService {
    
    @Autowired
    private ReservationDb reservationDb;

    @Autowired
    private FacilityDb facilityDb;

    @Autowired
    private RoomDb roomDb;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BillServiceImpl billServiceImpl;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ApointmentService apointmentService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomCapacityService roomCapacityService;

    @Override
    public List<ReservationResponseDTO> getAllReservations(String token) {
        EndUserResponseDTO currentUser = reservationService.getCurrentUser(token); 
        if (currentUser == null) {
            return new ArrayList<>();
        }

        String role = currentUser.getRole();
        UUID userId = currentUser.getId();
        List<Reservation> filteredReservations;

        if ("NURSE".equalsIgnoreCase(role)) {
            filteredReservations = reservationDb.findByIsDeletedFalseAndNurse(userId);
        } else if ("PATIENT".equalsIgnoreCase(role)) {
            filteredReservations = reservationDb.findByIsDeletedFalseAndPatientId(userId);
        } else {
            filteredReservations = reservationDb.findByIsDeletedFalse();
        }

        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
        filteredReservations.forEach(reservation -> {
            var reservationResponseDTO = reservationToReservationResponseDTO(reservation);
            listReservationResponseDTO.add(reservationResponseDTO);
        });

        return listReservationResponseDTO;
    }
    

    @Override
    public ReservationResponseDTO reservationToReservationResponseDTO(Reservation reservation) {
        var reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservation.getId());
        reservationResponseDTO.setTotalFee(reservation.getTotalFee());
        reservationResponseDTO.setDateIn(reservation.getDateIn());
        reservationResponseDTO.setDateOut(reservation.getDateOut());
        reservationResponseDTO.setRoomId(reservation.getRoomId());
        var roomResponseDTO = roomService.getRoomById(reservation.getRoomId().getId());
        reservationResponseDTO.setRoomName(roomResponseDTO.getName());

        reservationResponseDTO.setPatientId(reservation.getPatientId());
        reservationResponseDTO.setNurse(reservation.getNurse());
        reservationResponseDTO.setCreatedDate(reservation.getCreatedDate());
        reservationResponseDTO.setUpdatedDate(reservation.getUpdatedDate());
        reservationResponseDTO.setStatus(reservation.getStatus());

        var patientResponseDTO = patientService.getPatientById(reservation.getPatientId());
        reservationResponseDTO.setNamaPatient(patientResponseDTO.getName());
        reservationResponseDTO.setEmail(patientResponseDTO.getEmail());
        reservationResponseDTO.setGender(patientResponseDTO.isGender());

        var nurseResponseDTO = nurseService.getNurseById(reservation.getNurse());
        reservationResponseDTO.setNamaNurse(nurseResponseDTO.getName());
        if (reservation.getAppointmentId() != null){
            reservationResponseDTO.setAppointmentId(reservation.getAppointmentId());
        }
        

        if (reservation.getFacilities() != null) {
            var listFacilitiesResponseDTO = new ArrayList<FacilityResponseDTO>();
            reservation.getFacilities().forEach(facility -> {
                var facilityResponseDTO = new FacilityResponseDTO();
                facilityResponseDTO.setId(facility.getId());
                facilityResponseDTO.setName(facility.getName());
                facilityResponseDTO.setFee(facility.getFee());
                facilityResponseDTO.setCreatedDate(facility.getCreatedDate());
                facilityResponseDTO.setUpdatedDate(facility.getUpdatedDate());
                listFacilitiesResponseDTO.add(facilityResponseDTO);
            });
            reservationResponseDTO.setFacilities(listFacilitiesResponseDTO);
        }

        return reservationResponseDTO; 
    }


    @Override
    public ReservationResponseDTO getReservationById(String idReservation) {
        var reservation = reservationDb.findByIdAndIsDeletedFalse(idReservation);
        if (reservation == null) {
            return null;
        }

       return reservationToReservationResponseDTO(reservation);
    }


    @Override
    public String deleteReservation(String idReservation) {
         
        Reservation reservation = reservationDb.findById(idReservation).orElse(null);
        
        if (reservation == null) {
            return "NOT_FOUND";
        }
        
        System.out.println("Reservation Status: " + reservation.getStatus()); 

        if ("ongoing".equalsIgnoreCase(reservation.getStatus())) {
            return "ONGOING";
        }
        
        reservation.setDeletedAt(new Date());
        reservation.setDeleted(true);
        reservationDb.save(reservation);
        
        return "DELETED";
    }

    @Override
    public ReservationResponseDTO updateFacilitiesReservation(String idReservation, UpdateReservationsRequestRestDTO reservationDTO) {
        // Mencari reservasi berdasarkan ID
        var reservation = reservationDb.findById(idReservation).orElse(null);
        if (reservation == null) {
            return null;
        }

        // Update informasi dasar yang tidak terkait dengan fasilitas
        reservation.setId(idReservation);
        reservation.setPatientId(reservation.getPatientId());
        reservation.setNurse(reservation.getNurse());
        reservation.setDateIn(reservation.getDateIn());
        reservation.setDateOut(reservation.getDateOut());
        reservation.setRoomId(reservation.getRoomId());
        reservation.setCreatedDate(reservation.getCreatedDate());
        reservation.setUpdatedDate(new Date());

        // Mengupdate fasilitas jika ada perubahan
        if (reservationDTO.getFacilities() != null) {
            LocalDate dateOut = reservation.getDateOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            if (currentDate.isAfter(dateOut)) {
                throw new IllegalArgumentException("Fasilitas tidak dapat diupdate karena tanggal keluar sudah terlewati.");
            }

            var listFacilitiesFromDTO = reservationDTO.getFacilities().stream()
                    .distinct()
                    .collect(Collectors.toList());

            var listFacilitiesExisting = reservation.getFacilities();

            List<String> notFoundFacilityIds = new ArrayList<>();
            listFacilitiesFromDTO.forEach(idFacility -> {
                var facility = facilityDb.findById(idFacility.getId()).orElse(null);
                if (facility != null) {
                    if (!reservation.getFacilities().contains(facility)) {
                        reservation.getFacilities().add(facility);
                    }
                    if (!facility.getListReservations().contains(reservation)) {
                        facility.getListReservations().add(reservation);
                    }
                } else {
                    notFoundFacilityIds.add(idFacility.getId().toString());
                }
            });

            if (!notFoundFacilityIds.isEmpty()) {
                throw new IllegalArgumentException("Fasilitas dengan ID berikut tidak ditemukan: " + String.join(", ", notFoundFacilityIds));
            }

            if (listFacilitiesExisting != null && !listFacilitiesExisting.isEmpty()) {
                listFacilitiesExisting.removeIf(facility -> 
                    !listFacilitiesFromDTO.stream().anyMatch(f -> f.getId().equals(facility.getId()))
                );
            }
        }

    
        
        long daysStayed = ChronoUnit.DAYS.between(reservation.getDateIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
                                                reservation.getDateOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        double totalFacilityFee = reservation.getFacilities().stream()
            .mapToDouble(facility -> facility.getFee()) 
            .sum();

        double roomFee = reservation.getRoomId().getPricePerDay(); 

     
        double newTotalFee = totalFacilityFee + (roomFee * daysStayed);
        reservation.setTotalFee(newTotalFee);
        var updatedReservation = reservationDb.save(reservation);

        return reservationToReservationResponseDTO(updatedReservation);
    }

    @Override
    public ReservationResponseDTO updateRoomAndDateReservation(String idReservation, UpdateReservationsRequestRestDTO reservationDTO) {
        
        var reservation = reservationDb.findById(idReservation).orElse(null);
        if (reservation == null) {
            return null;
        }

        // Update informasi dasar yang tidak terkait dengan fasilitas
        reservation.setId(idReservation);
        reservation.setPatientId(reservation.getPatientId());
        reservation.setNurse(reservation.getNurse());
        reservation.setCreatedDate(reservation.getCreatedDate());
        reservation.setFacilities(reservation.getFacilities());
        reservation.setCreatedDate(reservation.getCreatedDate());
        reservation.setUpdatedDate(new Date());

        if (reservationDTO.getRoom() != null && reservationDTO.getDateIn() != null && reservationDTO.getDateOut() != null) {
            LocalDate dateIn = reservationDTO.getDateIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
        
            if (!currentDate.isBefore(reservation.getDateIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                throw new IllegalArgumentException("Cannot save reservation. DateIn has already passed.");
            }
            
            boolean isDateChanged = !reservationDTO.getDateIn().equals(reservation.getDateIn()) || !reservationDTO.getDateOut().equals(reservation.getDateOut());
            boolean isRoomChanged = !reservationDTO.getRoom().getId().equals(reservation.getRoomId().getId());
        
            Date today = new Date();
            if (isDateChanged) {
                reservation.setDateIn(reservationDTO.getDateIn());
                reservation.setDateOut(reservationDTO.getDateOut());
        
                if (reservationDTO.getDateOut() != null && reservationDTO.getDateOut().before(today)) {
                    reservation.setStatus("done");
                } else if (dateIn != null && dateIn.isAfter(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                    reservation.setStatus("upcoming");
                } else if (dateIn != null && dateIn.isBefore(today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                        && reservationDTO.getDateOut() != null && reservationDTO.getDateOut().after(today)) {
                    reservation.setStatus("ongoing");
                }
            }
        
            if (isRoomChanged) {
                
                var room = roomDb.findById(reservationDTO.getRoom().getId()).orElse(null);
                if (room != null) {
                    List<Reservation> currentReservations = reservationDb.findByRoomIdAndDateInLessThanEqualAndDateOutGreaterThanEqual(
                        room, reservationDTO.getDateOut(), reservationDTO.getDateIn()
                    );
            
                    
                    int currentReservationCount = currentReservations.size();
            
                    
                    if (currentReservationCount >= room.getMaxCapacity()) {
                        throw new IllegalArgumentException("Room dengan ID " + room.getId() + " sudah penuh.");
                    }
            
                    reservation.setRoomId(room); 
                } else {
                    throw new IllegalArgumentException("Room dengan ID " + reservationDTO.getRoom().getId() + " tidak ditemukan.");
                }
            }
            
        }

        long daysStayed = ChronoUnit.DAYS.between(reservation.getDateIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
                                                reservation.getDateOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        double totalFacilityFee = reservation.getFacilities().stream()
            .mapToDouble(facility -> facility.getFee()) 
            .sum();

        double roomFee = reservation.getRoomId().getPricePerDay(); 

     
        double newTotalFee = totalFacilityFee + (roomFee * daysStayed);
        reservation.setTotalFee(newTotalFee);
        var updatedReservation = reservationDb.save(reservation);

        return reservationToReservationResponseDTO(updatedReservation);
    }

    @Override
    public ReservationResponseDTO addReservation(AddReservationsRequestRestDTO reservationDTO, String token) {
        //pengurangan kapasitas kamar setiap kali add Reservation baru
        Room roomCek = roomService.getRoomById(reservationDTO.getRoom().getId()); 
        roomCapacityService.decreaseRoomCapacity(reservationDTO.getRoom().getId()); 

        // Periksa apakah kapasitas ruangan masih ada
        if (roomService.getAvailableCapacity(roomCek.getId()) > 0) { 
            roomService.decreaseRoomCapacity(roomCek.getId()); 
        } 

        Reservation reservation = new Reservation();
        reservation.setDateIn(reservationDTO.getDateIn());
        reservation.setDateOut(reservationDTO.getDateOut());
        Date today = new Date();
        LocalDate todayLocalDate = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate dateInLocalDate = reservationDTO.getDateIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateOutLocalDate = reservationDTO.getDateOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Update status berdasarkan dateIn dan dateOut
        if (dateOutLocalDate.isBefore(todayLocalDate)) {
            reservation.setStatus("done");
        } else if (dateInLocalDate.isAfter(todayLocalDate)) {
            reservation.setStatus("upcoming");
        } else if ((dateInLocalDate.isEqual(todayLocalDate) || dateInLocalDate.isBefore(todayLocalDate)) 
                && dateOutLocalDate.isAfter(todayLocalDate)) {
            reservation.setStatus("ongoing");
        }


        var patient = patientService.getPatientById(reservationDTO.getPatient());
        if (patient == null) {
            System.out.println("Patient dengan ID " + reservationDTO.getPatient() + " tidak ditemukan.");
            throw new IllegalArgumentException("Patient dengan ID " + reservationDTO.getPatient()+ " tidak ditemukan.");
        } 
        reservation.setPatientId(reservationDTO.getPatient());


        String reservationId = generateReservationId(
            reservationDTO.getDateIn(),
            reservationDTO.getDateOut(),
            patient.getNik()
        );
        reservation.setId(reservationId);


        EndUserResponseDTO currentUser = reservationService.getCurrentUser(token);
        if (!"NURSE".equalsIgnoreCase(currentUser.getRole())) {
            throw new IllegalArgumentException("Hanya Nurse yang memiliki akses untuk membuat Reservation! Role ditemukan: " + currentUser.getRole());
        }        
        UUID userId = currentUser.getId();
        if (userId == null) {
            throw new IllegalArgumentException("User dengan peran Nurse tidak memiliki ID yang valid.");
        }
        reservation.setNurse(userId);

        // Validasi kuota room
        var room = roomDb.findById(reservationDTO.getRoom().getId()).orElse(null);
        if (room != null) {
            // Mendapatkan daftar reservasi aktif yang jatuh dalam rentang `dateIn` dan `dateOut`
            List<Reservation> currentReservations = reservationDb.findByRoomIdAndDateInLessThanEqualAndDateOutGreaterThanEqual(
                room, reservationDTO.getDateOut(), reservationDTO.getDateIn()
            );

            int currentReservationCount = currentReservations.size();

            if (currentReservationCount >= room.getMaxCapacity()) {
                throw new IllegalArgumentException("Room dengan ID " + room.getId() + " sudah penuh.");
            }
        }
        reservation.setRoomId(room); 
        reservation.setFacilities(new ArrayList<>());
        

        // Validasi duplikasi fasilitas
        if (reservationDTO.getFacilities() != null) {
            Set<UUID> facilityIds = new HashSet<>();
            for (Facility facility : reservationDTO.getFacilities()) {
                if (!facilityIds.add(facility.getId())) {
                    throw new IllegalArgumentException("Terdapat ID fasilitas yang duplikat: " + facility.getId());
                }
            }

            List<String> notFoundFacilityIds = new ArrayList<>();

            // Pengecekan apakah ID fasilitas yang diinput ada di sistem
            for (Facility idFacility : reservationDTO.getFacilities()) {
                facilityDb.findById(idFacility.getId()).ifPresentOrElse(
                    facility -> {
                        reservation.getFacilities().add(facility);
                        facility.getListReservations().add(reservation);
                    },
                    () -> notFoundFacilityIds.add(idFacility.getId().toString())
                );
            }

            // Jika terdapat fasilitas yang tidak ditemukan, lemparkan error
            if (!notFoundFacilityIds.isEmpty()) {
                throw new IllegalArgumentException("Fasilitas dengan ID berikut tidak ditemukan: " + String.join(", ", notFoundFacilityIds));
            }

        }

        //validasi apointmentId
        if (reservationDTO.getAppointmentId() != null) {
            List<AppointmentResponseDTO> allAppointments = apointmentService.getAllApointment();
            boolean appointmentExists = allAppointments.stream()
                .anyMatch(appointment -> appointment.getId().equals(reservationDTO.getAppointmentId()));

            if (!appointmentExists) {
                throw new IllegalArgumentException("Appointment dengan ID " + reservationDTO.getAppointmentId() + " tidak ditemukan atau tidak tersedia.");
            }
            reservation.setAppointmentId(reservationDTO.getAppointmentId());
        }
        


        long daysStayed = ChronoUnit.DAYS.between(reservation.getDateIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
                                                reservation.getDateOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        double totalFacilityFee = reservation.getFacilities().stream()
            .mapToDouble(facility -> facility.getFee()) 
            .sum();

        double roomFee = reservation.getRoomId().getPricePerDay(); 

     
        double newTotalFee = totalFacilityFee + (roomFee * daysStayed);
        reservation.setTotalFee(newTotalFee);

        //create bill
        BillResponseDTO billReservation = billServiceImpl.createBill(reservation.getId(), reservation.getTotalFee(), reservation.getPatientId(), token);

        var newReservation = reservationDb.save(reservation);
        return reservationToReservationResponseDTO(newReservation);
    }


    private String generateReservationId(Date dateIn, Date dateOut, String nik) {
        // 1. Karakter pertama adalah "RES"
        String prefix = "RES";
        
        // 2. Menghitung selisih tanggal (dateOut - dateIn) dan mengambil 2 digit terakhir
        long diffInMillies = dateOut.getTime() - dateIn.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        String dayDiff = String.format("%02d", (int) (diffInDays % 100)); // Mengambil 2 digit terakhir

        // 3. Mendapatkan hari pembuatan reservasi dalam format 3 karakter (SUN, MON, dst.)
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.ENGLISH);
        String dayOfWeek = dayFormat.format(dateIn).toUpperCase().substring(0, 3);

        // 4. Mendapatkan 4 karakter terakhir dari NIK pasien
        String nikSuffix = nik.length() >= 4 ? nik.substring(nik.length() - 4) : nik;
        
        // 5. Mendapatkan jumlah reservasi yang ada di sistem (termasuk soft delete)
        int reservationCount = reservationService.getTotalReservationCount(); 
        
        // 6. Memastikan 4 digit terakhir adalah jumlah reservasi yang ada
        String reservationCountStr = String.format("%04d", reservationCount);

        // 7. Menggabungkan semua komponen ID menjadi ID 16 karakter
        String reservationId = String.format("%s%s%s%s%s", prefix, dayDiff, dayOfWeek, nikSuffix, reservationCountStr);

        // 8. Memastikan ID bersifat unik
        while (reservationService.isReservationIdExists(reservationId)) {
            reservationCount++;
            reservationCountStr = String.format("%04d", reservationCount);
            reservationId = String.format("%s%s%s%s%s", prefix, dayDiff, dayOfWeek, nikSuffix, reservationCountStr);
        }

        return reservationId;
    }

}
