package me.emicsto.flashcards.deck;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.user.UserRepository;
import me.emicsto.flashcards.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
class DeckService {
    private final DeckRepository deckRepository;

    List<DeckDto> findAllByUser(User user) {
        List<Deck> decks = deckRepository.findAllByUser(user);
        List<DeckDto> decksDto = new ArrayList<>();

        for(Deck deck : decks) {
            DeckDto deckDto = ObjectMapperUtils.map(deck, DeckDto.class);
            //TODO optimize
            deckDto.setQuantity(deck.getFlashcards().size());
            decksDto.add(deckDto);
        }

        return decksDto;
    }

    Deck findById(Long id) {
        return deckRepository.findById(id).orElseThrow(DeckNotFoundException::new);
    }

    DeckDto save(DeckDto deckDto, User user) {
        Deck deck = ObjectMapperUtils.map(deckDto, Deck.class);
        deck.setUser(user);
        return ObjectMapperUtils.map(deckRepository.save(deck), DeckDto.class);
    }
}
