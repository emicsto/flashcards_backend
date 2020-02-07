package me.emicsto.flashcards.flashcard;

import lombok.AllArgsConstructor;
import me.emicsto.flashcards.user.User;
import me.emicsto.flashcards.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
class FlashcardService {
    private final FlashcardRepository flashcardRepository;

    List<FlashcardDto> findAllByUser(User user) {
        return ObjectMapperUtils.mapAll(flashcardRepository.findAllByUser(user), FlashcardDto.class);
    }

    public List<FlashcardDto> findAllByDeckIdAndUser(Long id, User user) {
        return ObjectMapperUtils.mapAll(flashcardRepository.findAllByDeckIdAndUser(id, user), FlashcardDto.class);
    }
}
