package me.emicsto.flashcards.deck;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/decks")
@AllArgsConstructor
class DeckController {
    private final DeckApi deckApi;

    @GetMapping
    public ResponseEntity<List<DeckDto>> findAll() {
        return ResponseEntity.ok(deckApi.findAllByUser(SecurityUtils.getCurrentUser()));
    }

    @PostMapping
    public ResponseEntity<DeckDto> save(@Valid @RequestBody DeckDto deck) {
        return ResponseEntity.ok(deckApi.save(deck, SecurityUtils.getCurrentUser()));
    }
}
