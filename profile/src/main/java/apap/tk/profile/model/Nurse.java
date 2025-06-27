package apap.tk.profile.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "nurse")
public class Nurse extends apap.tk.profile.model.EndUser {

}
