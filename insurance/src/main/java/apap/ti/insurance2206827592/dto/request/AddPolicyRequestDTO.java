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
public class AddPolicyRequestDTO {

    @NotBlank(message = "Expiry date  must not be empty.")
    private String expiryDate;

    @NotNull(message = "Company must not be empty.")
    private Company company;

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Please choose Insurance Class.")
    private int pClass;

    @NotBlank(message = "NIK must not be empty.")
    private String nik;

    private Long totalCoverage;

    private Long totalCovered;
}
