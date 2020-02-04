package me.emicsto.flashcards.deck;

import me.emicsto.flashcards.infrastructure.ModuleApi;
import lombok.AllArgsConstructor;

import java.util.List;

@ModuleApi
@AllArgsConstructor
public class DeckApi {
    private final DeckService deckService;

    public List<DeckDto> findAll() {
        return deckService.findAll();
    }
}
