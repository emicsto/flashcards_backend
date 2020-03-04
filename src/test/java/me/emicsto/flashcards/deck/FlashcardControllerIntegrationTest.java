package me.emicsto.flashcards.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.emicsto.flashcards.flashcard.Flashcard;
import me.emicsto.flashcards.flashcard.FlashcardDto;
import me.emicsto.flashcards.flashcard.FlashcardRepository;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.user.UserRepository;
import me.emicsto.flashcards.utils.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    User user = new User();

    @BeforeEach
    void setUp() {
        flashcardRepository.deleteAll();;
        userRepository.deleteAll();;

        user.setName("username");
        userRepository.save(user);
    }

    @Test
    void shouldSaveFlashcard() throws Exception {
        Deck deck = new Deck();
        deck.setUser(user);
        deck.setName("deck");
        deck.setId("1");
        deckRepository.save(deck);

        FlashcardDto flashcardDto = new FlashcardDto();
        flashcardDto.setFront("front");
        flashcardDto.setBack("back");
        flashcardDto.setDeckId("1");

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
    }
}
