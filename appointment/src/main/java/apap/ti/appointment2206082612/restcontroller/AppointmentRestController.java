package apap.ti.appointment2206082612.restcontroller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.appointment2206082612.exception.DoctorAlreadyBookedException;
import apap.ti.appointment2206082612.exception.UserNotFound;
import apap.ti.appointment2206082612.exception.UserUnauthorized;
import apap.ti.appointment2206082612.exception.DataNotFound;
import apap.ti.appointment2206082612.restdto.request.AppointmentStatusUpdateRequestRestDTO;
import apap.ti.appointment2206082612.restdto.request.CreateAppointmentRequestRestDTO;
import apap.ti.appointment2206082612.restdto.request.UpdateAppointmentRequestRestDTO;
import apap.ti.appointment2206082612.restdto.response.AppointmentResponseRestDTO;
import apap.ti.appointment2206082612.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206082612.restdto.response.EndUserResponseDTO;
import apap.ti.appointment2206082612.restservice.AppointmentRestService;
import apap.ti.appointment2206082612.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentRestController {

    @Autowired
    private AppointmentRestService appointmentRestService;

    @Autowired
    private AppointmentService appointmentService;

    // PBI-BE-A6
    // Create appointment di Web Untuk ADMIN saja
    @PostMapping("/add")
    public ResponseEntity<BaseResponseDTO<AppointmentResponseRestDTO>> addAppointment(
            @RequestBody CreateAppointmentRequestRestDTO appointmentDTO, @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseRestDTO>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        try {
            AppointmentResponseRestDTO appointment = appointmentRestService.addAppointment(appointmentDTO, token);
            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setMessage("Created");
            baseResponseDTO.setData(appointment);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
        } catch (DoctorAlreadyBookedException e) {
            // Handle the custom exception thrown when doctor is already booked
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        } catch (UserNotFound e) {
            // Handle UserNotFound exception
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            // Handle UserUnauthorized exception
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Handle general exceptions
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
    
    // PBI-BE-A2
    // READ DETAIL APPOINTMENT pada WEB diakses oleh Admin, Doctor, Nurse.
    @GetMapping("/id/{id}")
    public ResponseEntity<BaseResponseDTO<AppointmentResponseRestDTO>> getAppointmentById(
            @PathVariable String id, @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseRestDTO>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    
        try {
            // Mengirim token ke service untuk diproses
            AppointmentResponseRestDTO appointment = appointmentRestService.getAppointmentById(id, token);
            if (appointment == null) {
                throw new Exception("Appointment not found");
            }
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("OK");
            baseResponseDTO.setData(appointment);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    
        } catch (UserNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (DataNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    // PBI-BE-A1
    // API READ ALL untuk NURSE dan ADMIN di FE Vue
    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseRestDTO>>> getAllAppointments(
            @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseRestDTO>>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
    
        try {
            // Mengirim token ke service
            List<AppointmentResponseRestDTO> appointments = appointmentRestService.getAllAppointments(token);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("OK");
            baseResponseDTO.setData(appointments);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    
        } catch (UserNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
    
    // PBI-BE-A8
    // KONTEKS WEB HANYA BISA DILAKUKAN OLEH DOCTOR
    // Aksi ini dilakukan pada halaman detail appointment
    // Update hanya dapat dilakukan jika status appointment adalah "Created" dan waktu sekarang lebih dari sama dengan tanggal appointment. 
    // Terdapat form untuk mengubah diagnosis & treatments dari appointment tertentu. Kedua field bersifat mandatory.
    // Terdapat pesan konfirmasi sebelum appointment diubah
    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO<AppointmentResponseRestDTO>> updateAppointment(
            @RequestBody UpdateAppointmentRequestRestDTO appointmentDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseRestDTO>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        
        try {
            AppointmentResponseRestDTO updatedAppointment = appointmentRestService.updateAppointment(appointmentDTO, token);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("OK");
            baseResponseDTO.setData(updatedAppointment);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (DataNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    // PBI-BE-A9
    // Konteks Web HANYA Diakses oleh ADMIN
    // Aksi ini dilakukan pada halaman detail appointment
    // Terdapat pesan konfirmasi sebelum appointment dihapus
    @DeleteMapping("/delete/{idAppointment}")
    public ResponseEntity<BaseResponseDTO<Void>> softDeleteAppointment(@PathVariable String idAppointment, @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<Void>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        
        try {
            // Panggil service untuk melakukan soft delete
            appointmentRestService.softDeleteAppointment(idAppointment, token);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Appointment successfully soft deleted");
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (DataNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/exist")
    public ResponseEntity<BaseResponseDTO<Boolean>> isAppointmentExistForDoctor(
            @RequestParam("doctorId") String doctorId,
            @RequestParam("date") Date date) {
        var baseResponseDTO = new BaseResponseDTO<Boolean>();
    
        try {
            Boolean exists = appointmentRestService.isAppointmentExistForDoctor(doctorId, date);
            if (exists == null) {
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage("Appointment data not found");
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }
    
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Appointment check success");
            baseResponseDTO.setData(exists);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(false);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
    
    // PBI-BE-A3
    // API for VIEW ALL APPOINTMENT BY DOCTOR IN FE VUE
    @GetMapping("/doctorid/{doctorId}")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseRestDTO>>> getAppointmentsByDoctorId(
            @PathVariable String doctorId, @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseRestDTO>>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        
        try {
            // Mengirim token ke service untuk diproses
            List<AppointmentResponseRestDTO> appointments = appointmentRestService.getAppointmentsByDoctorId(doctorId, token);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("OK");
            baseResponseDTO.setData(appointments);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (UserNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    // PBI-BE-A4
    @GetMapping("/patientid/{patientId}")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseRestDTO>>> getAppointmentsByPatientId(
            @PathVariable UUID patientId, @RequestHeader("Authorization") String authorizationHeader) {
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseRestDTO>>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        
        try {
            // Mengirim token ke service untuk diproses
            List<AppointmentResponseRestDTO> appointments = appointmentRestService.getAppointmentsByPatientId(patientId, token);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("OK");
            baseResponseDTO.setData(appointments);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (UserNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    // PBI-BE-A5
    // API TO BE CONSUME IN WEB 
    // "Terdapat filter berdasarkan rentang waktu, dari 'from' hingga 'to' secara inklusif (wajib mengonsumsi API yang telah dibentuk)"
    @GetMapping("/datefrom/{datefrom}/dateto/{dateto}")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseRestDTO>>> getAppointmentsByDateRange(
            @PathVariable String datefrom,
            @PathVariable String dateto, @RequestHeader("Authorization") String authorizationHeader) {
    
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseRestDTO>>();
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        
        try {
            // Format parsing tanggal
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
            // Parsing tanggal dari String ke LocalDate dan lakukan penyesuaian
            LocalDate fromDate = LocalDate.parse(datefrom, formatter).plusDays(1);
            LocalDate toDate = LocalDate.parse(dateto, formatter).plusDays(1);
    
            // Panggil service untuk mendapatkan data appointment
            List<AppointmentResponseRestDTO> appointments = appointmentRestService.getAppointmentsByDateRange(fromDate, toDate, token);
            
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("OK");
            baseResponseDTO.setData(appointments);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (UserNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
    
    // PBI-BE-A7
    // KONTEKS WEB HANYA DIAKSES OLEH ADMIN
    // Aksi ini dilakukan pada halaman detail appointment
    // Status appointment hanya dapat diubah menjadi "Cancelled" atau "Done"
    // Status dapat diubah menjadi "Done" jika diagnosis & treatment sudah ada.
    // Status tidak dapat diubah menjadi "Cancelled" jika waktu sekarang adalah satu hari (atau kurang) sebelum tanggal appointment tersebut.
    @PutMapping("/updatestatus/cancelled/{idAppointment}")
    public ResponseEntity<BaseResponseDTO<Void>> updateAppointmentStatusToCancelled(@PathVariable String idAppointment, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return updateAppointmentStatus(idAppointment, 2, "cancelled", token);
    }

    // PBI-BE-A7
    @PutMapping("/updatestatus/done/{idAppointment}")
    public ResponseEntity<BaseResponseDTO<Void>> updateAppointmentStatusToDone(@PathVariable String idAppointment, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        return updateAppointmentStatus(idAppointment, 1, "done", token);
    }

    // Metode helper untuk memperbarui status
    // PBI-BE-A7
    private ResponseEntity<BaseResponseDTO<Void>> updateAppointmentStatus(String idAppointment, int status, String statusText, String token) {
        var baseResponseDTO = new BaseResponseDTO<Void>();
        
        try {
            // Mengirim token untuk memverifikasi pengguna yang sedang login
            appointmentRestService.updateAppointmentStatus(idAppointment, status, token);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Appointment status successfully updated to " + statusText);
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

        } catch (UserNotFound e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (UserUnauthorized e) {
            baseResponseDTO.setStatus(HttpStatus.FORBIDDEN.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setData(null);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }
}
