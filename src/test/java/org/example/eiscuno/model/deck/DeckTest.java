package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {


    private Deck deck;

    @BeforeAll
    static void disableImageLoading() {
        Card.testing = true;
    }

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testDeckIsInitialized_NotEmpty() {
        assertFalse(deck.isEmpty());
    }



    @Test
    public void testTakeAllCardsAndThenThrow() {
        while (!deck.isEmpty()) {
            deck.takeCard();
        }

        assertThrows(IllegalStateException.class, () -> deck.takeCard());
    }

}