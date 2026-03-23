package com.pushpalatha.ecommerce.security;

import com.pushpalatha.ecommerce.entity.AuthProvider;
import com.pushpalatha.ecommerce.entity.Role;
import com.pushpalatha.ecommerce.entity.User;
import com.pushpalatha.ecommerce.repository.UserRepository;
import java.util.UUID;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = oauth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not provided by identity provider");
        }

        String displayName = oauth2User.getAttribute("name");
        final String fullName = (displayName == null || displayName.isBlank()) ? email : displayName;

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createOAuthUser(email, fullName, registrationId));

        return new DefaultOAuth2User(
                java.util.List.of(new SimpleGrantedAuthority(user.getRole().name())),
                oauth2User.getAttributes(),
                "email"
        );
    }

    private User createOAuthUser(String email, String fullName, String registrationId) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setUsername(buildUniqueUsername(email));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole(Role.ROLE_USER);
        user.setProvider(mapProvider(registrationId));
        return userRepository.save(user);
    }

    private String buildUniqueUsername(String email) {
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        if (base.isBlank()) {
            base = "user";
        }
        String candidate = base;
        int counter = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + counter++;
        }
        return candidate;
    }

    private AuthProvider mapProvider(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProvider.GOOGLE;
            case "facebook" -> AuthProvider.FACEBOOK;
            case "azure", "microsoft" -> AuthProvider.MICROSOFT;
            default -> AuthProvider.LOCAL;
        };
    }
}
