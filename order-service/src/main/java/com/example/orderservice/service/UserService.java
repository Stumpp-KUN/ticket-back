package com.example.orderservice.service;

import com.example.orderservice.entity.User;
import com.example.orderservice.exception.EntityNotFoundException;

public interface UserService {
    User getById(Long id) throws EntityNotFoundException;

    User getByUserEmail(String userEmail) throws EntityNotFoundException;

    User getManager() throws EntityNotFoundException;

    User getEngineer() throws EntityNotFoundException;

}
