package com.emicsto.flashcards.deck;

import org.springframework.data.jpa.repository.JpaRepository;

interface DeckRepository extends JpaRepository<Deck, Long> {

}
