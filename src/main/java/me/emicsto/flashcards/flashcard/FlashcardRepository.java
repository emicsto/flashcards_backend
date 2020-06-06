package me.emicsto.flashcards.flashcard;

import me.emicsto.flashcards.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FlashcardRepository extends MongoRepository<Flashcard, String> {
    List<Flashcard> findAllByUser(User user);
    List<Flashcard> findAllByDeckIdAndUser(String deckId, User user, Pageable pageable);
}
