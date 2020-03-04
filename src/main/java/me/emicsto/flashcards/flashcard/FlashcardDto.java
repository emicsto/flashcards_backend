package me.emicsto.flashcards.flashcard;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class FlashcardDto {
    private String id;
    @NotEmpty
    private String front;
    @NotEmpty
    private String back;
    private String deckId;
}
