package apap.ti.appointment2206082612.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseRestDTO {
    private String id;
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String diagnosis;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TreatmentResponseRestDTO> treatments;
    private long totalFee;
    private int status; // 0: Created, 1: Done, 2: Cancelled
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;
}
