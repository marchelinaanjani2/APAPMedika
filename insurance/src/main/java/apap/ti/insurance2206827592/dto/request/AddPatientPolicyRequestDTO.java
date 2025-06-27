package apap.ti.insurance2206827592.dto.request;

import apap.ti.insurance2206827592.model.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPatientPolicyRequestDTO {

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Please choose a Gender.")
    private int gender;

    @NotBlank(message = "Expiry date  must not be empty.")
    private String expiryDate;

    @NotBlank(message = "NIK must not be empty.")
    private String nik;

    @NotBlank(message = "Date of Birth must not be empty.")
    private String birthDate;

    @NotBlank(message = "Email must not be empty.")
    private String email;

    @NotNull(message = "Please choose Insurance Class.")
    private int piClass;

    @NotNull(message = "Company must not be empty.")
    private Company company;
}
