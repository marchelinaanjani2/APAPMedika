package apap.ti.appointment2206082612.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import apap.ti.appointment2206082612.dto.response.AppointmentResponseDTO;
import apap.ti.appointment2206082612.dto.response.BaseResponseDTO;
import apap.ti.appointment2206082612.model.Appointment;
import apap.ti.appointment2206082612.repository.AppointmentDb;
import apap.ti.appointment2206082612.restdto.response.AppointmentResponseRestDTO;
import jakarta.transaction.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDb appointmentDb;

    private final WebClient webClient;

    public AppointmentServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8082/api") // Adjust the base URL as needed
                .build();
    }

    @Override
    public boolean isAppointmentExistForDoctor(String doctorId, Date date) throws Exception {
        // Gunakan appointmentDb untuk pengecekan janji temu
        return appointmentDb.existsByDoctorIdAndDate(doctorId, date);
    }

    // Fix later
    // @Override
    // public AppointmentResponseDTO addAppointment(CreateAppointmentRequestDTO appointmentDTO, Doctor doctor) throws Exception {
    //     // Step 1: Cek apakah janji temu sudah ada untuk dokter dan tanggal yang sama
    //     boolean exists = appointmentDb.existsByDoctorIdAndDate(appointmentDTO.getDoctorId(), appointmentDTO.getDate());
    
    //     if (exists) {
    //         throw new Exception("Appointment already exists for the selected doctor and date");
    //     }
    
    //     if (doctor == null) {
    //         throw new Exception("Doctor not found with ID: " + appointmentDTO.getDoctorId());
    //     }
    
    //     // Step 3: Ambil pasien yang baru dibuat berdasarkan patientId dari DTO
    //     Patient patient = patientService.getPatientById(appointmentDTO.getPatientId());
    
    //     if (patient == null) {
    //         throw new Exception("Patient not found with ID: " + appointmentDTO.getPatientId());
    //     }
    
    //     // Step 4: Inisialisasi janji temu baru (Appointment)
    //     Appointment newAppointment = new Appointment();
        
    //     // Step 5: Generate ID unik untuk appointment menggunakan metode yang sesuai
    //     String appointmentCode = generateAppointmentCode(doctor, appointmentDTO.getDate());
    //     newAppointment.setId(appointmentCode);
    
    //     // Step 6: Set dokter dan pasien pada janji temu
    //     // Fix later
    //     // newAppointment.setDoctor(doctor);
    //     // newAppointment.setPatient(patient);
    
    //     // Step 7: Set atribut lain pada appointment:
    //     newAppointment.setStatus(0); // Status 0: Created
    //     newAppointment.setDiagnosis(""); // Diagnosis dikosongkan
    //     newAppointment.setTreatments(new ArrayList<Treatment>()); // List Treatment kosong
    
    //     // Set tanggal appointment
    //     newAppointment.setDate(appointmentDTO.getDate());
    
    //     // Set totalFee diambil dari fee dokter
    //     newAppointment.setTotalFee(doctor.getFee());
    
    //     // Step 8: Tambahkan janji temu baru ke daftar janji temu pada dokter
    //     if (doctor.getAppointments() == null) {
    //         doctor.setAppointments(new ArrayList<>());
    //     }
    //     List<Appointment> appointments = new ArrayList<Appointment>();
    //     appointments = doctor.getAppointments();
    //     appointments.add(newAppointment);
    //     doctor.setAppointments(appointments);
    //     doctorDb.save(doctor);
    
    //     // Step 9: Simpan janji temu ke database
    //     appointmentDb.save(newAppointment);
    
    //     // Step 10: Buat response DTO untuk mengembalikan hasil
    //     AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
    //     responseDTO.setId(newAppointment.getId());
    //     // responseDTO.setDoctorName(doctor.getName());
    //     // responseDTO.setPatientName(patient.getName());
    //     responseDTO.setDate(newAppointment.getDate());
    //     responseDTO.setTotalFee(newAppointment.getTotalFee());
    //     responseDTO.setStatus(newAppointment.getStatus());
    //     if (newAppointment.getDiagnosis() != null) {
    //         responseDTO.setDiagnosis(newAppointment.getDiagnosis());
    //     }
    
    //     return responseDTO;
    // }
    
    // private String generateAppointmentCode(Doctor doctor, Date appointmentDate) throws Exception {
    //     // Karakter [1,3] -> Spesialisasi dokter
    //     String specializationCode = doctor.getSpecializationCode();
    
    //     // Karakter [4,7] -> Tanggal appointment dalam format DDMM
    //     SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMM");
    //     String datePart = dateFormatter.format(appointmentDate);
    
    //     // Karakter [8,10] -> Urutan janji dalam hari yang sama
    //     // Cari appointment berdasarkan tanggal untuk menghitung urutan janji dalam hari tersebut
    //     List<Appointment> appointmentsOnSameDay = appointmentDb.findByDate(appointmentDate);
    //     int sequenceNumber = appointmentsOnSameDay.size() + 1; // Urutan appointment berikutnya
    //     String sequencePart = String.format("%03d", sequenceNumber); // Format menjadi 3 digit (001, 002, dst.)
    
    //     // Gabungkan semua bagian menjadi 10 karakter
    //     return specializationCode + datePart + sequencePart;
    // }

    // Fix later
    

    @Override
    public AppointmentResponseDTO getAppointmentByIdRest(String id) throws Exception {
        var response = webClient
                .get()
                .uri("/appointment?id=" + id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<AppointmentResponseDTO>>() {})
                .block();

        if (response == null) {
            throw new Exception("Failed to consume API getAppointmentById");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    @Override
    public Appointment getAppointmentById(String id) {
        return appointmentDb.findById(id).orElse(null);
    }
    @Override
    public List<Appointment> getAllAppointments(){
        return appointmentDb.findAll();
    }

    @Override
    public List<AppointmentResponseRestDTO> getAllAppointmentFromRest() throws Exception {
        var response = webClient
            .get()
            .uri("/appointment/all")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<AppointmentResponseRestDTO>>>() {})
            .block();

        if (response == null) {
            throw new Exception("Failed to consume API getAllAppointment");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    // Fix later
    // @Override
    // @Transactional
    // public Appointment updateAppointment(UpdateAppointmentRequestDTO appointmentDTO) throws Exception {
    //     // Cari appointment berdasarkan ID
    //     Appointment existingAppointment = appointmentDb.findById(appointmentDTO.getId()).orElse(null);
    //     if (existingAppointment == null) {
    //         throw new Exception("Appointment tidak ditemukan.");
    //     }
    
    //     // Cek apakah appointment bisa diubah (tidak kurang dari satu hari)
    //     LocalDate appointmentDate = existingAppointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    //     if (LocalDate.now().plusDays(1).isAfter(appointmentDate)) {
    //         throw new Exception("Appointment tidak bisa diubah kurang dari satu hari sebelum jadwal.");
    //     }
    
    //     // Cari dokter baru berdasarkan ID dari DTO
    //     Doctor doctor = doctorDb.findById(appointmentDTO.getDoctorId()).orElse(null);
    //     if (doctor == null) {
    //         throw new Exception("Dokter tidak ditemukan.");
    //     }
    
    //     // Update dokter dan tanggal pada appointment
    //     // Fix later
    //     // existingAppointment.setDoctor(doctor);
    //     existingAppointment.setDate(appointmentDTO.getDate());
    
    //     // Update totalFee sesuai dengan fee dari dokter baru
    //     existingAppointment.setTotalFee(doctor.getFee());
    
    //     // Kosongkan list treatment dan diagnosis
    //     existingAppointment.setTreatments(new ArrayList<>()); // Kosongkan treatments
    //     existingAppointment.setDiagnosis(""); // Kosongkan diagnosis
    
    //     // Simpan perubahan appointment
    //     return appointmentDb.save(existingAppointment);
    // }

    // Fix later

    // // Method untuk update diagnosis dan treatment
    // public void updateDiagnosisAndTreatment(AppointmentNoteRequestDTO noteRequest) {
    //     Appointment appointment = appointmentRepository.findById(noteRequest.getId())
    //             .orElseThrow(() -> new RuntimeException("Appointment not found"));

    //     // Update diagnosis dan treatment
    //     appointment.setDiagnosis(noteRequest.getDiagnosis());
        
    //     // Set treatment yang baru, perlu memastikan bahwa data treatment valid.
    //     List<Treatment> updatedTreatments = new ArrayList<>();
    //     for (TreatmentDTO treatmentDTO : noteRequest.getTreatments()) {
    //         Treatment treatment = new Treatment();
    //         treatment.setName(treatmentDTO.getName());
    //         updatedTreatments.add(treatment);
    //     }
    //     appointment.setTreatments(updatedTreatments);

    //     // Simpan perubahan ke database
    //     appointmentRepository.save(appointment);
    // }

    @Override
    @Transactional
    public void deleteAppointment(String id) {
        Appointment existingAppointment = appointmentDb.findById(id).orElse(null);
        if (existingAppointment != null) {
            existingAppointment.setDeletedAt(new Date()); // Soft delete
            appointmentDb.save(existingAppointment);
        }
    }

    @Override
    public void updateAppointmentStatus(String appointmentId, Integer status) throws Exception {
        // Temukan appointment berdasarkan ID
        Appointment appointment = appointmentDb.findById(appointmentId)
                .orElseThrow(() -> new Exception("Appointment not found"));
    
        // Update status appointment (0 = Created, 1 = Done, 2 = Cancelled)
        appointment.setStatus(status);
    
        // Simpan perubahan ke database
        appointmentDb.save(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDateRange(Date from, Date to) throws Exception {
        var response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/appointment/date-range")
                        .queryParam("from", from.getTime())
                        .queryParam("to", to.getTime())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<AppointmentResponseDTO>>>() {})
                .block();

        if (response == null) {
            throw new Exception("Failed to consume API getAppointmentsByDateRange");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    @Override
    public List<Date> getDoctorSchedules(String doctorId) throws Exception {
        var response = webClient
                .get()
                .uri("/doctor/schedules?doctorId=" + doctorId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<Date>>>() {})
                .block();

        if (response == null) {
            throw new Exception("Failed to consume API getDoctorSchedules");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(String doctorId) throws Exception {
        return appointmentDb.findByDoctorId(doctorId);
    }
}
