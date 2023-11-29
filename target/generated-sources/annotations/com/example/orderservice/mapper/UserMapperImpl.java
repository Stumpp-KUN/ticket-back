package com.example.orderservice.mapper;

import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.dto.UserDTO.UserDTOBuilder;
import com.example.orderservice.entity.User;
import com.example.orderservice.entity.User.UserBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-29T10:30:53+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.firstName( userDTO.getFirstName() );
        user.lastName( userDTO.getLastName() );
        user.role( userDTO.getRole() );
        user.email( userDTO.getEmail() );

        return user.build();
    }

    @Override
    public UserDTO fromEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.firstName( user.getFirstName() );
        userDTO.lastName( user.getLastName() );
        userDTO.role( user.getRole() );
        userDTO.email( user.getEmail() );

        return userDTO.build();
    }
}
