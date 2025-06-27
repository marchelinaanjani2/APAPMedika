package apap.ti.insurance2206827592.restdto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPolicyRequestRestDTO {

    @NotNull
    private UUID companyId;

    @NotNull
    private UUID patientId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Expiry Date must not be empty")
    private Date expiryDate;

}
