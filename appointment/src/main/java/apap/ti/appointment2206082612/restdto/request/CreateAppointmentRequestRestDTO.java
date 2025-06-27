package apap.ti.appointment2206082612.restdto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentRequestRestDTO {

    @NotNull(message = "Doctor ID must not be null")
    @NotEmpty(message = "Doctor ID must not be empty")
    private String doctorId;

    @NotNull(message = "Patient ID must not be null")
    @NotEmpty(message = "Patient ID must not be empty")
    private String patientId;

    @NotNull(message = "Date must not be null")
    @FutureOrPresent(message = "Date must be in the present or future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
