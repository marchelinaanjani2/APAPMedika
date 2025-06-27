package apap.ti.appointment2206082612.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTreatmentRequestDTO {
    
    @NotNull
    private Long id; // ID treatment yang akan diperbarui

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private long price;

    private List<String> appointments; // Optional, bisa dihilangkan atau diganti sesuai kebutuhan
}
