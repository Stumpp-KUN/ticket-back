package com.example.orderservice.service.impl;

import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.enums.Role;
import com.example.orderservice.entity.User;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.mapper.UserMapper;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO getById(Long id) throws EntityNotFoundException {
        log.info("Getting user with id {}",id);

        return entityToDto(userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("There is not user with id "+id)));
    }

    @Override
    public UserDTO getByUserEmail(String userEmail) throws EntityNotFoundException {
        log.info("Getting user with email {}",userEmail);

        return entityToDto(userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("There is not user with email "+userEmail)));
    }

    @Override
    public UserDTO getManager() throws EntityNotFoundException {
        log.info("Getting random manager to assign ticket");

        return entityToDto(userRepository.findUserByRole(Role.MANAGER)
                .orElseThrow(()->new EntityNotFoundException("No managers")));
    }

    @Override
    public UserDTO getEngineer() throws EntityNotFoundException {
        log.info("Getting random engineer to approve ticket");

        return entityToDto(userRepository.findUserByRole(Role.ENGINEER)
                .orElseThrow(()->new EntityNotFoundException("No engineers")));
    }

    private UserDTO entityToDto(User user){
        return userMapper.fromEntity(user);
    }
}
