package apap.ti.insurance2206827592.model;

import com.github.javafaker.Bool;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "policy")
public class Policy {

    @Id
    private String id;

    @NotNull
    @Column(name = "companyId", nullable = false)
    private UUID companyId;

    @NotNull
    @Column(name = "patientId", nullable = false)
    private UUID patientId;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private Date expiryDate;

    @NotNull
    @Column(name = "total_coverage")
    private Long totalCoverage;

    @NotNull
    @Column(name = "total_covered")
    private Long totalCovered;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @NotNull
    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany
    @JoinTable(name = "used_coverage_policy", joinColumns = @JoinColumn(name = "id_policy"),
            inverseJoinColumns = @JoinColumn(name = "id_coverage"))
    List<Coverage> listCoverageUsed;
}
