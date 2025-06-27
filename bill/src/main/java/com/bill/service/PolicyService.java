package com.bill.service;

import java.util.List;

import com.bill.restdto.response.AppointmentResponseDTO;
import com.bill.restdto.response.PolicyResponseDTO;

public interface PolicyService {
    PolicyResponseDTO getPolicyById(String id); 
}
