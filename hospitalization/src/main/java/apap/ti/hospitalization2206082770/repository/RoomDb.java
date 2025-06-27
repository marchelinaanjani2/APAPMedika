package apap.ti.hospitalization2206082770.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import apap.ti.hospitalization2206082770.model.Room;
import java.util.*;


@Repository
public interface RoomDb extends JpaRepository<Room, String> {
    List<Room> findAll();
    
    @Query("SELECT r FROM Room r") // JPQL
    List<Room> findAllRooms(); // Custom method name

    List<Room> findByIsDeletedFalse(); 

    Room findTopByOrderByIdDesc(); 

     

}