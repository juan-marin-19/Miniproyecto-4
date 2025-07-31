package org.example.eiscuno.model.game;

import javafx.application.Platform;
import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CardTest {

    private Card card;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setup() {
        card = new Card("/org/example/eiscuno/cards-uno/6_blue.png", "6", "BLUE");
    }

    @Test
    void cardAttributesShouldBeCorrect() {
        assertTrue(card.getImage().getUrl().contains("/org/example/eiscuno/cards-uno/6_blue.png"));

        assertEquals("6", card.getValue());
        assertEquals("BLUE", card.getColor());
    }
}
