package com.emicsto.flashcards.deck;

import com.emicsto.flashcards.infrastructure.ModuleApi;
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
