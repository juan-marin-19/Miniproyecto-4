package org.example.eiscuno.model.rules;

import org.example.eiscuno.model.card.Card;
/**
 * Rule that allows playing a card based on its value.
 * A new card can be played if its value matches the value of the top card.
 */
public class ValueRule implements CardRule {

        @Override
        public boolean canBePlayed(Card newCard, Card topCard) {
            return newCard.getValue().equals(topCard.getValue());
        }
}


