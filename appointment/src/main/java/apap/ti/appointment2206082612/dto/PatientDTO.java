// PatientDTO.java
package apap.ti.appointment2206082612.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PatientDTO {
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
