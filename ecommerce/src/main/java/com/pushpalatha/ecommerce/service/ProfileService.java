package com.pushpalatha.ecommerce.service;

import com.pushpalatha.ecommerce.dto.ChangePasswordRequest;
import com.pushpalatha.ecommerce.dto.ProfileUpdateRequest;
import com.pushpalatha.ecommerce.dto.UserResponse;
import com.pushpalatha.ecommerce.entity.AuthProvider;
import com.pushpalatha.ecommerce.entity.User;
import com.pushpalatha.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponse getProfile(User user) {
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(User user, ProfileUpdateRequest request) {
        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(BAD_REQUEST, "Email already in use");
                });

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new ResponseStatusException(BAD_REQUEST, "Password changes are only available for local accounts");
        }
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(BAD_REQUEST, "Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
