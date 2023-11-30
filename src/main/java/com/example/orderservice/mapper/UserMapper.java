package com.example.orderservice.mapper;

import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO fromEntity(User user);
}
