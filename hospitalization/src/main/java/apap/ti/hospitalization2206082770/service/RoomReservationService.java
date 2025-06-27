package apap.ti.hospitalization2206082770.service;

import java.util.Date;
import java.util.List;

import apap.ti.hospitalization2206082770.model.Room;

public interface RoomReservationService {
    List<Room> findAvailableRooms(Date dateIn, Date dateOut);
} 