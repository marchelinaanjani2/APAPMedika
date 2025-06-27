package apap.ti.appointment2206082612.restservice;

import apap.ti.appointment2206082612.restdto.response.TreatmentResponseRestDTO;

import java.util.List;

public interface TreatmentRestService {
    List<TreatmentResponseRestDTO> getAllTreatments();
    TreatmentResponseRestDTO getTreatmentById(Long id);
}
