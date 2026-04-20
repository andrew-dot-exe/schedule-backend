package ru.andrewexe.schedule.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "test-jwt-secret-key-12345678901234567890";

    @Test
    void getTokenCreatesSignedTokenWithUsernameSubject() {
        JwtService service = new JwtService(SECRET, Duration.ofHours(1));
        UserDetails user = testUser("andrew");

        String token = service.getToken(user);

        assertThat(service.extractUsername(token)).isEqualTo("andrew");
        assertThat(service.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValidReturnsFalseForDifferentUser() {
        JwtService service = new JwtService(SECRET, Duration.ofHours(1));
        String token = service.getToken(testUser("andrew"));

        assertThat(service.isTokenValid(token, testUser("jane"))).isFalse();
    }

    @Test
    void isTokenValidReturnsFalseForExpiredToken() {
        JwtService service = new JwtService(SECRET, Duration.ofSeconds(-1));
        String token = service.getToken(testUser("andrew"));

        assertThat(service.isTokenValid(token, testUser("andrew"))).isFalse();
    }

    private UserDetails testUser(String username) {
        return User.withUsername(username)
                .password("encoded-password")
                .roles("ADMIN")
                .build();
    }
}
