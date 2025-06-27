package apap.ti.insurance2206827592.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCompanyRequestDTO extends AddCompanyRequestDTO {

    @NotNull(message = "UUID harus diisi.")
    private UUID id;
}
