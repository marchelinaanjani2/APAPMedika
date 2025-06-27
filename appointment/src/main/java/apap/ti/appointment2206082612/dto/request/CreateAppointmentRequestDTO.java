package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentRequestDTO {

    @NotNull(message = "Doctor ID must not be null")
    @NotEmpty(message = "Doctor ID must not be empty")
    private String doctorId;

    private UUID patientId;

    @NotNull(message = "Date must not be null")
    @FutureOrPresent(message = "Date must be in the present or future")
    private Date date;
}
