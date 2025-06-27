package com.bill.service;

import java.util.List;

import com.bill.restdto.response.AppointmentResponseDTO;

public interface AppointmentService {
    List<AppointmentResponseDTO> getAllApointment(); 
}
