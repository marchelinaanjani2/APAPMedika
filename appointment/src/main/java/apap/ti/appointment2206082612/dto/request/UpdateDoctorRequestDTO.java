package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDoctorRequestDTO extends CreateDoctorRequestDTO {
    @NotNull(message = "ID dokter tidak boleh kosong")
    private String id; // ID unik dokter
}
