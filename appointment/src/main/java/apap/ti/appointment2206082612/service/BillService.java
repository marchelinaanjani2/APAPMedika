package apap.ti.appointment2206082612.service;

import java.util.List;
import java.util.UUID;

import apap.ti.appointment2206082612.restdto.response.PatientResponseDTO;
import apap.ti.appointment2206082612.restdto.response.BillResponseDTO;
import apap.ti.appointment2206082612.restdto.response.EndUserResponseDTO;
import jakarta.persistence.EntityNotFoundException;

public interface BillService {
    BillResponseDTO addBill(String idAppointment, String token, UUID idPatient);
}