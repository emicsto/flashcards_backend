package com.emicsto.flashcards.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
class UserDto {
    private String email;
    private String name;
    private String pictureUrl;
}
