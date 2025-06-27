package apap.ti.hospitalization2206082770.restService;

import java.util.List;

import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.restdto.request.AddReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.request.UpdateReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;


public interface ReservationRestService {
    List<ReservationResponseDTO> getAllReservations(String token);
    ReservationResponseDTO getReservationById(String idReservation);
    String deleteReservation(String idReservation);
    ReservationResponseDTO updateFacilitiesReservation(String idReservation, UpdateReservationsRequestRestDTO reservationDTO);
    ReservationResponseDTO updateRoomAndDateReservation(String idReservation, UpdateReservationsRequestRestDTO reservationDTO);
    ReservationResponseDTO addReservation(AddReservationsRequestRestDTO reservationDTO, String token);
    ReservationResponseDTO reservationToReservationResponseDTO(Reservation reservation);
}
    
