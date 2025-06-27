package apap.ti.appointment2206082612.service;

import apap.ti.appointment2206082612.dto.response.TreatmentResponseDTO;
import apap.ti.appointment2206082612.model.Treatment;

import java.util.List;

public interface TreatmentService {
    List<Treatment> getAllTreatments();
    TreatmentResponseDTO getTreatmentById(Long id) throws Exception;
    List<Treatment> getTreatmentsByIds(List<Long> treatmentIds);

}
