package me.emicsto.flashcards.flashcard;

import me.emicsto.flashcards.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

interface FlashcardRepository extends MongoRepository<Flashcard, String> {
    @PreAuthorize("#user.id == authentication.principal.id")
    List<Flashcard> findAllByUser(User user);

    @PreAuthorize("#user.id == authentication.principal.id")
    List<Flashcard> findAllByDeckIdAndUser(String deckId, User user, Pageable pageable);
}
