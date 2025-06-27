package apap.ti.hospitalization2206082770.service;

import apap.ti.hospitalization2206082770.model.Facility;
import apap.ti.hospitalization2206082770.repository.FacilityDb;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityDb facilityDb;

    public FacilityServiceImpl(FacilityDb facilityDb) {
        this.facilityDb = facilityDb;
    }

    @Override
    public Facility createFacility(Facility facility) {
        return facilityDb.save(facility);
    }

    
    @Override
    public List<Facility> getAllFacilities() {
        List<Facility> facilities = facilityDb.findAll();
        return facilities;
    }

}
