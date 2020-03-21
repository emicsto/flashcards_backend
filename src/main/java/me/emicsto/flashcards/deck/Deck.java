package me.emicsto.flashcards.deck;

import lombok.*;
import me.emicsto.flashcards.flashcard.Flashcard;
import me.emicsto.flashcards.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"flashcards"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deck {
    @Id
    private String id;
    private String name;
    private User user;

    @DBRef
    private List<Flashcard> flashcards = new ArrayList<>();
}
