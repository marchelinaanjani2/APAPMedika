package apap.ti.appointment2206082612.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequestDTO {
    
    @NotNull
    private UUID id; // ID pasien yang akan diperbarui

    @NotNull
    @Size(max = 16)
    private String nik;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private boolean gender;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Date birthDate;

    @NotNull
    @Size(max = 50)
    private String birthPlace;
}
