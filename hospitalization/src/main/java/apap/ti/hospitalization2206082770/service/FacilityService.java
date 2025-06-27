package apap.ti.hospitalization2206082770.service;

import java.util.List;
import apap.ti.hospitalization2206082770.model.Facility;


public interface FacilityService {
    Facility createFacility(Facility facility);
    List<Facility> getAllFacilities();
}
