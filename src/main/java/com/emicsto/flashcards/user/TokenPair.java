package com.emicsto.flashcards.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
class TokenPair {
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    public TokenPair(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
