package apap.ti.insurance2206827592.restdto.response;

import apap.ti.insurance2206827592.model.Company;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoverageResponseDTO {
    private Long id;
    private String name;
    private Long coverageAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> listCompany;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
}
