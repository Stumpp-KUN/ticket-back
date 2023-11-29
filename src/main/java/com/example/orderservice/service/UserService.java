package com.example.orderservice.service;

import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.exception.EntityNotFoundException;

public interface UserService {
    UserDTO getById(Long id) throws EntityNotFoundException;
    UserDTO getByUserEmail(String userEmail) throws EntityNotFoundException;
    UserDTO getManager() throws EntityNotFoundException;
    UserDTO getEngineer() throws EntityNotFoundException;
}
