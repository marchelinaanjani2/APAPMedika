package apap.ti.appointment2206082612.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private UUID id;
    private String nik;
    private String name;
    private boolean gender;
    private String email;
    private Date birthDate;
    private String birthPlace;
    private Date createdAt;
    private Date updatedAt;
}
