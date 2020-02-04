package me.emicsto.flashcards.deck;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class DeckDto {
    private Long id;
    private String name;
    private Integer quantity;
}
