package me.emicsto.flashcards.deck;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.infrastructure.ModuleApi;
import me.emicsto.flashcards.user.User;

import java.util.List;

@ModuleApi
@AllArgsConstructor
public class DeckApi {
    private final DeckService deckService;

    public List<DeckDto> findAllByUser(User user) {
        return deckService.findAllByUser(user);
    }

    public Deck findById(Long id) {
        return deckService.findById(id);
    }
}
