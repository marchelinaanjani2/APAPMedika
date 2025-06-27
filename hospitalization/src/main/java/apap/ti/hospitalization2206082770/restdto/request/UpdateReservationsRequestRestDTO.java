package apap.ti.hospitalization2206082770.restdto.request;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;


@NoArgsConstructor
@Data
public class UpdateReservationsRequestRestDTO extends AddReservationsRequestRestDTO {
    // No need for @AllArgsConstructor as the superclass already provides it
}

