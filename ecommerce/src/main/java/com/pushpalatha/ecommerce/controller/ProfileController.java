package com.pushpalatha.ecommerce.controller;

import com.pushpalatha.ecommerce.dto.ChangePasswordRequest;
import com.pushpalatha.ecommerce.dto.ProfileUpdateRequest;
import com.pushpalatha.ecommerce.dto.UserResponse;
import com.pushpalatha.ecommerce.security.UserPrincipal;
import com.pushpalatha.ecommerce.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public UserResponse getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return profileService.getProfile(principal.getUser());
    }

    @PutMapping
    public UserResponse updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return profileService.updateProfile(principal.getUser(), request);
    }

    @PutMapping("/password")
    public void changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        profileService.changePassword(principal.getUser(), request);
    }
}
