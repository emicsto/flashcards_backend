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

    public Deck findById(String id) {
        return deckService.findById(id);
    }

    public DeckDto save(DeckDto deck, User user) {
        return deckService.save(deck, user);
    }

    public DeckDto save(Deck deck, User user) {
        return deckService.save(deck, user);
    }

    public void delete(String id) {
        deckService.delete(id);
    }

    public DeckDto update(DeckDto deck) {
        return deckService.update(deck);
    }
}
