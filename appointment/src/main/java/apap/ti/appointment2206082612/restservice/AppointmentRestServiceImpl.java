package apap.ti.appointment2206082612.restservice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apap.ti.appointment2206082612.exception.DoctorAlreadyBookedException;
import apap.ti.appointment2206082612.exception.UserNotFound;
import apap.ti.appointment2206082612.exception.UserUnauthorized;
import apap.ti.appointment2206082612.exception.DataNotFound;
import apap.ti.appointment2206082612.model.Appointment;
import apap.ti.appointment2206082612.model.Treatment;
import apap.ti.appointment2206082612.repository.AppointmentDb;
import apap.ti.appointment2206082612.repository.TreatmentDb;
import apap.ti.appointment2206082612.restdto.request.AppointmentStatusUpdateRequestRestDTO;
import apap.ti.appointment2206082612.restdto.request.CreateAppointmentRequestRestDTO;
import apap.ti.appointment2206082612.restdto.request.UpdateAppointmentRequestRestDTO;
import apap.ti.appointment2206082612.restdto.response.AppointmentResponseRestDTO;
import apap.ti.appointment2206082612.restdto.response.DoctorResponseDTO;
import apap.ti.appointment2206082612.restdto.response.EndUserResponseDTO;
import apap.ti.appointment2206082612.restdto.response.PatientResponseDTO;
import apap.ti.appointment2206082612.restdto.response.TreatmentResponseRestDTO;
import apap.ti.appointment2206082612.service.AppointmentService;
import apap.ti.appointment2206082612.service.ProfileService;
import apap.ti.appointment2206082612.service.BillService;

@Service
@Transactional
public class AppointmentRestServiceImpl implements AppointmentRestService {

    @Autowired
    private AppointmentDb appointmentDb;

    @Autowired
    private TreatmentDb treatmentDb;

    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BillService billService;

    // PBI-BE-A6
    @Override
    public AppointmentResponseRestDTO addAppointment(CreateAppointmentRequestRestDTO appointmentDTO, String token) {
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token); 
        if (currentUser == null) {
            throw new UserNotFound("You Must Login First");
        }

        String role = currentUser.getRole();
        UUID userId = currentUser.getId();
        // Mapping DTO to Entity
        Appointment appointment = new Appointment();

        UUID idPatient;
        if ("ADMIN".equalsIgnoreCase(role)) {
            PatientResponseDTO patient = profileService.getPatientById(UUID.fromString(appointmentDTO.getPatientId()));
            idPatient = patient.getId();
        }
        else if ("PATIENT".equalsIgnoreCase(role)) {
            idPatient = userId;
        }
        else {
            throw new UserUnauthorized("You are not authorized to create an appointment");
        }

        if (idPatient == null) {
            throw new UserNotFound("Patient not found in the database");
        }

        // Fetch doctor and patient from DB
        DoctorResponseDTO doctor = profileService.getDoctorById(UUID.fromString(appointmentDTO.getDoctorId()));

        // Check are the assigned date is in doctor's schedule
        // Get the appointment date and convert it to LocalDate
        LocalDate appointmentDate = LocalDate.parse(appointmentDTO.getDate().toString(), DateTimeFormatter.ISO_DATE);
        int appointmentDayOfWeek = appointmentDate.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        List<Integer> schedule = doctor.getSchedules();
        if (!schedule.contains(appointmentDayOfWeek)) {
            throw new UserUnauthorized("Doctor is not available on this day");
        }
        
        if (doctor == null || idPatient == null) {
            // Handle error if doctor or patient not found
            throw new UserNotFound("Doctor or Patient not found in the database");
        }

        // Check if doctor already has an appointment on the same date
        boolean isDoctorAlreadyBooked = appointmentDb.existsByDoctorIdAndDate(doctor.getId().toString(), appointmentDTO.getDate());
        if (isDoctorAlreadyBooked) {
            // Throw an exception if doctor already has an appointment
            throw new DoctorAlreadyBookedException("Doctor already has an appointment on this date");
        }

        // Set fields
        appointment.setDoctorId(doctor.getId().toString());
        appointment.setPatientId(idPatient);
        appointment.setDate(appointmentDTO.getDate());
        appointment.setStatus(0); // Created
        appointment.setCreatedAt(new Date());
        appointment.setUpdatedAt(new Date());
        appointment.setTotalFee(doctor.getFee());
        appointment.setDiagnosis("");
        appointment.setTreatments(new ArrayList<>());

        // Generate unique ID
        String uniqueId = generateAppointmentId(doctor, appointment.getDate());
        appointment.setId(uniqueId);

        // Save the appointment
        Appointment savedAppointment = appointmentDb.save(appointment);

        // Create bill
        billService.addBill(savedAppointment.getId(), token, idPatient);

        // Return the mapped response DTO
        return mapAppointmentToResponseDTO(savedAppointment);
    }

    // PBI-BE-A2
    @Override
    public AppointmentResponseRestDTO getAppointmentById(String id, String token) {
        // Verifikasi token dan dapatkan informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You Must Login First");
        }
    
        // Periksa apakah pengguna memiliki peran yang sesuai
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"DOCTOR".equalsIgnoreCase(role) && 
            !"NURSE".equalsIgnoreCase(role) && !"PATIENT".equalsIgnoreCase(role)) {
            throw new UserUnauthorized("You are not authorized to view this appointment");
        }
    
        Appointment appointment = null;
        Appointment checkAppointment = appointmentDb.findById(id).orElse(null);
        if (checkAppointment == null) {
            throw new DataNotFound("Data Appointment tidak ada di database"); // Appointment tidak ditemukan
        }
        // Jika pengguna adalah ADMIN atau NURSE, mereka dapat melihat semua janji
        if ("ADMIN".equalsIgnoreCase(role) || "NURSE".equalsIgnoreCase(role)) {
            appointment = appointmentDb.findById(id).orElse(null);
        }
    
        // Jika pengguna adalah DOCTOR, hanya dapat melihat janji yang ter-assign untuk mereka
        if ("DOCTOR".equalsIgnoreCase(role)) {
            List<Appointment> doctorAppointments = appointmentDb.findByDoctorId(currentUser.getId().toString());
            appointment = doctorAppointments.stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    
        // Jika pengguna adalah PATIENT, hanya dapat melihat janji yang ter-assign untuk mereka
        if ("PATIENT".equalsIgnoreCase(role)) {
            List<Appointment> patientAppointments = appointmentDb.findByPatientId(currentUser.getId());
            appointment = patientAppointments.stream()
                    .filter(a -> a.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    
        // Jika appointment ditemukan, kembalikan data dalam bentuk DTO
        if (appointment != null) {
            return mapAppointmentToResponseDTO(appointment);
        } else {
            throw new UserUnauthorized("You are not authorized to view this appointment"); // Appointment tidak ditemukan
        }
    }

    // PBI-BE-A1
    @Override
    public List<AppointmentResponseRestDTO> getAllAppointments(String token) {
        // Verifikasi token dan dapatkan informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You Must Login First");
        }
    
        // Periksa apakah pengguna memiliki peran ADMIN atau NURSE
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"NURSE".equalsIgnoreCase(role)) {
            throw new UserUnauthorized("You are not authorized to view appointments");
        }
    
        // Mengambil semua janji dari database
        List<Appointment> appointments = appointmentDb.findAll();
        List<AppointmentResponseRestDTO> responseDTOs = new ArrayList<>();
        appointments.forEach(appointment -> responseDTOs.add(mapAppointmentToResponseDTO(appointment)));
        return responseDTOs;
    }

    // PBI-BE-A8
    @Override
    public AppointmentResponseRestDTO updateAppointment(UpdateAppointmentRequestRestDTO appointmentDTO, String token) {
        // Verifikasi token dan ambil informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You must login first.");
        }
    
        // Cek apakah role pengguna adalah DOCTOR
        if (!"DOCTOR".equalsIgnoreCase(currentUser.getRole())) {
            throw new UserUnauthorized("Only doctors are allowed to update appointment's diagnosis and treatments.");
        }
    
        // Cari appointment berdasarkan ID
        Appointment existingAppointment = appointmentDb.findById(appointmentDTO.getId()).orElse(null);
        if (existingAppointment == null) {
            throw new DataNotFound("Appointment not found.");
        }
    
        // Cek apakah doctorId dari appointment sesuai dengan currentUser (id dokter yang sedang login)
        if (!existingAppointment.getDoctorId().equals(currentUser.getId().toString())) {
            throw new UserUnauthorized("You are not authorized to update this appointment.");
        }
    
        // Update diagnosis
        existingAppointment.setDiagnosis(appointmentDTO.getDiagnosis());
    

        // Fetch doctor and patient from DB
        DoctorResponseDTO doctor = profileService.getDoctorById(UUID.fromString(existingAppointment.getDoctorId()));
    
        // Update diagnosis
        existingAppointment.setDiagnosis(appointmentDTO.getDiagnosis());
    
        long totalFee = doctor.getFee();
    
        // Update treatments jika ada
        if (appointmentDTO.getTreatmentIds() != null) {
            List<Treatment> newTreatments = new ArrayList<>();
            List<Treatment> treatments = treatmentDb.findAllById(appointmentDTO.getTreatmentIds());
            for (Treatment treatment : treatments) {
                if (!newTreatments.contains(treatment)) {
                    totalFee += treatment.getPrice();
                    newTreatments.add(treatment);
                }
            }
            existingAppointment.setTreatments(newTreatments);
    
            // Update total fee
            existingAppointment.setTotalFee(totalFee);
        }
    
        // Set updated date
        existingAppointment.setUpdatedAt(new Date());
    
        // Simpan perubahan ke database
        Appointment updatedAppointment = appointmentDb.save(existingAppointment);
    
        // Map Appointment ke DTO untuk response
        return mapAppointmentToResponseDTO(updatedAppointment);
    }

    private AppointmentResponseRestDTO mapAppointmentToResponseDTO(Appointment appointment) {
        AppointmentResponseRestDTO responseDTO = new AppointmentResponseRestDTO();
        responseDTO.setId(appointment.getId());
        responseDTO.setDoctorId(appointment.getDoctorId());
        DoctorResponseDTO doctor = profileService.getDoctorById(UUID.fromString(appointment.getDoctorId()));
        PatientResponseDTO patient = profileService.getPatientById(appointment.getPatientId());
        responseDTO.setDoctorName(doctor.getName());
        responseDTO.setPatientId(appointment.getPatientId().toString());
        responseDTO.setPatientName(patient.getName());
        responseDTO.setDate(appointment.getDate());
        responseDTO.setDiagnosis(appointment.getDiagnosis());
        responseDTO.setTotalFee(appointment.getTotalFee());
        responseDTO.setStatus(appointment.getStatus());
        responseDTO.setCreatedAt(appointment.getCreatedAt());
        responseDTO.setUpdatedAt(appointment.getUpdatedAt());

        // Map treatments
        if (appointment.getTreatments() != null) {
            List<TreatmentResponseRestDTO> treatments = new ArrayList<>();
            appointment.getTreatments().forEach(treatment -> {
                TreatmentResponseRestDTO treatmentDTO = new TreatmentResponseRestDTO();
                treatmentDTO.setId(treatment.getId());
                treatmentDTO.setName(treatment.getName());
                treatmentDTO.setPrice(treatment.getPrice());
                treatments.add(treatmentDTO);
            });
            responseDTO.setTreatments(treatments);
        }

        return responseDTO;
    }

    /**
     * Generate appointment ID based on specifications.
     * Format: [SpecializationCode][DDMM][Sequence]
     */
    private String generateAppointmentId(DoctorResponseDTO doctor, Date date) {
        String specializationCode = getSpecializationCode(doctor.getSpecialist());
        String dateStr = new java.text.SimpleDateFormat("ddMM").format(date);

        // Get sequence number for the day
        List<Appointment> appointmentsOnDate = appointmentDb.findByDate(date);
        int sequence = appointmentsOnDate.size() + 1;

        return String.format("%s%s%03d", specializationCode, dateStr, sequence);
    }

    private String getSpecializationCode(Integer specialist) {
        return switch (specialist) {
            case 0 -> "UMM";
            case 1 -> "GGI";
            case 2 -> "ANK";
            case 3 -> "BDH";
            case 4 -> "PRE";
            case 5 -> "JPD";
            case 6 -> "KKL";
            case 7 -> "MTA";
            case 8 -> "OBG";
            case 9 -> "PDL";
            case 10 -> "PRU";
            case 11 -> "THT";
            case 12 -> "RAD";
            case 13 -> "KSJ";
            case 14 -> "ANS";
            case 15 -> "NRO";
            case 16 -> "URO";
            default -> "UNK";
        };
    }

    @Override
    public boolean isAppointmentExistForDoctor(String doctorId, Date date) throws Exception {
        // Call the service layer to check if the appointment exists
        return appointmentService.isAppointmentExistForDoctor(doctorId, date);
    }

    // PBI-BE-A3
    @Override
    public List<AppointmentResponseRestDTO> getAppointmentsByDoctorId(String doctorId, String token) {
        // Verifikasi token dan dapatkan informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You Must Login First");
        }
    
        // Periksa apakah pengguna memiliki peran yang sesuai
        String role = currentUser.getRole();
        if (!"DOCTOR".equalsIgnoreCase(role)) {
            throw new UserUnauthorized("You are not authorized to view appointments for this doctor");
        }
    
        // Periksa apakah doctorId sesuai dengan ID user yang sedang login
        if (!doctorId.equals(currentUser.getId().toString())) {
            throw new UserUnauthorized("You are not authorized to view appointments for this doctor");
        }
    
        // Ambil daftar appointment untuk doctorId tertentu
        List<Appointment> appointments = appointmentDb.findByDoctorId(doctorId);
        return appointments.stream()
                .map(this::mapAppointmentToResponseDTO)
                .collect(Collectors.toList());
    }
    
    // PBI-BE-A4
    @Override
    public List<AppointmentResponseRestDTO> getAppointmentsByPatientId(UUID patientId, String token) {
        // Verifikasi token dan dapatkan informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You Must Login First");
        }
    
        // Periksa apakah pengguna memiliki peran yang sesuai
        String role = currentUser.getRole();
        if (!"PATIENT".equalsIgnoreCase(role)) {
            throw new UserUnauthorized("You are not authorized to view appointments for this patient");
        }
    
        // Pastikan ID pasien yang sedang login sesuai dengan patientId yang diberikan
        if (!patientId.equals(currentUser.getId())) {
            throw new UserUnauthorized("You are not authorized to view this patient's appointments");
        }
    
        // Ambil daftar appointment untuk patientId tertentu
        List<Appointment> appointments = appointmentDb.findByPatientId(patientId);
        return appointments.stream()
                .map(this::mapAppointmentToResponseDTO)
                .collect(Collectors.toList());
    }
    
    // PBI-BE-A5
    @Override
    public List<AppointmentResponseRestDTO> getAppointmentsByDateRange(LocalDate fromDate, LocalDate toDate, String token) {
        // Verifikasi token dan dapatkan informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You Must Login First");
        }
    
        // Periksa apakah pengguna memiliki peran yang sesuai
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"DOCTOR".equalsIgnoreCase(role) && !"NURSE".equalsIgnoreCase(role)) {
            throw new UserUnauthorized("You are not authorized to view appointments");
        }
    
        // Konversi LocalDate ke Date
        Date from = java.sql.Date.valueOf(fromDate);
        Date to = java.sql.Date.valueOf(toDate);
    
        // Ambil daftar janji temu dalam rentang tanggal
        List<Appointment> appointments = appointmentDb.findByDateBetween(from, to);
        return appointments.stream()
                .map(this::mapAppointmentToResponseDTO)
                .collect(Collectors.toList());
    }
    
    // PBI-BE-A9
    @Override
    public void softDeleteAppointment(String idAppointment, String token) {
        // Verifikasi token dan ambil informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You must login first.");
        }
    
        // Cek apakah role pengguna adalah ADMIN
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new UserUnauthorized("Only admins are allowed to delete appointments.");
        }
    
        // Cari appointment berdasarkan ID
        Appointment existingAppointment = appointmentDb.findById(idAppointment).orElse(null);
        if (existingAppointment == null) {
            throw new DataNotFound("Appointment not found.");
        }
    
        // Soft delete: Set deleted_at dengan timestamp saat ini
        existingAppointment.setDeletedAt(new Date());
    
        // Simpan perubahan ke database
        appointmentDb.save(existingAppointment);
    }
    
    // PBI-BE-A7
    @Override
    public void updateAppointmentStatus(String idAppointment, int status, String token) {
        // Verifikasi token dan ambil informasi pengguna
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        if (currentUser == null) {
            throw new UserNotFound("You must login first.");
        }
    
        String role = currentUser.getRole();
    
        // Handle role selain PATIENT dan ADMIN
        if (!"PATIENT".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            throw new UserUnauthorized("You are not authorized to update appointment status");
        }
    
        // Cari appointment berdasarkan ID
        Appointment appointment = appointmentDb.findById(idAppointment).orElse(null);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }
    
        // Jika role adalah PATIENT, cek apakah appointment milik user yang sedang login
        if ("PATIENT".equalsIgnoreCase(role)) {
            if (!appointment.getPatientId().equals(currentUser.getId())) {
                throw new UserUnauthorized("You are not authorized to update this appointment");
            }
        }
    
        // Jika role adalah ADMIN, tidak ada batasan, biarkan dia memperbarui status appointment apapun
        if ("ADMIN".equalsIgnoreCase(role)) {
            // Admin bisa memperbarui appointment apapun, tidak perlu pengecekan lebih lanjut
        }
    
        // Update status appointment
        appointment.setStatus(status);
        appointment.setUpdatedAt(new Date());
        appointmentDb.save(appointment);
    }
    
}