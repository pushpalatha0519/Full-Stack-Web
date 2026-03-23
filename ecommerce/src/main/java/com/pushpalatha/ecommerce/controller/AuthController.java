package com.pushpalatha.ecommerce.controller;

import com.pushpalatha.ecommerce.dto.AuthRequest;
import com.pushpalatha.ecommerce.dto.AuthResponse;
import com.pushpalatha.ecommerce.dto.RegisterRequest;
import com.pushpalatha.ecommerce.dto.UserResponse;
import com.pushpalatha.ecommerce.security.UserPrincipal;
import com.pushpalatha.ecommerce.service.AuthService;
import com.pushpalatha.ecommerce.service.UserMapper;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse currentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return userMapper.toResponse(principal.getUser());
    }
}
