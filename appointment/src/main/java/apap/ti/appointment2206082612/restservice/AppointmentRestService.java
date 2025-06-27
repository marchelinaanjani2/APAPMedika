package apap.ti.appointment2206082612.restservice;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import apap.ti.appointment2206082612.restdto.request.AppointmentStatusUpdateRequestRestDTO;
import apap.ti.appointment2206082612.restdto.request.CreateAppointmentRequestRestDTO;
import apap.ti.appointment2206082612.restdto.request.UpdateAppointmentRequestRestDTO;
import apap.ti.appointment2206082612.restdto.response.AppointmentResponseRestDTO;

public interface AppointmentRestService {
    AppointmentResponseRestDTO addAppointment(CreateAppointmentRequestRestDTO appointmentDTO, String token);
    AppointmentResponseRestDTO getAppointmentById(String id, String token);
    List<AppointmentResponseRestDTO> getAllAppointments(String token);
    AppointmentResponseRestDTO updateAppointment(UpdateAppointmentRequestRestDTO appointmentDTO, String token);
    boolean isAppointmentExistForDoctor(String doctorId, Date date) throws Exception;
    List<AppointmentResponseRestDTO> getAppointmentsByDoctorId(String doctorId, String token);
    List<AppointmentResponseRestDTO> getAppointmentsByPatientId(UUID patientId, String token);
    List<AppointmentResponseRestDTO> getAppointmentsByDateRange(LocalDate fromDate, LocalDate toDate, String token);
    void softDeleteAppointment(String idAppointment, String token);
    void updateAppointmentStatus(String idAppointment, int status, String token);
}
