package apap.tk.profile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor extends apap.tk.profile.model.EndUser {
    
    @NotNull
    @Column(name = "specialist", nullable = false)
    private int specialist;

    @NotNull
    @Column(name = "year_of_experience", nullable = false)
    private int yearsOfExperience;

    @NotNull
    @Column(name = "fee", nullable = false)
    private long fee;

    @ElementCollection
    @CollectionTable(name = "doctor_schedule", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "schedule")
    private List<Integer> schedules;

    public String getSpecializationCode(){
        return switch(this.specialist) {
            case 0 -> "UMM";
            case 1 -> "PDL";
            case 2 -> "GGI";
            case 3 -> "PRU";
            case 4 -> "ANK";
            case 5 -> "THT";
            case 6 -> "BDH";
            case 7 -> "KSJ";
            case 8 -> "PRE";
            case 9 -> "ANS";
            case 10 -> "JPD";
            case 11 -> "NRO";
            case 12 -> "KKL";
            case 13 -> "URO";
            case 14 -> "MTA";
            case 15 -> "OBG";
            case 16 -> "RAD";
            default -> "UNK"; // Unknown
        };
    }

    public List<String> getScheduleDays() {
        List<String> days = new ArrayList<>();
        for (Integer day : this.schedules) {
            days.add(mapDayNumberToName(day));
        }
        return days;
    }

    private String mapDayNumberToName(Integer day) {
        return switch (day) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Unknown";
        };
    }

}
