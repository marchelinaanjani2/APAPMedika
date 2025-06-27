package apap.ti.hospitalization2206082770.restdto.request;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import apap.ti.hospitalization2206082770.config.json.FacilityDeserializer;
import apap.ti.hospitalization2206082770.model.Facility;

import apap.ti.hospitalization2206082770.model.Room;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddReservationsRequestRestDTO {
    private String id;

    @NotNull(message = "Total fee tidak boleh kosong")
    private double totalFee;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Tanggal masuk tidak boleh kosong")
    @PastOrPresent(message = "Tanggal masuk maksimal adalah hari ini")
    private Date dateIn;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Tanggal keluar tidak boleh kosong")
    @FutureOrPresent(message = "Tanggal keluar minimal adalah hari ini")
    private Date dateOut;

    @NotNull(message = "Pasien tidak boleh kosong")
    private UUID patient;

    @NotNull(message = "Perawat tidak boleh kosong")
    private UUID nurse;
    
    @JsonDeserialize(contentUsing = FacilityDeserializer.class)
    private List<Facility> facilities; 

    @NotNull(message = "Kamar tidak boleh kosong")
    private Room room;
    private String appointmentId;
    private String nikPatient;
    private Date createdDate;
    private String status;
    


}

