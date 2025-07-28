package org.example.eiscuno.model.rules;

import org.example.eiscuno.model.card.Card;


/**
 * interface that applies the open/closed principle with the placing rules for the cards
 * */
public interface CardRule {

    boolean canBePlayed(Card newCard, Card topCard);

}


