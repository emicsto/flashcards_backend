package me.emicsto.flashcards.flashcard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class FlashcardDto {
    private String id;
    private String front;
    private String back;
}
