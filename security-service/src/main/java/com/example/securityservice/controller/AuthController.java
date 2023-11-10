package com.example.securityservice.controller;

import com.example.securityservice.entity.User;
import com.example.securityservice.entity.jwt.JwtRequest;
import com.example.securityservice.entity.jwt.JwtResponse;
import com.example.securityservice.entity.jwt.RefreshJwtRequest;
import com.example.securityservice.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid JwtRequest jwtRequest) throws AuthException {
        return new ResponseEntity<>(authService.email(jwtRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody @Valid RefreshJwtRequest request) throws AuthException {
        return new ResponseEntity<>(authService.refresh(request.getRefreshToken()),HttpStatus.OK);
    }


}
