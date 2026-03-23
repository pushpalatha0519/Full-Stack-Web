package com.pushpalatha.ecommerce.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
