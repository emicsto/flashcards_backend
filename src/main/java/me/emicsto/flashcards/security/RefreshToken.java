package me.emicsto.flashcards.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshToken {

    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshToken() {
    }
}
