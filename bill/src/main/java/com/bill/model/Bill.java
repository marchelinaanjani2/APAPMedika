package com.bill.model;


import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "policy_id", nullable = true) 
    private String policyId;

    @Column(name = "appointment_id", nullable = true) 
    private String appointmentId;

    private long appointmentFee;

    @Column(name = "prescription_id", nullable = true) 
    private String prescriptionId;

    private long prescriptionFee;

    @Column(name = "reservation_id", nullable = true) 
    private String reservationId;

    private double reservationFee;

    @Column(name = "patient_id", nullable = false) 
    private UUID patientId;

    @Column(name = "status", nullable = false) 
    private int status;
    // 0: Treatment In Progress
    // 1: Unpaid
    // 2: Paid

    private double subTotal;

    private double totalAmountDue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false) 
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true) 
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = true) 
    private String createdBy;

    @Column(name = "updated_by", nullable = true) 
    private String updatedBy;

    @Column(name = "is_deleted", nullable = true) 
    private boolean isDeleted;

}
