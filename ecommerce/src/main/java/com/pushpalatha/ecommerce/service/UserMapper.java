package com.pushpalatha.ecommerce.service;

import com.pushpalatha.ecommerce.dto.UserResponse;
import com.pushpalatha.ecommerce.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getRole(),
                user.getProvider()
        );
    }
}
