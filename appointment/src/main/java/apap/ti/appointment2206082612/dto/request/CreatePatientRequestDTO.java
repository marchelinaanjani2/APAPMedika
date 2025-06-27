package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequestDTO {

    @NotNull(message = "NIK must not be null")
    @NotEmpty(message = "NIK must not be empty")
    @Size(max = 16, message = "NIK must be at most 16 characters")
    private String nik;

    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name must not be empty")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @NotNull(message = "Gender must not be null")
    private Boolean gender; // Using Boolean to allow @NotNull

    @NotNull(message = "Email must not be null")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Birth date must not be null")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Menambahkan anotasi untuk format yang sesuai
    private Date birthDate;

    @NotNull(message = "Birth place must not be null")
    @NotEmpty(message = "Birth place must not be empty")
    @Size(max = 50, message = "Birth place must be at most 50 characters")
    private String birthPlace;
}
