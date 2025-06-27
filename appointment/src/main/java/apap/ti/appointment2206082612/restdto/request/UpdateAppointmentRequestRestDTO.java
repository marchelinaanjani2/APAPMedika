package apap.ti.appointment2206082612.restdto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentRequestRestDTO {

    @NotNull(message = "Appointment ID must not be null")
    @NotEmpty(message = "Appointment ID must not be empty")
    private String id;

    @Size(max = 1000, message = "Diagnosis must be at most 1000 characters")
    private String diagnosis;

    private List<Long> treatmentIds;
}
