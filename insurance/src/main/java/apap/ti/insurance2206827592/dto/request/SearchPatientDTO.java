package apap.ti.insurance2206827592.dto.request;

import jakarta.validation.constraints.NotNull;

public class SearchPatientDTO {

    @NotNull(message = "NIK must not be empty.")
    private String nik;
}
