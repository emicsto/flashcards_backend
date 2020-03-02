package me.emicsto.flashcards.deck;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
class DeckDto {
    private String id;
    @NotEmpty
    private String name;
    private Integer quantity;
}
