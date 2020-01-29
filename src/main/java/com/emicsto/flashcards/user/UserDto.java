package com.emicsto.flashcards.user;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class UserDto {
    private String email;
    private String name;
    private String pictureUrl;
}
