package apap.ti.insurance2206827592.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class UpdatePolicyRequestDTO extends AddPolicyRequestDTO {

    @NotBlank(message = "Policy ID must not be empty.")
    private String idPolicy;
}