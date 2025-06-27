package apap.ti.hospitalization2206082770.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date; 
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomResponseDTO {
    private String id;

    private String name;

    private String description;

    private int maxCapacity; 

    private double pricePerDay; 

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedDate;

    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date deletedAt;

    private List<ReservationResponseDTO> listReservations; 
}
