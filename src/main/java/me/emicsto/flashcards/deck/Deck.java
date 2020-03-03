package me.emicsto.flashcards.deck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.emicsto.flashcards.flashcard.Flashcard;
import me.emicsto.flashcards.user.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deck {
    @Id
    private String id;
    private String name;
    private User user;

    @DBRef
    private List<Flashcard> flashcards = new ArrayList<>();
}
