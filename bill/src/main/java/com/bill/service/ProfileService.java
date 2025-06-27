package com.bill.service;

import java.util.List;
import java.util.UUID;

import com.bill.restdto.response.EndUserResponseDTO;
import jakarta.persistence.EntityNotFoundException;

public interface ProfileService {
    EndUserResponseDTO getCurrentUser(String token);
}