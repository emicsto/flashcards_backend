package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.emicsto.flashcards.deck.Deck;
import me.emicsto.flashcards.user.User;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flashcard {
    @Id
    private String id;
    private String front;
    private String back;
    private User user;
    private Deck deck;
    private Estimate estimate;
}
