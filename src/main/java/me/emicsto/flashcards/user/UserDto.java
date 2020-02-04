package me.emicsto.flashcards.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String sub;
}
