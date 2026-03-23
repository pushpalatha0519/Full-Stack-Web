package com.pushpalatha.ecommerce.service;

import com.pushpalatha.ecommerce.dto.AuthRequest;
import com.pushpalatha.ecommerce.dto.AuthResponse;
import com.pushpalatha.ecommerce.dto.RegisterRequest;
import com.pushpalatha.ecommerce.entity.AuthProvider;
import com.pushpalatha.ecommerce.entity.Role;
import com.pushpalatha.ecommerce.entity.User;
import com.pushpalatha.ecommerce.repository.UserRepository;
import com.pushpalatha.ecommerce.security.JwtService;
import com.pushpalatha.ecommerce.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);
        UserPrincipal principal = new UserPrincipal(savedUser);
        return new AuthResponse(jwtService.generateToken(principal), userMapper.toResponse(savedUser));
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Invalid credentials"));
        UserPrincipal principal = new UserPrincipal(user);
        return new AuthResponse(jwtService.generateToken(principal), userMapper.toResponse(user));
    }
}
