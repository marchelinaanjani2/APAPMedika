package apap.ti.appointment2206082612.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDTO {
    private UUID id;
    private String policyId; // Nullable
    private String appointmentId; // Nullable
    private String reservationId; // Nullable
    private UUID patientId;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
}

