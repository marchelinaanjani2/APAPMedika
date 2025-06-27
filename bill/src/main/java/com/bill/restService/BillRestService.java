package com.bill.restService;

import java.util.List;
import java.util.UUID;

import com.bill.restdto.request.AddBillRequestRestDTO;
import com.bill.restdto.request.UpdateBillRequestRestDTO;
import com.bill.restdto.response.BillResponseDTO;


public interface BillRestService {
    List<BillResponseDTO> getBillsByPatientId(UUID patientId, String token);
    List<BillResponseDTO> getAllBills();
    BillResponseDTO getBillById(UUID idBill);
    BillResponseDTO createBillFromReservation(AddBillRequestRestDTO billDTO, String token);
    BillResponseDTO updateBill(UUID idBill, UpdateBillRequestRestDTO billDTO, String token);
    BillResponseDTO addBillForAppointment(AddBillRequestRestDTO billRequestDTO, String token);


}


