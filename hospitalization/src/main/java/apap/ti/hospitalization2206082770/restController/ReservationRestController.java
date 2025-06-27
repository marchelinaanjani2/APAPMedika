package apap.ti.hospitalization2206082770.restController;

import org.hibernate.engine.internal.Collections;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.hospitalization2206082770.model.Facility;
import apap.ti.hospitalization2206082770.model.Room;
import apap.ti.hospitalization2206082770.restService.ReservationRestService;
import apap.ti.hospitalization2206082770.restdto.request.UpdateReservationsRequestRestDTO;
import apap.ti.hospitalization2206082770.restdto.request.AddReservationsRequestRestDTO;

import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.EndUserResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082770.service.ReservationService;
import apap.ti.hospitalization2206082770.service.RoomReservationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    private final ReservationRestService reservationRestService;
    private final ReservationService reservationService;


    private final RoomReservationService roomReservationService;


    public ReservationRestController(ReservationRestService reservationRestService, RoomReservationService roomReservationService, ReservationService reservationService ) {
        this.reservationRestService = reservationRestService;
        this.reservationService = reservationService;
        this.roomReservationService = roomReservationService;

    }

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<ReservationResponseDTO>>> listReservation(
        @RequestHeader("Authorization") String authorizationHeader) { 

        
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        
        List<ReservationResponseDTO> listReservation = reservationRestService.getAllReservations(token);

        var baseResponseDTO = new BaseResponseDTO<List<ReservationResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listReservation);
        baseResponseDTO.setMessage("List Reservation berhasil diambil");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
    
    @GetMapping("/{idReservation}")
    public ResponseEntity<?> detailReservation(@PathVariable("idReservation") String idReservation) {
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
        ReservationResponseDTO reservation = reservationRestService.getReservationById(idReservation);
        
        if (reservation == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data reservation tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(reservation);
        baseResponseDTO.setMessage(String.format("Reservation dengan ID %s berhasil ditemukan", idReservation));
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{idReservation}/delete")
    public ResponseEntity<?> deleteProyek(@PathVariable("idReservation") String idReservation) {
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
        String result = reservationRestService.deleteReservation(idReservation);
        
        baseResponseDTO.setTimestamp(new Date());

        switch (result) {
            case "NOT_FOUND":
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage("Data Reservation tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);

            case "ONGOING":
                baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponseDTO.setMessage("Reservation sudah berjalan dan tidak bisa dibatalkan");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);

            case "DELETED":
                baseResponseDTO.setStatus(HttpStatus.OK.value());
                baseResponseDTO.setMessage(String.format("Reservation dengan ID %s berhasil dihapus", idReservation));
                return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);

            default:
                baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                baseResponseDTO.setMessage("Terjadi kesalahan saat menghapus reservation");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }


    @PutMapping("/{idReservation}/update-room-date")
    public ResponseEntity<?> updateRoomAndDate(
            @Valid @PathVariable("idReservation") String idReservation,
            @RequestBody UpdateReservationsRequestRestDTO reservationDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
        EndUserResponseDTO currentUser = reservationService.getCurrentUser(token);
        if (!"NURSE".equalsIgnoreCase(currentUser.getRole())) {
            throw new IllegalArgumentException("Hanya Nurse yang memiliki akses untuk mengupdate Reservation! Role Anda: " + currentUser.getRole());
        } 
        // Validasi input
        if (bindingResult.hasFieldErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages.append(error.getDefaultMessage()).append("; ");
            }
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages.toString());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        }

        try {
            ReservationResponseDTO updatedReservation = reservationRestService.updateRoomAndDateReservation(idReservation, reservationDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(updatedReservation);
            baseResponseDTO.setMessage(String.format("Reservation dengan ID %s berhasil diperbarui (Room/Date)", idReservation));
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.badRequest().body(baseResponseDTO);
        }
    }


    @PutMapping("/{idReservation}/update-facilities")
    public ResponseEntity<?> updateFacilities(
            @Valid @PathVariable("idReservation") String idReservation,
            @RequestBody UpdateReservationsRequestRestDTO reservationDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();

        EndUserResponseDTO currentUser = reservationService.getCurrentUser(token);
        if (!"NURSE".equalsIgnoreCase(currentUser.getRole())) {
            throw new IllegalArgumentException("Hanya Nurse yang memiliki akses untuk mengupdate Reservation! Role Anda: " + currentUser.getRole());
        }  
        // Validasi input
        if (bindingResult.hasFieldErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages.append(error.getDefaultMessage()).append("; ");
            }
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages.toString());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponseDTO);
        }

        try {
            ReservationResponseDTO updatedReservation = reservationRestService.updateFacilitiesReservation(idReservation, reservationDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(updatedReservation);
            baseResponseDTO.setMessage(String.format("Reservation dengan ID %s berhasil diperbarui (Facilities)", idReservation));
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.badRequest().body(baseResponseDTO);
        }
    }



    @PostMapping(value = "/{reservationId}/facilities-update", params = { "addRow" })
    public ResponseEntity<BaseResponseDTO<AddReservationsRequestRestDTO>> addRowFacility(
            @PathVariable("reservationId") String reservationId,
            @RequestBody AddReservationsRequestRestDTO reservationDTO) {
        // Tambahkan row baru pada fasilitas
        if (reservationDTO.getFacilities() == null) {
            reservationDTO.setFacilities(new ArrayList<>());
        }
        reservationDTO.getFacilities().add(new Facility());

        // Buat respon
        BaseResponseDTO<AddReservationsRequestRestDTO> response = new BaseResponseDTO<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(reservationDTO);
        response.setMessage("Fasilitas baru berhasil ditambahkan.");
        response.setTimestamp(new Date());

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{reservationId}/facilities", params = { "deleteRow" })
    public ResponseEntity<BaseResponseDTO<AddReservationsRequestRestDTO>> deleteRowFacility(
            @PathVariable("reservationId") String reservationId,
            @RequestParam("deleteRow") int row,
            @RequestBody AddReservationsRequestRestDTO reservationDTO) {
 
        reservationDTO.getFacilities().remove(row);

        BaseResponseDTO<AddReservationsRequestRestDTO> response = new BaseResponseDTO<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(reservationDTO);
        response.setMessage("Fasilitas pada indeks " + row + " berhasil dihapus.");
        response.setTimestamp(new Date());

        return ResponseEntity.ok(response);
    }

    //untuk get kamar yg tersedia 
    @GetMapping("/find")
    public ResponseEntity<BaseResponseDTO<List<Map<String, Object>>>> findAvailableRooms(
            @RequestParam("dateIn") String dateIn,
            @RequestParam("dateOut") String dateOut,
            @RequestHeader("Authorization") String authorizationHeader) {
    
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        EndUserResponseDTO currentUser = reservationService.getCurrentUser(token);
        if (!"Nurse".equalsIgnoreCase(currentUser.getRole()) ) {
            throw new IllegalArgumentException("Hanya Nurse  yang memiliki akses untuk melihat semua Room! Role Anda: " + currentUser.getRole());
        } 
        // Date format for parsing the date strings
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date checkInDate;
        Date checkOutDate;
    
        try {
 
            checkInDate = formatter.parse(dateIn);
            checkOutDate = formatter.parse(dateOut);
        } catch (ParseException e) {

            BaseResponseDTO<List<Map<String, Object>>> errorResponse = new BaseResponseDTO<>();
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            errorResponse.setMessage("Invalid date format. Please use 'yyyy-MM-dd'.");
            errorResponse.setTimestamp(new Date());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    
       
        List<Room> availableRooms = roomReservationService.findAvailableRooms(checkInDate, checkOutDate);

        List<Map<String, Object>> roomData = new ArrayList<>();
        for (Room room : availableRooms) {
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("room", room);
            roomData.add(roomInfo);
        }
    
 
        BaseResponseDTO<List<Map<String, Object>>> response = new BaseResponseDTO<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(roomData); 
        response.setMessage("Available rooms fetched successfully.");
        response.setTimestamp(new Date());
    
        return ResponseEntity.ok(response); 
    }
    

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody AddReservationsRequestRestDTO reservationDTO, BindingResult bindingResult, @RequestHeader("Authorization") String authorizationHeader) {
        
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        System.out.println("Received token: " + token);
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();
        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        ReservationResponseDTO reservation = reservationRestService.addReservation(reservationDTO, token);
        if (reservation == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data reservation tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(reservation);
        baseResponseDTO.setMessage(String.format("Reservation dengan ID %s berhasil ditambahkan", reservation.getId()));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }
   
}
