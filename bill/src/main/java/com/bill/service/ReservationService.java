package com.bill.service;

import java.util.List;

import com.bill.restdto.response.ReservationResponseDTO;

import jakarta.persistence.EntityNotFoundException;

public interface ReservationService {
    List<ReservationResponseDTO> getAllReservations();
    ReservationResponseDTO getReservationById(String id) throws EntityNotFoundException;
}
