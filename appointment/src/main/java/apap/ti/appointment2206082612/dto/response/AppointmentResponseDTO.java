package apap.ti.appointment2206082612.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private String id;
    private DoctorResponseDTO doctor;
    private PatientResponseDTO patient;
    private Date date;
    private String diagnosis;
    private List<TreatmentResponseDTO> treatments;
    private long totalFee;
    private int status; // 0: Created, 1: Done, 2: Cancelled
    private Date createdAt;
    private Date updatedAt;
}
