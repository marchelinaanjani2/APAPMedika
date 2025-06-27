package apap.ti.insurance2206827592.dto.request;

import apap.ti.insurance2206827592.model.Coverage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCompanyRequestDTO {

    @NotNull(message = "Nama perusahaan harus diisi.")
    private String name;

    @NotNull(message = "Alamat perusahaan harus diisi.")
    private String address;

    @NotNull(message = "Kontak perusahaan harus diisi.")
    private String contact;

    @NotNull(message = "Email perusahaan harus diisi.")
    private String email;

    @NotNull(message = "Coverage perusahaan harus diisi.")
    private List<Coverage> listCoverage;
}
