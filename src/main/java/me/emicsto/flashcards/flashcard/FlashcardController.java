package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.security.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
class FlashcardController {
    private final FlashcardApi flashcardApi;

    @GetMapping("/flashcards")
    public ResponseEntity<List<FlashcardDto>> findAll() {
        return ResponseEntity.ok(flashcardApi.findAllByUser(SecurityUtils.getCurrentUser()));
    }

    @GetMapping("/decks/{deckId}/flashcards")
    public ResponseEntity<List<FlashcardDto>> findAllByDeckId(@PathVariable Long deckId, Pageable pageable) {
        return ResponseEntity.ok(flashcardApi.findAllByDeckIdAndUser(deckId, SecurityUtils.getCurrentUser(), pageable));
    }

    @PostMapping("/decks/{deckId}/flashcards/import")
    public ResponseEntity<String> importFlashcards(@PathVariable Long deckId, @RequestBody FlashcardsCsv flashcards) {
        flashcardApi.importFlashcards(deckId, flashcards.getFlashcards());
        return ResponseEntity.ok("");
    }
}
