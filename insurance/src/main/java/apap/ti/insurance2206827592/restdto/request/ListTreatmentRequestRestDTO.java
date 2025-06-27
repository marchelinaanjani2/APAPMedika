package apap.ti.insurance2206827592.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListTreatmentRequestRestDTO {
    List<Long> listIdTreatment;
}
