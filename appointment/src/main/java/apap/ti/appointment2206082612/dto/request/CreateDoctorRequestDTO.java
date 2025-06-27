package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDoctorRequestDTO {

    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name must not be empty")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @NotNull(message = "Email must not be null")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @NotNull(message = "Specialization must not be null")
    private int specialist; // Menggunakan 'int'

    @NotNull(message = "Gender must not be null")
    private boolean gender; // Menggunakan 'boolean'

    @NotNull(message = "Years of experience must not be null")
    @Min(value = 0, message = "Years of experience must be at least 0")
    private int yearsOfExperience;

    @NotNull(message = "Fee must not be null")
    @Min(value = 0, message = "Fee must be at least 0")
    private long fee;

    @NotNull(message = "Schedules must not be null")
    @Size(min = 1, message = "At least one schedule day must be selected")
    private List<Integer> schedules = new ArrayList<>(); // Menggunakan 'List<Integer>'
}
