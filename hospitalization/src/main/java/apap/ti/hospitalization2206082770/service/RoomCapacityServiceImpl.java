package apap.ti.hospitalization2206082770.service;

import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.repository.ReservationDb;
import apap.ti.hospitalization2206082770.repository.RoomDb;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RoomCapacityServiceImpl implements RoomCapacityService {

    private final ReservationDb reservationDb;

    private final RoomService roomService;

    private final RoomDb roomDb;

    public RoomCapacityServiceImpl(ReservationDb reservationDb, RoomService roomService, RoomDb roomDb) {
        this.reservationDb = reservationDb; 
        this.roomService = roomService;
        this.roomDb = roomDb; 
    }
    @Override
    public void decreaseRoomCapacity(String room) {
        roomService.decreaseRoomCapacity(room); 
    }

    @Override
    public void increaseRoomCapacity(String room, int count) {
        roomService.increaseRoomCapacity(room, count); 
    }

    // Scheduled method untuk menambah kapasitas kamar setelah reservasi selesai
    @Scheduled(cron = "0 0 0 * * ?") // Menjalankan setiap hari pada tengah malam
    public void updateRoomCapacityDec() {
        Date today = new Date();
        List<Reservation> expiredReservations = reservationDb.findByDateOutBefore(today);
        Map<String, Long> roomToExpiredReservationCount = new HashMap<>();
        for (Reservation reservation : expiredReservations) {
            String roomId = reservation.getRoomId().getId();
            roomToExpiredReservationCount.put(roomId, roomToExpiredReservationCount.getOrDefault(roomId, 0L) + 1);
        }

        for (Map.Entry<String, Long> entry : roomToExpiredReservationCount.entrySet()) {
            String roomId = entry.getKey();
            int expiredCount = entry.getValue().intValue();
            Room room = roomDb.findById(roomId).orElse(null); 
            if (room != null) {
                increaseRoomCapacity(roomId, expiredCount); 

                System.out.println("Kapasitas kamar " + room.getName() + " bertambah " + expiredCount + " setelah " + expiredCount + " reservasi selesai.");
            }
        }
    }

}
