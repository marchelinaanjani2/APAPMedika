package com.bill.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolicyResponseDTO {
    private String id;
    private UUID patient;
    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private Long totalCoverage;
    private Long totalCovered;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;

    private String createdBy;
    private String updatedBy;

    private List<Long> listIdCoverageUsed;
}
