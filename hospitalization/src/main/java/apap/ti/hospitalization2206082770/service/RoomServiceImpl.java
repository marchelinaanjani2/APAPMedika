package apap.ti.hospitalization2206082770.service;

import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.repository.RoomDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomDb roomDb;


    private Map<String, Integer> availableCapacityMap = new HashMap<>(); // Menyimpan kapasitas yang tersedia berdasarkan ID ruangan

   
    public RoomServiceImpl(RoomDb roomDb) {
        this.roomDb = roomDb;
    }

    @Override
    public void initializeAvailableCapacity(String roomId) {
        Room room = roomDb.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        availableCapacityMap.put(roomId, room.getMaxCapacity()); // Inisialisasi kapasitas yang tersedia
    }

    @Override
    public void decreaseRoomCapacity(String roomId) {
        Integer currentAvailableCapacity = availableCapacityMap.get(roomId);
        if (currentAvailableCapacity != null && currentAvailableCapacity > 0) {
            availableCapacityMap.put(roomId, currentAvailableCapacity - 1); // Mengurangi kapasitas yang tersedia
        } else {
            throw new IllegalStateException("Kapasitas ruangan sudah penuh atau tidak ditemukan");
        }
    }

    @Override
    public void increaseRoomCapacity(String roomId, int count) {
        Integer currentAvailableCapacity = availableCapacityMap.get(roomId);
        if (currentAvailableCapacity != null) {
            availableCapacityMap.put(roomId, currentAvailableCapacity + count); // Tambahkan kapasitas yang tersedia
        } else {
            throw new IllegalStateException("Ruangan tidak ditemukan");
        }
    }

    @Override
    public int getAvailableCapacity(String roomId) {
        return availableCapacityMap.getOrDefault(roomId, 0); // Mengembalikan kapasitas yang tersedia
    }

    @Override
    public synchronized Room createRoom(Room room) {
        // Implementasi pembuatan ID dan logika lainnya tetap sama
        String newId;
        Room lastRoom = roomDb.findTopByOrderByIdDesc();

        if (lastRoom != null) {
            int lastIdNumber = Integer.parseInt(lastRoom.getId().substring(2));
            newId = String.format("RM%04d", lastIdNumber + 1);
        } else {
            newId = "RM0001";
        }

        while (roomDb.existsById(newId)) {
            int idNumber = Integer.parseInt(newId.substring(2));
            newId = String.format("RM%04d", idNumber + 1);
        }

        room.setId(newId);
        Room savedRoom = roomDb.save(room);
        initializeAvailableCapacity(savedRoom.getId());
        return savedRoom;
    }


    @Override
    public Room getRoomById(String idRoom) {
        Room room = roomDb.findById(idRoom).orElse(null);
        if (room != null) {
            initializeAvailableCapacity(room.getId()); // Inisialisasi kapasitas saat ruangan ditemukan
        }
        return room;
    }


    @Override
    public String getLastRoomId() {
        List<Room> rooms = roomDb.findAll();
        if (rooms == null || rooms.isEmpty()) {
            return null;
        }
        return rooms.get(rooms.size() - 1).getId();
    }

   
	
}
