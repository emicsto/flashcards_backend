package me.emicsto.flashcards.security;

import lombok.RequiredArgsConstructor;
import me.emicsto.flashcards.deck.DeckApi;
import me.emicsto.flashcards.flashcard.FlashcardApi;
import me.emicsto.flashcards.user.User;
import org.springframework.stereotype.Component;

@Component("AuthorizationComponent")
@RequiredArgsConstructor
public class AuthorizationComponent {
    private final DeckApi deckApi;
    private final FlashcardApi flashcardApi;

    public boolean isDeckOwner(String deckId, User user) {
        return deckApi.findById(deckId).getUser().equals(user);
    }

    public boolean isFlashcardOwner(String flashcardId, User user) {
        return flashcardApi.findById(flashcardId).getUser().equals(user);
    }
}
