package apap.ti.insurance2206827592.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coverage")
public class Coverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "coverage_amount", nullable = false)
    private Long coverageAmount;

    @ManyToMany(mappedBy = "listCoverage", fetch = FetchType.LAZY)
    private List<Company> listCompany;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToMany(mappedBy = "listCoverageUsed", fetch = FetchType.LAZY)
    private List<Policy> listPolicy;

    public String coverageAmountToString() {

        Long result = coverageAmount;
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        return format.format(result);
    }
}
