// src/main/java/apap/ti/appointment2206082612/dto/request/CreateAppointmentWithPatientDTO.java
package apap.ti.appointment2206082612.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateAppointmentWithPatientDTO {

    @Valid
    private CreatePatientDTO patient;

    @NotNull
    private String doctorId;

    @NotNull
    private Date appointmentDate;

    private List<Long> treatmentIds;

    @NotNull
    private long totalFee;
}
