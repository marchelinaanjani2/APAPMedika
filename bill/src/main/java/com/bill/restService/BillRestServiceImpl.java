package com.bill.restService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bill.model.Bill;
import com.bill.repository.BillDb;
import com.bill.restdto.request.AddBillRequestRestDTO;
import com.bill.restdto.request.UpdateBillRequestRestDTO;
import com.bill.restdto.response.BillResponseDTO;
import com.bill.restdto.response.EndUserResponseDTO;
import com.bill.restdto.response.ReservationResponseDTO;
import com.bill.service.PolicyService;
import com.bill.service.ProfileService;
import com.bill.restdto.request.BillRequestDTO;

@Service
public class BillRestServiceImpl  implements BillRestService{

    @Autowired
    private BillDb billDb;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PolicyService policyService;

    @Override
    public List<BillResponseDTO> getBillsByPatientId(UUID patientId, String token) {
    List<Bill> bills = billDb.findByPatientIdAndIsDeletedFalse(patientId);
    return bills.stream()
                .map(this::billToBillResponseDTO)  
                .collect(Collectors.toList());
    }


    @Override
    public BillResponseDTO addBillForAppointment(AddBillRequestRestDTO billRequestDTO, String token) {

        Bill newBill = new Bill();
        newBill.setId(UUID.randomUUID());
        newBill.setAppointmentId(billRequestDTO.getAppointmentId());
        newBill.setPatientId(billRequestDTO.getPatientId());
        newBill.setAppointmentFee(0);
        newBill.setStatus(0);
        newBill.setCreatedAt(LocalDateTime.now());
        newBill.setUpdatedAt(LocalDateTime.now());
        billDb.save(newBill);

        return billToBillResponseDTO(newBill);
    }
    
    @Override
    public List<BillResponseDTO> getAllBills() {
        var listBills = billDb.findAll();
        var listBillsResponseDTO = new ArrayList<BillResponseDTO>();
        
        listBills.forEach(bills -> {
            var billsResponseDTO = billToBillResponseDTO(bills);
            listBillsResponseDTO.add(billsResponseDTO);
        });

        return listBillsResponseDTO;
    }

    @Override
    public BillResponseDTO getBillById(UUID idBill) {
        var bill = billDb.findById(idBill).orElse(null);
        if (bill == null) {
            return null;
        }

       return billToBillResponseDTO(bill);
    }


    @Override
    public BillResponseDTO createBillFromReservation(AddBillRequestRestDTO billDTO, String token) {
        if (billDTO.getReservationId() == null || billDTO.getReservationId().isEmpty()) {
            throw new IllegalArgumentException("Reservation ID is required.");
        }

        Bill bill = new Bill();
        bill.setId(UUID.randomUUID());
        bill.setReservationId(billDTO.getReservationId());

        Double reservationFee = billDTO.getReservationFee() != 0  ? billDTO.getReservationFee() : 0.0;
        bill.setReservationFee(reservationFee);
        bill.setCreatedAt(LocalDateTime.now());
        bill.setPatientId(billDTO.getPatientId() != null ? billDTO.getPatientId() : null);
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        bill.setCreatedBy(currentUser.getName());
        bill.setStatus(1); 
        bill.setSubTotal(reservationFee); 
        bill.setTotalAmountDue(reservationFee); 

        var newBillFromReservation = billDb.save(bill);

        return billToBillResponseDTO(newBillFromReservation);
    }



    @Override
    public BillResponseDTO updateBill(UUID idBill, UpdateBillRequestRestDTO billDTO, String token) {
        Bill bill = billDb.findById(idBill)
                .orElseThrow(() -> new IllegalArgumentException("Bill dengan ID " + idBill + " tidak ditemukan"));

        if (billDTO.getPolicyId() != null) {
            var policy = policyService.getPolicyById(billDTO.getPolicyId());
            if (policy != null) {
                bill.setPolicyId(billDTO.getPolicyId());


                double subTotal = policy.getTotalCoverage() - bill.getSubTotal();
                bill.setSubTotal(subTotal);
            } else {
                throw new IllegalArgumentException("Policy dengan ID " + billDTO.getPolicyId() + " tidak ditemukan");
            }
        }else{
            bill.setPolicyId(null);
        }

        if (billDTO.getReservationId() != null) {
            bill.setReservationId(billDTO.getReservationId());
        }
        bill.setStatus(billDTO.getStatus());
        bill.setUpdatedAt(LocalDateTime.now());
        EndUserResponseDTO currentUser = profileService.getCurrentUser(token);
        bill.setUpdatedBy(currentUser.getName());
        Bill updatedBill = billDb.save(bill);

        return billToBillResponseDTO(updatedBill);
    }



    public BillResponseDTO billToBillResponseDTO(Bill bill) {
        var billResponseDTO = new BillResponseDTO();
        
        billResponseDTO.setId(bill.getId());
        billResponseDTO.setPolicyId(bill.getPolicyId());
        billResponseDTO.setAppointmentId(bill.getAppointmentId());
        billResponseDTO.setReservationId(bill.getReservationId());
        billResponseDTO.setPatientId(bill.getPatientId());
        billResponseDTO.setStatus(bill.getStatus());
        billResponseDTO.setCreatedAt(bill.getCreatedAt());
        billResponseDTO.setUpdatedAt(bill.getUpdatedAt());
        billResponseDTO.setCreatedBy(bill.getCreatedBy());
        billResponseDTO.setUpdatedBy(bill.getUpdatedBy());
        billResponseDTO.setDeleted(bill.isDeleted());
        billResponseDTO.setReservationFee(bill.getReservationFee());
        billResponseDTO.setAppointmentFee(bill.getAppointmentFee());
        billResponseDTO.setSubTotal(bill.getSubTotal());

        return billResponseDTO;
    }

    
    public BillResponseDTO mapBillToBillResponseDTO(Bill bill) {
        // Create the DTO and set its fields based on the Bill entity
        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setId(bill.getId());
        billResponseDTO.setPolicyId(bill.getPolicyId());
        billResponseDTO.setAppointmentId(bill.getAppointmentId());
        billResponseDTO.setReservationId(bill.getReservationId());
        billResponseDTO.setPatientId(bill.getPatientId());
        billResponseDTO.setStatus(bill.getStatus());
        billResponseDTO.setCreatedAt(bill.getCreatedAt());
        billResponseDTO.setUpdatedAt(bill.getUpdatedAt());
        billResponseDTO.setCreatedBy(bill.getCreatedBy());
        billResponseDTO.setUpdatedBy(bill.getUpdatedBy());
        billResponseDTO.setDeleted(bill.isDeleted());
    
        // Return the DTO
        return billResponseDTO;
    }
}
