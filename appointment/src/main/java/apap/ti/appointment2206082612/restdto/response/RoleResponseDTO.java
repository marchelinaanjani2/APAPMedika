package apap.ti.appointment2206082612.restdto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleResponseDTO {
    private long id;
    private String role;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EndUserResponseDTO> users;
}
