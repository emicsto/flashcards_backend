package com.emicsto.flashcards.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccessToken {

    @JsonProperty("access_token")
    private final String accessToken;

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
