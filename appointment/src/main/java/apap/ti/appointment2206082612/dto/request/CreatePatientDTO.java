// src/main/java/apap/ti/appointment2206082612/dto/request/CreatePatientDTO.java
package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreatePatientDTO {

    @NotNull
    @Size(max = 16)
    private String nik;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private boolean gender; // true: female, false: male

    @NotNull
    @Email
    private String email;

    @NotNull
    private Date birthDate;

    @NotNull
    @Size(max = 50)
    private String birthPlace;
}
