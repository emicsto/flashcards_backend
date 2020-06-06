package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.security.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @PreAuthorize("@AuthorizationComponent.isDeckOwner(#deckId, principal)")
    public ResponseEntity<List<FlashcardDto>> findAllByDeckId(@PathVariable String deckId, Pageable pageable) {
        return ResponseEntity.ok(flashcardApi.findAllByDeckIdAndUser(deckId, SecurityUtils.getCurrentUser(), pageable));
    }

    @PostMapping("/flashcards")
    @PreAuthorize("@AuthorizationComponent.isDeckOwner(#flashcard.deckId, principal)")
    public ResponseEntity<FlashcardDto> save(@Valid @RequestBody FlashcardDto flashcard) {
        return ResponseEntity.ok(flashcardApi.save(flashcard, SecurityUtils.getCurrentUser()));
    }

    @PutMapping("/flashcards")
    @PreAuthorize("@AuthorizationComponent.isFlashcardOwner(#flashcard.id, principal)")
    public ResponseEntity<FlashcardDto> update(@Valid @RequestBody FlashcardDto flashcard) {
        return ResponseEntity.ok(flashcardApi.update(flashcard, SecurityUtils.getCurrentUser()));
    }

    @PostMapping("/decks/{deckId}/flashcards/import")
    @PreAuthorize("@AuthorizationComponent.isDeckOwner(#deckId, principal)")
    public ResponseEntity<String> importFlashcards(@PathVariable String deckId, @RequestBody FlashcardsCsv flashcards) {
        flashcardApi.importFlashcards(deckId, flashcards.getFlashcards());
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/flashcards/{id}")
    @PreAuthorize("@AuthorizationComponent.isFlashcardOwner(#id, principal)")
    public ResponseEntity<String> delete(@PathVariable String id) {
        flashcardApi.delete(id);
        return ResponseEntity.ok("");
    }

    @PostMapping("/flashcards/{id}/estimates")
    @PreAuthorize("@AuthorizationComponent.isFlashcardOwner(#id, principal)")
    public ResponseEntity<FlashcardDto> rate(@PathVariable String id, @RequestBody FlashcardEstimateDto flashcardEstimateDto) {
        return ResponseEntity.ok(flashcardApi.estimate(id, flashcardEstimateDto.getEstimate(), SecurityUtils.getCurrentUser()));
    }
}
