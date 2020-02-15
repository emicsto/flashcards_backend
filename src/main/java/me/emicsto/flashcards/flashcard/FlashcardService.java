package me.emicsto.flashcards.flashcard;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import me.emicsto.flashcards.deck.Deck;
import me.emicsto.flashcards.deck.DeckApi;
import me.emicsto.flashcards.security.SecurityUtils;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;

@Service
@AllArgsConstructor
class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final DeckApi deckApi;

    List<FlashcardDto> findAllByUser(User user) {
        return ObjectMapperUtils.mapAll(flashcardRepository.findAllByUser(user), FlashcardDto.class);
    }

    public List<FlashcardDto> findAllByDeckIdAndUser(Long id, User user) {
        return ObjectMapperUtils.mapAll(flashcardRepository.findAllByDeckIdAndUser(id, user), FlashcardDto.class);
    }

    public void importFlashcards(Long deckId, String flashcardsCsv) {
        CSVReader reader = new CSVReaderBuilder(new StringReader(flashcardsCsv)).build();
        List<Flashcard> flashcards =  new CsvToBeanBuilder(reader).withType(Flashcard.class).build().parse();

        Deck deck = deckApi.findById(deckId);
        User user = SecurityUtils.getCurrentUser();

        for(Flashcard flashcard : flashcards) {
            flashcard.setUser(user);
            flashcard.setDeck(deck);
            flashcardRepository.save(flashcard);
        }
    }
}
