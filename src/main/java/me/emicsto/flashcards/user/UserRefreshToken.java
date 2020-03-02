package me.emicsto.flashcards.user;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserRefreshToken {
    @Id
    private String id;

    private String token;

    private User user;

    public UserRefreshToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public UserRefreshToken() {
    }
}
