package com.example.orderservice.repository;

import com.example.orderservice.entity.Role;
import com.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByRole(Role role);
}
