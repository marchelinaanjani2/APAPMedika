package apap.ti.appointment2206082612.model;

import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLRestriction("deleted_at IS NULL")
@Table(name = "appointment")
public class Appointment {

    @Id
    @Column(name = "id", length = 10, nullable = false, unique = true)
    private String id; // Custom ID sesuai spesifikasi

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    private String doctorId; // Storing only the doctor ID

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private UUID patientId; // Storing only the patient ID

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private Date date;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @ManyToMany
    @JoinTable(
        name = "appointment_treatment",
        joinColumns = @JoinColumn(name = "appointment_id"),
        inverseJoinColumns = @JoinColumn(name = "treatment_id")
    )
    private List<Treatment> treatments;

    @NotNull
    @Column(name = "total_fee", nullable = false)
    private long totalFee;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status; // 0: Created, 1: Done, 2: Cancelled

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
