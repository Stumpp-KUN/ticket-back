package com.example.securityservice.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    EMPLOYEE("EMPLOYEE"),
    MANAGER("MANAGER"),
    ENGINEER("ENGINEER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }

}