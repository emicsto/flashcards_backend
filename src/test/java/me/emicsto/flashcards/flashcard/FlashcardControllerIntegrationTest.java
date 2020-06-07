package me.emicsto.flashcards.flashcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.emicsto.flashcards.deck.Deck;
import me.emicsto.flashcards.deck.DeckRepository;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.user.UserRepository;
import me.emicsto.flashcards.utils.WithMockCustomUser;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockCustomUser
class FlashcardControllerIntegrationTest {

    private static final String DECK_ID_1 = "deck_id_1";
    private static final String DECK_NAME = "deck";
    private static final String FRONT = "front";
    private static final String BACK = "back";
    private static final String FRONT_UPDATED = "front_updated";
    private static final String BACK_UPDATED = "back_updated";
    private static final String SOURCE_DECK = "source_Deck";
    private static final String DESTINATION_DECK = "destination_deck";
    private static final String FRONT_2 = "front_2";
    private static final String BACK_2 = "back_2";
    private static final String FRONT_3 = "front_3";
    private static final String BACK_3 = "back_3";
    private static final String DECK_ID_2 = "deck_id_2";
    private static final String FLASHCARD_ID_1 = "flashcard_id_1";
    private static final String FLASHCARD_ID_2 = "flashcard_id_2";
    private static final String FLASHCARD_ID_3 = "flashcard_id_3";
    private static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id_1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    User user = new User();

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Deck.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Flashcard.class);

        user.setName(USER_NAME);
        user.setId(USER_ID);
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.dropCollection(Deck.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Flashcard.class);
    }

    @Test
    void shouldSaveFlashcard() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .deckId(DECK_ID_1)
                .front(FRONT)
                .back(BACK)
                .build();

        mockMvc.perform(post("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAll();

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getFront()).isEqualTo(FRONT);
        assertThat(flashcards.get(0).getBack()).isEqualTo(BACK);
        assertThat(flashcards.get(0).getDeck().getId()).isEqualTo(DECK_ID_1);
        assertThat(flashcards.get(0).getDeck().getName()).isEqualTo(DECK_NAME);
        assertThat(flashcards.get(0).getUser().getName()).isEqualTo(USER_NAME);
    }

    @Test
    void shouldUpdateFlashcard() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.builder()
                .front(FRONT)
                .back(BACK)
                .user(user)
                .deck(deck)
                .build();

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .id(savedFlashcard.getId())
                .deckId(DECK_ID_1)
                .front(FRONT_UPDATED)
                .back(BACK_UPDATED)
                .build();

        mockMvc.perform(put("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAllByDeckIdAndUser(deck.getId(), user, Pageable.unpaged());

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getFront()).isEqualTo(FRONT_UPDATED);
        assertThat(flashcards.get(0).getBack()).isEqualTo(BACK_UPDATED);
        assertThat(flashcards.get(0).getDeck().getId()).isEqualTo(DECK_ID_1);
        assertThat(flashcards.get(0).getDeck().getName()).isEqualTo(DECK_NAME);
        assertThat(flashcards.get(0).getUser().getName()).isEqualTo(USER_NAME);
    }

    @Test
    void shouldChangeFlashcardDeck() throws Exception {
        Deck sourceDeck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(SOURCE_DECK)
                .build();

        Deck destinationDeck = Deck.builder()
                .id(DECK_ID_2)
                .user(user)
                .name(DESTINATION_DECK)
                .build();

        deckRepository.saveAll(Arrays.asList(sourceDeck, destinationDeck));

        Flashcard flashcard = Flashcard.builder()
                .front(FRONT)
                .back(BACK)
                .user(user)
                .deck(sourceDeck)
                .build();

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .id(savedFlashcard.getId())
                .deckId(DECK_ID_2)
                .front(FRONT)
                .back(BACK)
                .build();

        mockMvc.perform(put("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAllByDeckIdAndUser(destinationDeck.getId(), user, Pageable.unpaged());

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getFront()).isEqualTo(FRONT);
        assertThat(flashcards.get(0).getBack()).isEqualTo(BACK);
        assertThat(flashcards.get(0).getDeck().getId()).isEqualTo(DECK_ID_2);
        assertThat(flashcards.get(0).getDeck().getName()).isEqualTo(DESTINATION_DECK);
        assertThat(flashcards.get(0).getUser().getName()).isEqualTo(USER_NAME);
    }

    @Test
    void addFlashcardToSomeonesDeck_shouldReturnForbidden() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(new User())
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .deckId(DECK_ID_1)
                .front(FRONT)
                .back(BACK)
                .build();

        mockMvc.perform(post("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().is(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    void shouldReturnUserFlashcards() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        Flashcard flashcard1 = Flashcard.builder()
                .id(FLASHCARD_ID_1)
                .deck(deck)
                .front(FRONT)
                .back(BACK)
                .user(user)
                .build();

        Flashcard flashcard2 = Flashcard.builder()
                .id(FLASHCARD_ID_2)
                .deck(deck)
                .front(FRONT_2)
                .back(BACK_2)
                .user(user)
                .build();

        Flashcard flashcard3 = Flashcard.builder()
                .id(FLASHCARD_ID_3)
                .deck(deck)
                .front(FRONT_3)
                .back(BACK_3)
                .user(new User())
                .build();

        flashcardRepository.saveAll(Arrays.asList(flashcard1, flashcard2, flashcard3));

        mockMvc.perform(get("/api/decks/" + DECK_ID_1 + "/flashcards")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(FLASHCARD_ID_1))
                .andExpect(jsonPath("$[0].front").value(FRONT))
                .andExpect(jsonPath("$[0].back").value(BACK))
                .andExpect(jsonPath("$[1].id").value(FLASHCARD_ID_2))
                .andExpect(jsonPath("$[1].front").value(FRONT_2))
                .andExpect(jsonPath("$[1].back").value(BACK_2))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteFlashcard() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.builder()
                .front(FRONT)
                .back(BACK)
                .user(user)
                .deck(deck)
                .build();

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        mockMvc.perform(delete("/api/flashcards/" + savedFlashcard.getId())
                .contentType("application/json"))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAll();

        assertThat(flashcards).hasSize(0);
    }


    @Test
    void shouldEstimateFlashcard() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.builder()
                .id(FLASHCARD_ID_1)
                .front(FRONT)
                .back(BACK)
                .user(user)
                .deck(deck)
                .build();

       flashcardRepository.save(flashcard);

        mockMvc.perform(post("/api/flashcards/"+FLASHCARD_ID_1+"/estimates")
                .content(objectMapper.writeValueAsString(new FlashcardEstimateDto(Estimate.AGAIN)))
                .contentType("application/json"))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAll();

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getId()).isEqualTo(FLASHCARD_ID_1);
        assertThat(flashcards.get(0).getEstimate()).isEqualTo(Estimate.AGAIN);
    }

    @Test
    void shouldOverwriteOldEstimate() throws Exception {
        Deck deck = Deck.builder()
                .id(DECK_ID_1)
                .user(user)
                .name(DECK_NAME)
                .build();

        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.builder()
                .id(FLASHCARD_ID_1)
                .front(FRONT)
                .back(BACK)
                .user(user)
                .deck(deck)
                .estimate(Estimate.HARD)
                .build();

       flashcardRepository.save(flashcard);

        mockMvc.perform(post("/api/flashcards/"+FLASHCARD_ID_1+"/estimates")
                .content(objectMapper.writeValueAsString(new FlashcardEstimateDto(Estimate.GOOD)))
                .contentType("application/json"))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAll();

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getId()).isEqualTo(FLASHCARD_ID_1);
        assertThat(flashcards.get(0).getEstimate()).isEqualTo(Estimate.GOOD);
    }
}
