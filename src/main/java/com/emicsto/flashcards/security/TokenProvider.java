package com.emicsto.flashcards.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.emicsto.flashcards.user.User;
import com.emicsto.flashcards.user.UserRefreshToken;
import com.emicsto.flashcards.user.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    public static final int ONE_MINUTE = 60000;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.encryption.secret}")
    private String secret;

    @Value("${jwt.access.token.expiration.minutes}")
    private long expirationTimeInMinutes;


    public String createAccessToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                //TODO: Reduce expiration time
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeInMinutes * ONE_MINUTE))
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public String createRefreshToken(User user) {
        String token = RandomStringUtils.randomAlphanumeric(128);
        userRefreshTokenRepository.save(new UserRefreshToken(token, user));
        return token;
    }

    DecodedJWT getDecodedToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
        } catch (TokenExpiredException ex) {
            throw new InvalidTokenException();
        }
    }

}
