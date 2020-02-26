package me.emicsto.flashcards.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import me.emicsto.flashcards.utils.WithMockCustomUser;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockCustomUser
class DeckControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("username");
        userRepository.save(user);
    }

    @Test
    @Transactional
    void shouldSaveDeck() throws Exception {
        DeckDto deckDto = new DeckDto();
        deckDto.setName("deck");

        mockMvc.perform(post("/api/decks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deckDto)))
                .andExpect(status().isOk());

        List<Deck> decks = deckRepository.findAll();

        assertThat(decks).hasSize(1);
        assertThat(decks.get(0).getName()).isEqualTo("deck");
    }

    @Test
    void shouldReturnBadRequest_whenDeckWithEmptyNameIsPassedToSave() throws Exception {
        DeckDto deckDto = new DeckDto();

        mockMvc.perform(post("/api/decks")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(deckDto)))
                .andExpect(status().isBadRequest());
    }
}
