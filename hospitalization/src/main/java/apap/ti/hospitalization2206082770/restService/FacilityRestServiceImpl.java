package apap.ti.hospitalization2206082770.restService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206082770.model.Facility; // Pastikan menggunakan model Facility
import apap.ti.hospitalization2206082770.repository.FacilityDb;
import apap.ti.hospitalization2206082770.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;

@Service
public class FacilityRestServiceImpl implements FacilityRestService {

    private final FacilityDb facilityDb;

    public FacilityRestServiceImpl(FacilityDb facilityDb) {
        this.facilityDb = facilityDb;
    }

    @Override
    public List<FacilityResponseDTO> getAllFacility() {
        var listFacilities = facilityDb.findAll();
        var listFacilitiesResponseDTO = new ArrayList<FacilityResponseDTO>();
        
        listFacilities.forEach(facility -> {
            var facilityResponseDTO = facilityToFacilityResponseDTO(facility);
            listFacilitiesResponseDTO.add(facilityResponseDTO);
        });

        return listFacilitiesResponseDTO;
    }

    // Metode untuk mengonversi Facility ke FacilityResponseDTO
    private FacilityResponseDTO facilityToFacilityResponseDTO(Facility facility) {
        var facilityResponseDTO = new FacilityResponseDTO();
        facilityResponseDTO.setId(facility.getId());
        facilityResponseDTO.setName(facility.getName());
        facilityResponseDTO.setFee(facility.getFee());
        facilityResponseDTO.setCreatedDate(facility.getCreatedDate());
        facilityResponseDTO.setUpdatedDate(facility.getUpdatedDate());
        facilityResponseDTO.setDeleted(facility.isDeleted());
        facilityResponseDTO.setDeletedAt(facility.getDeletedAt());

        // Mengonversi data reservations jika ada
        if (facility.getListReservations() != null) {
            var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();
            facility.getListReservations().forEach(reservation -> {
                var reservationResponseDTO = new ReservationResponseDTO();
                reservationResponseDTO.setId(reservation.getId());
                reservationResponseDTO.setTotalFee(reservation.getTotalFee());
                reservationResponseDTO.setDateIn(reservation.getDateIn());
                reservationResponseDTO.setDateOut(reservation.getDateOut());
                reservationResponseDTO.setPatientId(reservation.getPatientId());
                reservationResponseDTO.setNurse(reservation.getNurse());
                reservationResponseDTO.setCreatedDate(reservation.getCreatedDate());
                reservationResponseDTO.setUpdatedDate(reservation.getUpdatedDate());
                listReservationResponseDTO.add(reservationResponseDTO);
            });
            facilityResponseDTO.setListReservations(listReservationResponseDTO);
        }

        return facilityResponseDTO;
    }
}
