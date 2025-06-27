package apap.ti.hospitalization2206082770.service;




import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.restdto.response.EndUserResponseDTO;

public interface ReservationService {
    int getTotalReservationCount();
    boolean isReservationIdExists(String reservationId);
    Reservation getReservationById(String reservationId);
    EndUserResponseDTO getCurrentUser(String token);

} 
