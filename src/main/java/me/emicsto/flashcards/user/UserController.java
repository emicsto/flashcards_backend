package me.emicsto.flashcards.user;

import me.emicsto.flashcards.security.AccessToken;
import me.emicsto.flashcards.security.IdTokenDto;
import me.emicsto.flashcards.security.RefreshToken;
import me.emicsto.flashcards.security.TokenPair;
import me.emicsto.flashcards.utils.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserApi userApi;

    @PostMapping("/auth/token/sign-in")
    public ResponseEntity<TokenPair> signIn(@RequestBody IdTokenDto idToken) {
        return ResponseEntity.ok(userApi.signIn(idToken));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserDto> getMe(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok(ObjectMapperUtils.map(user, UserDto.class));
    }

    @PostMapping("/auth/token/refresh")
    public ResponseEntity<AccessToken> refresh(@Valid @RequestBody RefreshToken refreshToken) {
        return ResponseEntity.ok(userApi.refreshAccessToken(refreshToken.getRefreshToken()));
    }
}
