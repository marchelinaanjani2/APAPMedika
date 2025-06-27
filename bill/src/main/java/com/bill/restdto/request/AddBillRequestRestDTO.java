package com.bill.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBillRequestRestDTO {
    private UUID id;
    private String policyId; // Nullable
    private String appointmentId; // Nullable
    private double appointmentFee; // Nullable
    private String prescriptionId;
    private long prescriptionFee;
    private String reservationId; // Nullable
    private double reservationFee; // Nullable
    private UUID patientId;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
    private double subTotal;

    private double totalAmountDue;




}
