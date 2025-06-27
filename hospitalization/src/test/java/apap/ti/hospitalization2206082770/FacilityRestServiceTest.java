package apap.ti.hospitalization2206082770;

import apap.ti.hospitalization2206082770.model.Facility;
import apap.ti.hospitalization2206082770.model.Reservation;
import apap.ti.hospitalization2206082770.repository.FacilityDb;
import apap.ti.hospitalization2206082770.restService.FacilityRestService;
import apap.ti.hospitalization2206082770.restService.FacilityRestServiceImpl;
import apap.ti.hospitalization2206082770.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacilityRestServiceTest {

    private FacilityDb facilityDb;
    private FacilityRestServiceImpl facilityRestService;

    @BeforeEach
    public void setUp() {
        facilityDb = mock(FacilityDb.class);
        facilityRestService = new FacilityRestServiceImpl(facilityDb);
    }

    @Test
    public void testGetAllFacility_Success() {
        // Given
        List<Facility> mockFacilities = new ArrayList<>();
        Facility facility1 = new Facility();
        facility1.setId(UUID.randomUUID());
        facility1.setName("Facility 1");
        facility1.setFee(100.0);
        facility1.setCreatedDate(new Date());
        facility1.setUpdatedDate(new Date());
        facility1.setDeleted(false);
    
        Facility facility2 = new Facility();
        facility2.setId(UUID.randomUUID());
        facility2.setName("Facility 2");
        facility2.setFee(150.0);
        facility2.setCreatedDate(new Date());
        facility2.setUpdatedDate(new Date());
        facility2.setDeleted(false);
    
        mockFacilities.add(facility1);
        mockFacilities.add(facility2);
    
        // Mock the facilityDb.findAll() method to return mockFacilities
        when(facilityDb.findAll()).thenReturn(mockFacilities);

        // Call service method
        List<FacilityResponseDTO> result = facilityRestService.getAllFacility();
    
        assertNotNull(result);
        assertEquals(2, result.size());

        FacilityResponseDTO responseDTO1 = result.get(0);
        assertEquals(facility1.getId(), responseDTO1.getId());
        assertEquals("Facility 1", responseDTO1.getName());
        assertEquals(100.0, responseDTO1.getFee());
        assertFalse(responseDTO1.isDeleted());
        assertEquals(1, responseDTO1.getListReservations().size());


        FacilityResponseDTO responseDTO2 = result.get(1);
        assertEquals(facility2.getId(), responseDTO2.getId());
        assertEquals("Facility 2", responseDTO2.getName());
        assertEquals(150.0, responseDTO2.getFee());
        assertFalse(responseDTO2.isDeleted());
        assertTrue(responseDTO2.getListReservations().isEmpty());

    
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
   
    
        // Verify interaction with the mocked facilityDb
        verify(facilityDb, times(1)).findAll();
    }
    
    

}
