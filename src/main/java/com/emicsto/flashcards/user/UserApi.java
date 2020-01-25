package com.emicsto.flashcards.user;

import com.emicsto.flashcards.infrastructure.ModuleApi;
import com.emicsto.flashcards.security.IdTokenDto;
import com.emicsto.flashcards.security.TokenPair;
import lombok.AllArgsConstructor;

@ModuleApi
@AllArgsConstructor
public class UserApi {
    private final UserService userService;

    public TokenPair signIn(IdTokenDto idToken) {
        return userService.signIn(idToken);
    }
}
