package com.pushpalatha.ecommerce.dto;

import com.pushpalatha.ecommerce.entity.AuthProvider;
import com.pushpalatha.ecommerce.entity.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        String fullName,
        String phoneNumber,
        String address,
        Role role,
        AuthProvider provider
) {
}
