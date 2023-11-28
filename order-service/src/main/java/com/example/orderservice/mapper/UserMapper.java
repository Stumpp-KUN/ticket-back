package com.example.orderservice.mapper;

import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserMapper {

    public abstract User toEntity(UserDTO userDTO);

    public abstract UserDTO fromEntity(User user);
}
