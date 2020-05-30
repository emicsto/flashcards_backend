package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.infrastructure.ModuleApi;
import me.emicsto.flashcards.user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ModuleApi
@AllArgsConstructor
public class FlashcardApi {
    private final FlashcardService flashcardService;

    public List<FlashcardDto> findAllByUser(User user) {
        return flashcardService.findAllByUser(user);
    }

    public List<FlashcardDto> findAllByDeckIdAndUser(String id, User user, Pageable pageable) {
        return flashcardService.findAllByDeckIdAndUser(id, user, pageable);
    }

    public void importFlashcards(String deckId, String flashcards) {
        flashcardService.importFlashcards(deckId, flashcards);
    }

    public FlashcardDto save(FlashcardDto flashcardDto, User currentUser) {
        return flashcardService.save(flashcardDto, currentUser);
    }

    public void delete(String id) {
        flashcardService.delete(id);
    }

    public FlashcardDto update(FlashcardDto flashcardDto, User currentUser) {
        return flashcardService.update(flashcardDto, currentUser);
    }

    public FlashcardDto estimate(String id, Estimate estimate, User currentUser) {
        return flashcardService.estimate(id, estimate, currentUser);
    }
}
