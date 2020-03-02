package me.emicsto.flashcards.deck;

import me.emicsto.flashcards.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

interface DeckRepository extends MongoRepository<Deck, String> {
    List<Deck> findAllByUser(User user);
}
