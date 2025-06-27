// src/main/java/apap/ti/appointment2206082612/restdto/request/CreateAppointmentWithPatientRequestRestDTO.java
package apap.ti.appointment2206082612.restdto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateAppointmentWithPatientRequestRestDTO {

    @Valid
    private CreatePatientRequestRestDTO patient;

    @NotNull
    private String doctorId;

    @NotNull
    private Date appointmentDate;

    private List<Long> treatmentIds;

    @NotNull
    private long totalFee;
}
