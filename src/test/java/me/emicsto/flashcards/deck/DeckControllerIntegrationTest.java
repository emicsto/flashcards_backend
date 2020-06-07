package me.emicsto.flashcards.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.user.UserRepository;
import me.emicsto.flashcards.utils.ObjectMapperUtils;
import me.emicsto.flashcards.utils.WithMockCustomUser;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockCustomUser
class DeckControllerIntegrationTest {

    private static final String DECK_NAME = "deck";
    private static final String DECK_NAME_UPDATED = "deck_name_updated";
    private static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id_1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    private User user;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Deck.class);
        mongoTemplate.dropCollection(User.class);

        user = new User();
        user.setName(USER_NAME);
        user.setId(USER_ID);
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.dropCollection(Deck.class);
        mongoTemplate.dropCollection(User.class);
    }

    @Test
    void shouldSaveDeck() throws Exception {
        DeckDto deckDto = new DeckDto();
        deckDto.setName(DECK_NAME);

        mockMvc.perform(post("/api/decks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deckDto)))
                .andExpect(status().isOk());

        List<Deck> decks = deckRepository.findAll();

        assertThat(decks).hasSize(1);
        assertThat(decks.get(0).getName()).isEqualTo(DECK_NAME);
    }

    @Test
    void shouldReturnBadRequest_whenDeckWithEmptyNameIsPassedToSave() throws Exception {
        DeckDto deckDto = new DeckDto();

        mockMvc.perform(post("/api/decks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deckDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteDeck() throws Exception {
        Deck deck = new Deck();
        deck.setName(DECK_NAME);
        deck.setUser(user);
        Deck savedDeck = deckRepository.save(deck);

        mockMvc.perform(delete("/api/decks/" + savedDeck.getId())
                .contentType("application/json"))
                .andExpect(status().isOk());

        List<Deck> decks = deckRepository.findAll();

        assertThat(decks).hasSize(0);
    }

    @Test
    void deleteSomeonesDeckAttempt_shouldReturnForbidden() throws Exception {
        Deck deck = new Deck();
        deck.setName(DECK_NAME);
        deck.setUser(new User());
        Deck savedDeck = deckRepository.save(deck);

        mockMvc.perform(delete("/api/decks/" + savedDeck.getId())
                .contentType("application/json"))
                .andExpect(status().is(HttpStatus.SC_FORBIDDEN));
    }


    @Test
    void shouldUpdateDeck() throws Exception {
        Deck deck = Deck.builder()
                .id("1")
                .name(DECK_NAME)
                .user(user)
                .build();

        deckRepository.save(deck);

        DeckDto deckDto = ObjectMapperUtils.map(deck, DeckDto.class);
        deckDto.setName(DECK_NAME_UPDATED);

        mockMvc.perform(put("/api/decks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deckDto)))
                .andExpect(status().isOk());

        List<Deck> decks = deckRepository.findAll();

        assertThat(decks).hasSize(1);
        assertThat(decks.get(0).getName()).isEqualTo(DECK_NAME_UPDATED);
    }
}
