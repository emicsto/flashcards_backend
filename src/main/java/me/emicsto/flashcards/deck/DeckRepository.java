package me.emicsto.flashcards.deck;

import me.emicsto.flashcards.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface DeckRepository extends MongoRepository<Deck, String> {
    List<Deck> findAllByUser(User user);

    @Override
    @PreAuthorize("#deck.user.id == authentication.principal.id")
    void delete(Deck deck);
}
