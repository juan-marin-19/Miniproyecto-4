package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table table;

    @BeforeAll
    static void disableImageLoading() {
        Card.testing = true;
    }

    @BeforeEach
    void setUp() {
        table = new Table();
    }

    @Test
    void testAddCardOnTheTable_AddsCardSuccessfully() {
        Card card = card = new Card("/org/example/eiscuno/cards-uno/6_blue.png", "6", "BLUE");
        table.addCardOnTheTable(card);

        assertEquals(card, table.getCurrentCardOnTheTable());
    }



    @Test
    void testCanAddCardTable_ReturnsTrueWhenTableIsEmpty() {
        Card card = new Card("/org/example/eiscuno/cards-uno/6_blue.png", "6", "BLUE");

        assertTrue(table.canAddCardTable(card));
    }


    @Test
    void testCanAddCardTable_TrueWhenValueMatches() {
        Card card1 = new Card("/org/example/eiscuno/cards-uno/6_blue.png", "6", "BLUE");
        Card card2 = new Card("/org/example/eiscuno/cards-uno/2_blue.png", "2", "BLUE");

        table.addCardOnTheTable(card1);

        assertTrue(table.canAddCardTable(card2));
    }
}


