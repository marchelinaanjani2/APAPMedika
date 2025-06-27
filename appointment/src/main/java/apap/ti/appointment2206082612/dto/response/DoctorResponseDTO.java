package apap.ti.appointment2206082612.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private String id;
    private String name;
    private String email;
    private int specialist;
    private boolean gender; // true: female, false: male
    private int yearsOfExperience;
    private List<Integer> schedules;
    private long fee;
}
