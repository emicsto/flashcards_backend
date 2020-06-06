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
import org.springframework.data.domain.Pageable;
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

    List<FlashcardDto> findAllByDeckIdAndUser(String id, User user, Pageable pageable) {
        return ObjectMapperUtils.mapAll(flashcardRepository.findAllByDeckIdAndUser(id, user, pageable), FlashcardDto.class);
    }

    FlashcardDto save(FlashcardDto flashcardDto, User user) {
        Flashcard flashcard = ObjectMapperUtils.map(flashcardDto, Flashcard.class);
        Deck deck = deckApi.findById(flashcardDto.getDeckId());

        flashcard.setUser(user);
        flashcard.setDeck(deck);

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        deck.getFlashcards().add(savedFlashcard);
        deckApi.save(deck, user);

        return ObjectMapperUtils.map(savedFlashcard, FlashcardDto.class);
    }


    FlashcardDto save(Flashcard flashcard, User user) {
        Deck deck = flashcard.getDeck();

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        deck.getFlashcards().add(savedFlashcard);
        deckApi.save(deck, user);

        return ObjectMapperUtils.map(savedFlashcard, FlashcardDto.class);
    }



    FlashcardDto update(FlashcardDto flashcardDto, User user) {
        Flashcard flashcard = get(flashcardDto.getId());
        Deck deck = deckApi.findById(flashcardDto.getDeckId());

        if(!flashcardDto.getDeckId().equals(flashcard.getDeck().getId())) {
            Deck oldDeck = deckApi.findById(flashcard.getDeck().getId());
            oldDeck.getFlashcards().remove(flashcard);
            deckApi.save(oldDeck, user);
        }

        flashcard.setFront(flashcardDto.getFront());
        flashcard.setBack(flashcardDto.getBack());
        flashcard.setDeck(deck);
        flashcardRepository.save(flashcard);

        deck.getFlashcards().add(flashcard);
        deckApi.save(deck, user);

        return ObjectMapperUtils.map(flashcard, FlashcardDto.class);
    }

    void importFlashcards(String deckId, String flashcardsCsv) {
        CSVReader reader = new CSVReaderBuilder(new StringReader(flashcardsCsv)).build();
        List<Flashcard> flashcards = new CsvToBeanBuilder(reader).withType(Flashcard.class).build().parse();

        Deck deck = deckApi.findById(deckId);
        User user = SecurityUtils.getCurrentUser();

        for (Flashcard flashcard : flashcards) {
            flashcard.setUser(user);
            flashcard.setDeck(deck);
        }

        flashcardRepository.saveAll(flashcards);

        deck.getFlashcards().addAll(flashcards);
        deckApi.save(deck, user);
    }

    Flashcard get(String id) {
        return flashcardRepository.findById(id).orElseThrow(FlashcardNotFoundException::new);
    }

    void delete(String id) {
        Flashcard flashcard = get(id);
        flashcardRepository.delete(flashcard);
    }

    public FlashcardDto estimate(String id, Estimate estimate, User user) {
        Flashcard flashcard = get(id);
        flashcard.setEstimate(estimate);
        return ObjectMapperUtils.map(save(flashcard, user), FlashcardDto.class);
    }
}
