package apap.ti.hospitalization2206082770.service;


import java.util.stream.Collectors;
import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.repository.ReservationDb;
import apap.ti.hospitalization2206082770.repository.RoomDb;
import apap.ti.hospitalization2206082770.restdto.response.EndUserResponseDTO;
import apap.ti.hospitalization2206082770.service.RoomReservationService;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class RoomReservationServiceImpl implements RoomReservationService {
    private final RoomService roomService;

    private final ReservationService reservationService;
    private final ReservationDb reservationDb;
    private final RoomDb roomDb;



    public RoomReservationServiceImpl(RoomService roomService, ReservationDb reservationDb, ReservationService reservationService, RoomDb roomDb) {
        this.roomService = roomService; 
        this.reservationService = reservationService; 
        this.reservationDb = reservationDb; 
        this.roomDb = roomDb; 
    }
    
    // Fungsi untuk menemukan kamar yang tersedia
    @Override
    public List<Room> findAvailableRooms(Date dateIn, Date dateOut) { 
        List<Room> allRooms = roomDb.findByIsDeletedFalse();
        Date today = new Date();

        return allRooms.stream()
                .filter(room -> {
                    List<Reservation> overlappingReservations = reservationDb.findByRoomIdAndDateInLessThanEqualAndDateOutGreaterThanEqual(room, dateIn, dateOut);

                    long expiredReservationCount = overlappingReservations.stream()
                            .filter(reservation -> reservation.getDateOut().before(today)) 
                            .count();
                    long activeReservationsCount = overlappingReservations.stream()
                            .filter(reservation -> !reservation.getDateOut().before(today))
                            .count();

                    roomService.increaseRoomCapacity(room.getId(), (int) expiredReservationCount);

                    int availableQuota = room.getMaxCapacity() - (int) activeReservationsCount + (int) expiredReservationCount;
                    availableQuota = Math.max(availableQuota, 0);

                    return availableQuota > 0;
                })
                .collect(Collectors.toList());
    }

}
