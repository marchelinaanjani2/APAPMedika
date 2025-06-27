package com.bill.restController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bill.restService.BillRestService;
import com.bill.restdto.request.AddBillRequestRestDTO;
import com.bill.restdto.request.UpdateBillRequestRestDTO;
import com.bill.restdto.response.BaseResponseDTO;
import com.bill.restdto.response.BillResponseDTO;


@RestController
@RequestMapping("/api/bill")
public class BillRestController {
    
    @Autowired
    private BillRestService billRestService;

    @GetMapping("/{idPatient}")
    public ResponseEntity<BaseResponseDTO<List<BillResponseDTO>>> billByPatientId(@PathVariable("idPatient") UUID idPatient, @RequestHeader("Authorization") String authorizationHeader) {
    
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        BaseResponseDTO<List<BillResponseDTO>> baseResponseDTO = new BaseResponseDTO<>();
    
        try {
            List<BillResponseDTO> bills = billRestService.getBillsByPatientId(idPatient, token);
    
            if (bills.isEmpty()) {
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage("No bills found for the provided patient ID.");
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(bills);
            baseResponseDTO.setMessage("Bills successfully retrieved.");
            baseResponseDTO.setTimestamp(new Date());
    
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Error retrieving bills: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<BillResponseDTO>>> listBills() {
        List<BillResponseDTO> listBills = billRestService.getAllBills();

        var baseResponseDTO = new BaseResponseDTO<List<BillResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listBills);
        baseResponseDTO.setMessage("List Bills berhasil diambil");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
    
    @PostMapping("/add/forReservation")
    public ResponseEntity<?> addBillForReservation(@RequestBody AddBillRequestRestDTO billDTO, BindingResult bindingResult, @RequestHeader("Authorization") String authorizationHeader) {
   
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        var baseResponseDTO = new BaseResponseDTO<BillResponseDTO>();
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


        if (billDTO.getReservationId() == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data reservation tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        BillResponseDTO billReservation = billRestService.createBillFromReservation(billDTO, token);
        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(billReservation);
        baseResponseDTO.setMessage(String.format("Bill dengan ID %s berhasil ditambahkan", billReservation.getId()));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }

    

    @PostMapping("/add/forAppointment")
    public ResponseEntity<?> addBillForAppointment(@RequestBody AddBillRequestRestDTO billRequestDTO, BindingResult bindingResult, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        var baseResponseDTO = new BaseResponseDTO<BillResponseDTO>();

        // Validasi input menggunakan BindingResult
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

        // Panggil service untuk menambahkan Bill dengan request body
        BillResponseDTO billReservation = billRestService.addBillForAppointment(billRequestDTO, token);

        // Jika data appointment tidak ditemukan
        if (billReservation == null || billReservation.getAppointmentId() == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data appointment tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        if (billReservation.getId() == null)    {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Gagal Membuat Bill Karena User = NULL"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
        // Jika terjadi kesalahan saat membuat bill
        if (billReservation.getId() == null) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage("Gagal Membuat Bill Karena User = NULL");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(billReservation);
        baseResponseDTO.setMessage(String.format("Bill dengan ID %s berhasil ditambahkan", billReservation.getId()));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }
       

    @PutMapping("/{idBill}/update")
    public ResponseEntity<?> updateBill(
            @PathVariable("idBill") UUID idBill,
            @RequestBody UpdateBillRequestRestDTO billDTO,
            BindingResult bindingResult,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        var baseResponseDTO = new BaseResponseDTO<BillResponseDTO>();

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
   
            BillResponseDTO updatedBill = billRestService.updateBill(idBill, billDTO, token);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(updatedBill);
            baseResponseDTO.setMessage(String.format("Bill dengan ID %s berhasil diperbarui", idBill));
            baseResponseDTO.setTimestamp(new Date());
            
            return ResponseEntity.ok(baseResponseDTO);
        } catch (IllegalArgumentException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.badRequest().body(baseResponseDTO);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Error while updating the bill: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDTO);
        }
    }


    @GetMapping("/detail/{idBill}")
    public ResponseEntity<?> detailBills(@PathVariable("idBill") UUID idBill) {
        var baseResponseDTO = new BaseResponseDTO<BillResponseDTO>();
        BillResponseDTO bill = billRestService.getBillById(idBill);
        
        if (bill == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data bill tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(bill);
        baseResponseDTO.setMessage(String.format("Bill dengan ID %s berhasil ditemukan", idBill));
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}
