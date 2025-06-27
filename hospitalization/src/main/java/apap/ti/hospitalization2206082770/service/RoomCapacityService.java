package apap.ti.hospitalization2206082770.service;

import apap.ti.hospitalization2206082770.model.Room;

public interface RoomCapacityService {
    void decreaseRoomCapacity(String roomId);
    void increaseRoomCapacity(String room, int count);
    void updateRoomCapacityDec();
   
}
