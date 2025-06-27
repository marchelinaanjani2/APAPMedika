// AppointmentDb.java
package apap.ti.appointment2206082612.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apap.ti.appointment2206082612.model.Appointment;

@Repository
public interface AppointmentDb extends JpaRepository<Appointment, String> {
    
    // Cari appointment berdasarkan status
    List<Appointment> findByStatus(int status);

    List<Appointment> findByDateBetween(Date from, Date to);

    boolean existsByDoctorIdAndDate(String doctorId, Date date);

    List<Appointment> findByDoctorId(String doctorId);
    List<Appointment> findByDate(Date date);

    List<Appointment> findByPatientId(UUID patientId);

}
