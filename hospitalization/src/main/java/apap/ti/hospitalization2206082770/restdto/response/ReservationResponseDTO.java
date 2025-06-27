package apap.ti.hospitalization2206082770.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import apap.ti.hospitalization2206082770.model.Room;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date; 
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationResponseDTO {

    private String id;

    private double totalFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date dateIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date dateOut;

    @NotNull 
    private UUID patientId;

    private UUID nurse;

    private List<FacilityResponseDTO> facilities;

    private Room roomId;
    private String status;
    private String namaPatient;
    private String appointmentId;
    private String namaNurse;
    private String email;
    private boolean gender;
    private String roomName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedDate;

    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date deletedAt;
}
