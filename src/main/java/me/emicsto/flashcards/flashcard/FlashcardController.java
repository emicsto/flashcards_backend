package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@AllArgsConstructor
class FlashcardController {
    private final FlashcardApi flashcardApi;

    @GetMapping
    public ResponseEntity<List<FlashcardDto>> findAll() {
        return ResponseEntity.ok(flashcardApi.findAllByUser(SecurityUtils.getCurrentUser()));
    }
}
