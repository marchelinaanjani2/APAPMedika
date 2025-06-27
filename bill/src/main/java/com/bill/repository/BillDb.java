package com.bill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bill.model.Bill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillDb extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientId(UUID patientId);
    List<Bill> findAllByIsDeletedFalse();
    Bill findByAppointmentId(String appointmentId);
    Optional<Bill> findByAppointmentIdAndIsDeletedFalse(String appointmentId);
    List<Bill> findByPatientIdAndIsDeletedFalse(UUID patientId);

}
