package apap.ti.appointment2206082612.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTreatmentRequestDTO {
    
    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private long price;
}
