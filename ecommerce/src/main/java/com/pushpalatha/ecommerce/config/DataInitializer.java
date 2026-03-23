package com.pushpalatha.ecommerce.config;

import com.pushpalatha.ecommerce.entity.AuthProvider;
import com.pushpalatha.ecommerce.entity.Product;
import com.pushpalatha.ecommerce.entity.Role;
import com.pushpalatha.ecommerce.entity.User;
import com.pushpalatha.ecommerce.repository.ProductRepository;
import com.pushpalatha.ecommerce.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@busybrains.local");
                admin.setFullName("Admin User");
                admin.setPhoneNumber("+91 9000000001");
                admin.setAddress("Bengaluru");
                admin.setRole(Role.ROLE_ADMIN);
                admin.setProvider(AuthProvider.LOCAL);
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("user")) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setEmail("user@busybrains.local");
                user.setFullName("Standard User");
                user.setPhoneNumber("+91 9000000002");
                user.setAddress("Hyderabad");
                user.setRole(Role.ROLE_USER);
                user.setProvider(AuthProvider.LOCAL);
                userRepository.save(user);
            }

            if (productRepository.count() == 0) {
                productRepository.saveAll(List.of(
                        product("Noise Cancelling Headphones", "Immersive wireless headphones with 30 hours battery backup.", "Electronics", "8999.00", "https://images.unsplash.com/photo-1505740420928-5e560c06d30e"),
                        product("Smart Fitness Watch", "Track workouts, health metrics, and notifications on the go.", "Wearables", "5999.00", "https://images.unsplash.com/photo-1523275335684-37898b6baf30"),
                        product("Minimal Desk Lamp", "Warm ambient desk light designed for long work sessions.", "Home", "2499.00", "https://images.unsplash.com/photo-1507473885765-e6ed057f782c"),
                        product("Everyday Backpack", "Water-resistant backpack with laptop sleeve and multiple organizer pockets.", "Accessories", "1999.00", "https://images.unsplash.com/photo-1542291026-7eec264c27ff")
                ));
            }
        };
    }

    private Product product(String name, String description, String category, String price, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(new BigDecimal(price));
        product.setImageUrl(imageUrl);
        return product;
    }
}
