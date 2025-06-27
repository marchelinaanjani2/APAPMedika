package apap.ti.hospitalization2206082770.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private String id;
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
    private Date date;
    private String diagnosis;
    private long totalFee;
    private int status; // 0: Created, 1: Done, 2: Cancelled
    private Date createdAt;
    private Date updatedAt;
}
