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
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {

    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "contact", nullable = false)
    private String contact;

    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;

    @ManyToMany
    @JoinTable(name = "company_coverage", joinColumns = @JoinColumn(name = "id_company"),
            inverseJoinColumns = @JoinColumn(name = "id_coverage"))
    List<Coverage> listCoverage;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    public String showCoverage() {
        if (listCoverage == null || listCoverage.isEmpty()) {
            return "-";
        }

        StringBuilder coverageString = new StringBuilder();
        for (int i = 0; i < listCoverage.size(); i++) {
            coverageString.append(listCoverage.get(i).getName());
            if (i < listCoverage.size() - 1) {
                coverageString.append(", ");
            }
        }

        return coverageString.toString();
    }

    public String calculateTotalCoverage() {
        int total = 0;

        for (Coverage coverage: listCoverage) {
           total += coverage.getCoverageAmount();
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(total);
    }

    public Long calculateTotalCoverageLong() {
        Long total = 0L;

        for (Coverage coverage: listCoverage) {
            total += coverage.getCoverageAmount();
        }

        return total;
    }

    public List<Long> getListCoverageId() {
        List<Long> result = new ArrayList<>();

        for (Coverage coverage: listCoverage) {
            result.add(coverage.getId());
        }

        return result;
    }
}
