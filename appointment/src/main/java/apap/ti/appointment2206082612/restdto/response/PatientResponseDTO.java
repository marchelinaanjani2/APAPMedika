package apap.ti.appointment2206082612.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;
    private boolean isDeleted;

    private String nik;
    private String birthPlace;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private int pClass;
    private List<String> appointments;
}
