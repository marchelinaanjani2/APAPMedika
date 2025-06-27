package apap.ti.insurance2206827592.dto.request;

import apap.ti.insurance2206827592.model.Company;
import apap.ti.insurance2206827592.model.Coverage;
import apap.ti.insurance2206827592.model.Policy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UpgradeClassRequestDTO {

    @NotNull(message = "ID must not be empty.")
    private UUID id;

    @NotNull(message = "NIK must not be empty.")
    private String nik;

    @NotNull(message = "Name must not be empty.")
    private String name;

    @NotNull(message = "Please choose a Gender.")
    private int gender;

    @NotBlank(message = "Date of Birth must not be empty.")
    private String birthDate;

    @NotBlank(message = "Email must not be empty.")
    private String email;

    @NotNull(message = "Please choose Insurance Class.")
    private int piClass;

    private List<Policy> listPolicy;
}