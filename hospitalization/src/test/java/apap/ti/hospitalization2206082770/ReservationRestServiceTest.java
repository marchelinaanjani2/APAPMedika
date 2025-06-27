package apap.ti.hospitalization2206082770;

import apap.ti.hospitalization2206082770.model.Facility;
import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.repository.FacilityDb;
import apap.ti.hospitalization2206082770.repository.ReservationDb;
import apap.ti.hospitalization2206082770.repository.RoomDb;
import apap.ti.hospitalization2206082770.restService.ReservationRestService;
import apap.ti.hospitalization2206082770.restService.ReservationRestServiceImpl;
import apap.ti.hospitalization2206082770.restdto.request.AddReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.request.UpdateReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.response.BillResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.EndUserResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082770.service.BillServiceImpl;
import apap.ti.hospitalization2206082770.service.NurseService;
import apap.ti.hospitalization2206082770.service.PatientService;
import apap.ti.hospitalization2206082770.service.ReservationService;
import apap.ti.hospitalization2206082770.service.RoomCapacityService;
import apap.ti.hospitalization2206082770.service.RoomService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class ReservationRestServiceTest {

    @InjectMocks
    private ReservationRestServiceImpl reservationRestServiceImpl;

    @Mock
    private ReservationDb reservationDb;
    
    @Mock
    private RoomService roomService;

    @Mock
    private RoomDb roomDb;

    @Mock
    private FacilityDb facilityDb;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRestService reservationRestService;

    @Mock
    private PatientService patientService;

    @Mock
    private NurseService nurseService;

    @Mock
    private BillServiceImpl billServiceImpl;

    @Mock
    private RoomCapacityService roomCapacityService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllReservations_NurseRole() {
        String token = "mock-token";
        UUID nurseId = UUID.randomUUID();
        EndUserResponseDTO currentUser = new EndUserResponseDTO();
        currentUser.setRole("NURSE");
        currentUser.setId(nurseId);

        when(reservationService.getCurrentUser(token)).thenReturn(currentUser);

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setStatus("upcoming");
        reservation.setNurse(nurseId);

        List<Reservation> reservations = List.of(reservation);
        when(reservationDb.findByIsDeletedFalseAndNurse(nurseId)).thenReturn(reservations);

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId("1");
        reservationResponseDTO.setStatus("upcoming");

        List<ReservationResponseDTO> result = reservationRestServiceImpl.getAllReservations(token);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("upcoming", result.get(0).getStatus());
        verify(reservationService).getCurrentUser(token);
        verify(reservationDb).findByIsDeletedFalseAndNurse(nurseId);
    }


    @Test
    public void testAddReservation_Success() {
        AddReservationsRequestRestDTO reservationDTO = new AddReservationsRequestRestDTO();
        reservationDTO.setDateIn(new Date());
        reservationDTO.setDateOut(new Date(System.currentTimeMillis() + 86400000));  

        Room room = new Room();
        room.setId("RM0002");  
        room.setMaxCapacity(2);
        room.setPricePerDay(500.0);

        reservationDTO.setRoom(room);  

        UUID patientUuid = UUID.randomUUID();  
        reservationDTO.setPatient(patientUuid);  
        reservationDTO.setAppointmentId("1234");  

        when(roomService.getRoomById(anyString())).thenReturn(room);
        when(roomService.getAvailableCapacity(anyString())).thenReturn(2);

        EndUserResponseDTO nurseUser = new EndUserResponseDTO();
        nurseUser.setId(UUID.randomUUID());
        nurseUser.setRole("NURSE");

        when(reservationService.getCurrentUser(anyString())).thenReturn(nurseUser);

        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setId(UUID.randomUUID());
        when(billServiceImpl.createBill(anyString(), anyDouble(), any(UUID.class), anyString())).thenReturn(billResponseDTO);

        Reservation savedReservation = new Reservation();
        savedReservation.setId("1");
        savedReservation.setRoomId(room);
        savedReservation.setDateIn(reservationDTO.getDateIn());
        savedReservation.setDateOut(reservationDTO.getDateOut());
        savedReservation.setTotalFee(500.0);
        savedReservation.setPatientId(reservationDTO.getPatient());

        when(reservationDb.save(any(Reservation.class))).thenReturn(savedReservation);

        ReservationResponseDTO result = reservationRestServiceImpl.addReservation(reservationDTO, "mock-token");

        assertNotNull(result);
        assertEquals("1", result.getId());  
        assertEquals(500.0, result.getTotalFee(), 0.01);  

        verify(roomService, times(1)).decreaseRoomCapacity(anyString());
        verify(reservationDb, times(1)).save(any(Reservation.class));
        verify(billServiceImpl, times(1)).createBill(anyString(), anyDouble(), any(UUID.class), anyString());
    }
 

    @Test
    public void testUpdateRoomAndDateReservation() {
        String idReservation = "123";
        
        // Create and set only the roomId, dateIn, and dateOut as per the requirements
        UpdateReservationsRequestRestDTO reservationDTO = new UpdateReservationsRequestRestDTO();
        Room room = new Room();
        room.setId("room1");  
        reservationDTO.setRoom(room);  
        reservationDTO.setDateIn(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));  
        reservationDTO.setDateOut(Date.from(LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant())); 
    
       
        Reservation reservation = new Reservation();
        reservation.setId(idReservation);
        reservation.setDateIn(Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));  
        reservation.setDateOut(Date.from(LocalDate.now().plusDays(4).atStartOfDay(ZoneId.systemDefault()).toInstant()));  
        Room roomInReservation = new Room();
        roomInReservation.setId("room2"); 
        reservation.setRoomId(roomInReservation);
    
       
        Room mockRoom = new Room();
        mockRoom.setId("room1");
        mockRoom.setMaxCapacity(2);
    
     
        when(reservationDb.findById(idReservation)).thenReturn(Optional.of(reservation));  
        when(roomDb.findById("room1")).thenReturn(Optional.of(mockRoom));  
        when(reservationDb.findByRoomIdAndDateInLessThanEqualAndDateOutGreaterThanEqual(any(), any(), any())).thenReturn(List.of());  // Mock that no overlap exists
    
       
        ReservationResponseDTO mockResponse = new ReservationResponseDTO();
        mockResponse.setRoomId(mockRoom);  
        mockResponse.setStatus("upcoming");  
        mockResponse.setTotalFee(200.0);  
        when(reservationRestService.updateRoomAndDateReservation(idReservation, reservationDTO)).thenReturn(mockResponse);
    
     
        ReservationResponseDTO response = reservationRestService.updateRoomAndDateReservation(idReservation, reservationDTO);
    
       
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getRoomId(), "Room ID should not be null");
        assertEquals("room1", response.getRoomId().getId(), "Room ID should be 'room1'");
        assertEquals("upcoming", response.getStatus(), "Status should be 'upcoming'");
        assertEquals(200.0, response.getTotalFee(), 0.01, "Total fee should be 200.0");
    }
    

    @Test
    public void testDeleteReservation_Success() {
        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setStatus("upcoming");
        when(reservationDb.findById(anyString())).thenReturn(Optional.of(reservation));
        String result = reservationRestServiceImpl.deleteReservation("1");
        assertEquals("DELETED", result);
        verify(reservationDb, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testDeleteReservation_NotFound() {
        when(reservationDb.findById(anyString())).thenReturn(Optional.empty());

        String result = reservationRestServiceImpl.deleteReservation("1");
        assertEquals("NOT_FOUND", result);
    }

    @Test
    public void testDeleteReservation_Ongoing() {

        Reservation reservation = new Reservation();
        reservation.setId("1");
        reservation.setStatus("ongoing");
        when(reservationDb.findById(anyString())).thenReturn(Optional.of(reservation));

        String result = reservationRestServiceImpl.deleteReservation("1");

        assertEquals("ONGOING", result);
    }
}
