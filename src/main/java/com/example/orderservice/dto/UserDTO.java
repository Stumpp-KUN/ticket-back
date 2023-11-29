package com.example.orderservice.dto;

import com.example.orderservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private Role role;

    private String email;

}
