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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockCustomUser
class FlashcardControllerIntegrationTest {

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

        user.setName("name");
        user.setId("1");
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
                .id("1")
                .user(user)
                .name("deck")
                .build();

        deckRepository.save(deck);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .deckId("1")
                .front("front")
                .back("back")
                .build();

        mockMvc.perform(post("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAll();

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getFront()).isEqualTo("front");
        assertThat(flashcards.get(0).getBack()).isEqualTo("back");
        assertThat(flashcards.get(0).getDeck().getId()).isEqualTo("1");
        assertThat(flashcards.get(0).getDeck().getName()).isEqualTo("deck");
        assertThat(flashcards.get(0).getUser().getName()).isEqualTo("name");
    }

    @Test
    void shouldUpdateFlashcard() throws Exception {
        Deck deck = Deck.builder()
                .id("1")
                .user(user)
                .name("deck")
                .build();

        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.builder()
                .front("front")
                .back("back")
                .user(user)
                .deck(deck)
                .build();

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .id(savedFlashcard.getId())
                .deckId("1")
                .front("front_updated")
                .back("back_updated")
                .build();

        mockMvc.perform(put("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().isOk());

        List<Flashcard> flashcards = flashcardRepository.findAll();

        assertThat(flashcards).hasSize(1);
        assertThat(flashcards.get(0).getFront()).isEqualTo("front_updated");
        assertThat(flashcards.get(0).getBack()).isEqualTo("back_updated");
        assertThat(flashcards.get(0).getDeck().getId()).isEqualTo("1");
        assertThat(flashcards.get(0).getDeck().getName()).isEqualTo("deck");
        assertThat(flashcards.get(0).getUser().getName()).isEqualTo("name");
    }

    @Test
    void addFlashcardToSomeonesDeck_shouldReturnForbidden() throws Exception {
        Deck deck = Deck.builder()
                .id("1")
                .user(new User())
                .name("deck")
                .build();

        deckRepository.save(deck);

        FlashcardDto flashcardDto = FlashcardDto.builder()
                .deckId("1")
                .front("front")
                .back("back")
                .build();

        mockMvc.perform(post("/api/flashcards")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(flashcardDto)))
                .andExpect(status().is(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    void shouldReturnUserFlashcards() throws Exception {
        String deckId = "1";
        Deck deck = Deck.builder()
                .id(deckId)
                .user(user)
                .name("deck")
                .build();

        deckRepository.save(deck);

        Flashcard flashcard1 = Flashcard.builder()
                .id("1")
                .deck(deck)
                .front("front1")
                .back("back1")
                .user(user)
                .build();

        Flashcard flashcard2 = Flashcard.builder()
                .id("2")
                .deck(deck)
                .front("front2")
                .back("back2")
                .user(user)
                .build();

        Flashcard flashcard3 = Flashcard.builder()
                .id("3")
                .deck(deck)
                .front("front3")
                .back("back3")
                .user(new User())
                .build();

        flashcardRepository.saveAll(Arrays.asList(flashcard1, flashcard2, flashcard3));

        mockMvc.perform(get("/api/decks/" + deckId + "/flashcards")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].front").value("front1"))
                .andExpect(jsonPath("$[0].back").value("back1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].front").value("front2"))
                .andExpect(jsonPath("$[1].back").value("back2"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteFlashcard() throws Exception {
        String deckId = "1";
        Deck deck = Deck.builder()
                .id(deckId)
                .user(user)
                .name("deck")
                .build();

        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.builder()
                .front("test_front")
                .back("test_back")
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
}
