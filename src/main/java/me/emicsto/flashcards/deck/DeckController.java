package me.emicsto.flashcards.deck;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
@AllArgsConstructor
public class DeckController {
    private final DeckApi deckApi;

    @GetMapping
    public ResponseEntity<List<DeckDto>> findAll() {
        return ResponseEntity.ok(deckApi.findAll());
    }
}
