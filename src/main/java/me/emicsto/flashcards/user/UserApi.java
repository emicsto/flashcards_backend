package me.emicsto.flashcards.user;

import me.emicsto.flashcards.infrastructure.ModuleApi;
import me.emicsto.flashcards.security.AccessToken;
import me.emicsto.flashcards.security.IdTokenDto;
import me.emicsto.flashcards.security.TokenPair;
import lombok.AllArgsConstructor;

@ModuleApi
@AllArgsConstructor
public class UserApi {
    private final UserService userService;

    public TokenPair signIn(IdTokenDto idToken) {
        return userService.signIn(idToken);
    }

    public AccessToken refreshAccessToken(String refreshToken) {
        return userService.refreshToken(refreshToken);
    }
}
