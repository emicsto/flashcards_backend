package me.emicsto.flashcards.flashcard;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDto {
    private String id;
    @NotEmpty
    private String front;
    @NotEmpty
    private String back;
    private String deckId;
}
