package me.emicsto.flashcards.flashcard;

import me.emicsto.flashcards.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findAllByUser(User user);
    List<Flashcard> findAllByDeckIdAndUser(Long deckId, User user, Pageable pageable);
}
