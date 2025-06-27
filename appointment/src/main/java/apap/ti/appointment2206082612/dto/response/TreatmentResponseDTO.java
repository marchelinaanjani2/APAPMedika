package apap.ti.appointment2206082612.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentResponseDTO {
    private Long id;
    private String name;
    private long price;
    private Date createdAt;
    private Date updatedAt;
}
