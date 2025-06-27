package apap.ti.hospitalization2206082770.repository;

import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.model.Room;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import apap.ti.hospitalization2206082770.repository.ReservationDb;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ReservationDb extends JpaRepository<Reservation, String> {
    
    @Query("SELECT COUNT(r) FROM Reservation r")
    int countAllReservations();

    List<Reservation> findByRoomIdAndDateInLessThanEqualAndDateOutGreaterThanEqual(Room roomId, Date dateOut, Date dateIn);

    List<Reservation> findByDateOutBefore(Date today);

    List<Reservation> findReservationsByDateOutBefore(Date currentDate);

    // Add a method to find reservations by room
    List<Reservation> findByRoomId(Room room);
    List<Reservation> findByIsDeletedFalse();
    Reservation findByIdAndIsDeletedFalse(String id);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByIsDeletedFalseAndNurse(UUID nurseId);
    List<Reservation> findByIsDeletedFalseAndPatientId(UUID patientId);


}
