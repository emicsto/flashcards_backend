package me.emicsto.flashcards.deck;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
class DeckService {
    private final DeckRepository deckRepository;

    List<DeckDto> findAllByUser(User user) {
        return ObjectMapperUtils.mapAll(deckRepository.findAllByUser(user), DeckDto.class);
    }

    public Deck findById(Long id) {
        return deckRepository.findById(id).orElseThrow(DeckNotFoundException::new);
    }
}
