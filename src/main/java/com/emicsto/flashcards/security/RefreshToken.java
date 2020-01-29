package com.emicsto.flashcards.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RefreshToken {

    @NotBlank
    @JsonProperty("refresh_token")
    private final String refreshToken;

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
