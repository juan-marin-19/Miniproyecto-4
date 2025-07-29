package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.rules.CardRule;
import org.example.eiscuno.model.rules.ColorRule;
import org.example.eiscuno.model.rules.ValueRule;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class  Table {
    private ArrayList<Card> cardsTable;
    private ArrayList<CardRule> rules;



    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){
        this.cardsTable = new ArrayList<Card>();
        this.rules = new ArrayList<CardRule>();
        rules.add(new ColorRule());
        rules.add(new ValueRule());
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
    }

    /**
     * Determines if the card can be added or not to the table using rules of the interface CardRule
     *
     * @param card a card to be placed on the top of the table
     */
    public boolean canAddCardTable(Card card){

        if (cardsTable.isEmpty()) return true;

        Card topCard = getCurrentCardOnTheTable();

        for (CardRule rule : rules) {if (rule.canBePlayed(card, topCard)) return true;}

        return false;

    }


    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }
}
