package apap.tk.profile.restdto.request;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean gender;
    private String role;

    //jika Patient
    private String nik;
    private String birthPlace;
    private Date birthDate;
    private int pClass;

    //jika Doctor
    private int specialist;
    private int yearsOfExperience;
    private Long fee;
    private List<Integer> schedules;
}
