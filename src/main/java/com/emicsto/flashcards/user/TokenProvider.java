package com.emicsto.flashcards.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
class TokenProvider {
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Value("${jwt.encryption.secret}")
    private String secret;

    @Value("${jwt.access.token.expiration.seconds}")
    private long expirationTimeInSeconds;

    String createAccessToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                //TODO: Reduce expiration time
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeInSeconds * 200))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    String createRefreshToken(User user) {
        String token = RandomStringUtils.randomAlphanumeric(128);
        userRefreshTokenRepository.save(new UserRefreshToken(token, user));
        return token;
    }
}
