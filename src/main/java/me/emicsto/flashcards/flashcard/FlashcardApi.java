package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.infrastructure.ModuleApi;
import me.emicsto.flashcards.user.User;

import java.util.List;

@ModuleApi
@AllArgsConstructor
public class FlashcardApi {
    private final FlashcardService flashcardService;

    public List<FlashcardDto> findAllByUser(User user) {
        return flashcardService.findAllByUser(user);
    }

    public List<FlashcardDto> findAllByDeckIdAndUser(Long id, User user) {
        return flashcardService.findAllByDeckIdAndUser(id, user);
    }
}
