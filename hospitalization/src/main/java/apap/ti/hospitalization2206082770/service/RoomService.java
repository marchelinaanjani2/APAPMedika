package apap.ti.hospitalization2206082770.service;


import java.util.List;

import apap.ti.hospitalization2206082770.model.Room;

public interface RoomService {
    Room createRoom(Room room);
    Room getRoomById(String idRoom);
    String getLastRoomId();
    void decreaseRoomCapacity(String roomId);
    void initializeAvailableCapacity(String roomId);
    void increaseRoomCapacity(String roomId, int count);
    int getAvailableCapacity(String roomId);
    

}
