package com.example.securityservice.service;

import com.example.securityservice.entity.User;
import com.example.securityservice.exception.AuthException;
import com.example.securityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByEmail(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(()->new AuthException
                        ("Please make sure you are using a valid email or password"));
    }
}
