package com.emicsto.flashcards.user;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
public class UserRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public UserRefreshToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public UserRefreshToken() {
    }
}
