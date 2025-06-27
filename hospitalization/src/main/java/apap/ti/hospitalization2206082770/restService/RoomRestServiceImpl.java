package apap.ti.hospitalization2206082770.restService;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.repository.ReservationDb;
import apap.ti.hospitalization2206082770.repository.RoomDb;
import apap.ti.hospitalization2206082770.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.RoomResponseDTO;

@Service
public class RoomRestServiceImpl implements RoomRestService{

    private final RoomDb roomDb;

    public RoomRestServiceImpl(RoomDb roomDb) {
        this.roomDb = roomDb;

    }

    @Override
    public List<RoomResponseDTO> getAllRoom() {
        var listRoom = roomDb.findByIsDeletedFalse();
        var listRoomResponseDTO = new ArrayList<RoomResponseDTO>();
        
        listRoom.forEach(room -> {
            var roomResponseDTO = roomToRoomResponseDTO(room);
            listRoomResponseDTO.add(roomResponseDTO);
        });

        return listRoomResponseDTO;

    }

    private RoomResponseDTO roomToRoomResponseDTO(Room room) {
        var roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(room.getId());
        roomResponseDTO.setName(room.getName());
        roomResponseDTO.setDescription(room.getDescription());
        roomResponseDTO.setMaxCapacity(room.getMaxCapacity());
        roomResponseDTO.setPricePerDay(room.getPricePerDay());
        roomResponseDTO.setCreatedDate(room.getCreatedDate());
        roomResponseDTO.setUpdatedDate(room.getUpdatedDate());


        if (room.getListReservations() != null) {
            var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
            room.getListReservations().forEach(reservation -> {
                var reservationResponseDTO = new ReservationResponseDTO();
                reservationResponseDTO.setId(reservation.getId());
                reservationResponseDTO.setTotalFee(reservation.getTotalFee());
                reservationResponseDTO.setDateIn(reservation.getDateIn());
                reservationResponseDTO.setDateOut(reservation.getDateOut());
                reservationResponseDTO.setPatientId(reservation.getPatientId());
                reservationResponseDTO.setNurse(reservation.getNurse());
                reservationResponseDTO.setCreatedDate(reservation.getCreatedDate());
                reservationResponseDTO.setUpdatedDate(reservation.getUpdatedDate());
                listReservationResponseDTO.add(reservationResponseDTO);
            });
            roomResponseDTO.setListReservations(listReservationResponseDTO);
        }

        return roomResponseDTO; 
    }

    

    

    
} 