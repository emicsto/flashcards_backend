package com.emicsto.flashcards.user;

import com.emicsto.flashcards.security.IdTokenDto;
import com.emicsto.flashcards.security.TokenPair;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserApi userApi;

    @PostMapping("/auth/token/sign-in")
    public ResponseEntity<TokenPair> signIn(@RequestBody IdTokenDto idToken) {
        return ResponseEntity.ok(userApi.signIn(idToken));
    }

    //TODO: implement
    @PostMapping("/auth/token/refresh")
    public ResponseEntity<TokenPair> refresh() {
        return null;
    }
}
