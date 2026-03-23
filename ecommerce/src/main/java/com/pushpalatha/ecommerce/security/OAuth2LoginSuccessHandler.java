package com.pushpalatha.ecommerce.security;

import com.pushpalatha.ecommerce.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final String redirectUri;

    public OAuth2LoginSuccessHandler(
            JwtService jwtService,
            UserRepository userRepository,
            @Value("${app.oauth2.authorized-redirect-uri}") String redirectUri
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.redirectUri = redirectUri;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not available from OAuth provider");
            return;
        }

        UserPrincipal principal = userRepository.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow();

        String token = jwtService.generateToken(principal);
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, redirectUri + "?token=" + encodedToken);
    }
}
