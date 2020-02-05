package me.emicsto.flashcards.deck;

import me.emicsto.flashcards.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findAllByUser(User user);
}
