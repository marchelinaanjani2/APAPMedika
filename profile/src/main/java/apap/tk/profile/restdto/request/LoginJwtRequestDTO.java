package apap.tk.profile.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginJwtRequestDTO {
    private String email;
    private String password;
}
