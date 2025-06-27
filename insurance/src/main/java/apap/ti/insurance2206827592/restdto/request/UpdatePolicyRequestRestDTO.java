package apap.ti.insurance2206827592.restdto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePolicyRequestRestDTO extends AddPolicyRequestRestDTO {
    @NotNull
    private String policyId;
}
