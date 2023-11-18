package com.example.orderservice.service;

import com.example.orderservice.entity.Role;
import com.example.orderservice.entity.User;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User getById(Long id) throws EntityNotFoundException {
        log.info("Getting user with id {}",id);

        return userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("There is not user with id "+id));
    }

    public User getByUserEmail(String userEmail) throws EntityNotFoundException {
        log.info("Getting user with email {}",userEmail);

        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("There is not user with email "+userEmail));
    }

    public User getManager() throws EntityNotFoundException {
        log.info("Getting random manager to assign ticket");

        return userRepository.findUserByRole(Role.MANAGER)
                .orElseThrow(()->new EntityNotFoundException("No managers"));
    }

    public User getEngineer() throws EntityNotFoundException {
        log.info("Getting random engineer to approve ticket");

        return userRepository.findUserByRole(Role.ENGINEER)
                .orElseThrow(()->new EntityNotFoundException("No engineers"));
    }
}
