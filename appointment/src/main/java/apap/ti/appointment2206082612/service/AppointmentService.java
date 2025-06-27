package apap.ti.appointment2206082612.service;

import apap.ti.appointment2206082612.dto.request.*;
import apap.ti.appointment2206082612.dto.response.AppointmentResponseDTO;
import apap.ti.appointment2206082612.model.Appointment;
import apap.ti.appointment2206082612.restdto.response.AppointmentResponseRestDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AppointmentService {
    // AppointmentResponseDTO addAppointment(CreateAppointmentRequestDTO appointmentDTO, Doctor doctor) throws Exception;
    AppointmentResponseDTO getAppointmentByIdRest(String id) throws Exception;
    List<Appointment> getAllAppointments();
    // Appointment updateAppointment(UpdateAppointmentRequestDTO appointmentDTO) throws Exception;
    List<AppointmentResponseDTO> getAppointmentsByDateRange(Date from, Date to) throws Exception;
    List<Date> getDoctorSchedules(String doctorId) throws Exception;
    void updateAppointmentStatus(String appointmentId, Integer status) throws Exception;
    boolean isAppointmentExistForDoctor(String doctorId, Date date) throws Exception;
    List<Appointment> getAppointmentsByDoctorId(String doctorId) throws Exception;
    List<AppointmentResponseRestDTO> getAllAppointmentFromRest() throws Exception;
    Appointment getAppointmentById(String id);
    void deleteAppointment(String id);
}
