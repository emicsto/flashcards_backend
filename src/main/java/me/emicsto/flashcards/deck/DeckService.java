package me.emicsto.flashcards.deck;

import me.emicsto.flashcards.utils.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
class DeckService {
    private final DeckRepository deckRepository;

    List<DeckDto> findAll() {
        return ObjectMapperUtils.mapAll(deckRepository.findAll(), DeckDto.class);
    }
}
