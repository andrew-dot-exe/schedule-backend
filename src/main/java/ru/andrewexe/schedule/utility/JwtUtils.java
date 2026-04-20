package ru.andrewexe.schedule.utility;

import java.util.Optional;

public final class JwtUtils {

    private static final String BEARER_PREFIX = "Bearer ";

    private JwtUtils() {
    }

    public static Optional<String> extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(authorizationHeader.substring(BEARER_PREFIX.length()));
    }

}
