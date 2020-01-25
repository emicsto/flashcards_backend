package com.emicsto.flashcards.user;

import com.emicsto.flashcards.security.IdTokenDto;
import com.emicsto.flashcards.security.InvalidTokenException;
import com.emicsto.flashcards.security.TokenPair;
import com.emicsto.flashcards.security.TokenProvider;
import com.emicsto.flashcards.utils.ObjectMapperUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
class UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Value("${oauth.google.client-id}")
    private String clientId;

    TokenPair signIn(IdTokenDto idToken) {
        UserDto userDto = getUserPayload(idToken);
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        User user;

        if (existingUser.isEmpty()) {
            user = registerUser(userDto);
        } else {
            user = existingUser.get();
        }

        return loginUser(user);
    }

    TokenPair loginUser(User user) {
        String accessToken = tokenProvider.createAccessToken(user.getEmail());
        String refreshToken = tokenProvider.createRefreshToken(user);
        return new TokenPair(accessToken, refreshToken);
    }

    private User registerUser(UserDto userDto) {
        User user = ObjectMapperUtils.map(userDto, User.class);
        return userRepository.save(user);
    }

    private UserDto getUserPayload(IdTokenDto idToken) {
        GoogleIdToken googleIdToken = null;
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            googleIdToken = verifier.verify(idToken.getToken());
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        if (googleIdToken != null) {
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

             return UserDto.builder()
                     .email(payload.getEmail())
                     .name((String) payload.get("name"))
                     .pictureUrl((String) payload.get("picture"))
                     .build();
        } else {
            throw new InvalidTokenException();
        }
    }
}
