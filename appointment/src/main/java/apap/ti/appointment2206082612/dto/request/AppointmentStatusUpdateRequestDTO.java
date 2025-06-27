package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusUpdateRequestDTO {

    @NotNull(message = "Appointment ID must not be null")
    @NotEmpty(message = "Appointment ID must not be empty")
    private String id;

    @NotNull(message = "Status must not be null")
    @Min(value = 1, message = "Status must be at least 1 (Done)")
    @Max(value = 2, message = "Status must be at most 2 (Cancelled)")
    private Integer status; // 1: Done, 2: Cancelled
}
