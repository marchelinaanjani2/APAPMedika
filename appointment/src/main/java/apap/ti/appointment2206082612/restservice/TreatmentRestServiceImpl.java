package apap.ti.appointment2206082612.restservice;

import apap.ti.appointment2206082612.model.Treatment;
import apap.ti.appointment2206082612.repository.TreatmentDb;
import apap.ti.appointment2206082612.restdto.response.TreatmentResponseRestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TreatmentRestServiceImpl implements TreatmentRestService {

    @Autowired
    private TreatmentDb treatmentDb;

    @Override
    public List<TreatmentResponseRestDTO> getAllTreatments() {
        List<Treatment> treatments = treatmentDb.findAll();
        List<TreatmentResponseRestDTO> responseDTOs = new ArrayList<>();
        treatments.forEach(treatment -> responseDTOs.add(mapTreatmentToResponseDTO(treatment)));
        return responseDTOs;
    }

    @Override
    public TreatmentResponseRestDTO getTreatmentById(Long id) {
        Treatment treatment = treatmentDb.findById(id).orElse(null);
        if (treatment != null) {
            return mapTreatmentToResponseDTO(treatment);
        }
        return null;
    }

    private TreatmentResponseRestDTO mapTreatmentToResponseDTO(Treatment treatment) {
        TreatmentResponseRestDTO responseDTO = new TreatmentResponseRestDTO();
        responseDTO.setId(treatment.getId());
        responseDTO.setName(treatment.getName());
        responseDTO.setPrice(treatment.getPrice());
        return responseDTO;
    }
}
