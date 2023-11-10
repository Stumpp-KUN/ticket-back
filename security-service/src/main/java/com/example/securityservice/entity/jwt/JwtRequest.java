package com.example.securityservice.entity.jwt;

import com.example.securityservice.validator.ValidEmail;
import com.example.securityservice.validator.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

}
