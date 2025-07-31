package org.example.eiscuno.model.rules;

import org.example.eiscuno.model.card.Card;
/**
 * Rule that allows playing a card based on its color.
 * A new card can be played if its color matches the top card's color,
 * or if it is a special "CHOOSE" card (which defers color choice).
 */
public class ColorRule implements CardRule {


        @Override
        public boolean canBePlayed(Card newCard, Card topCard) {
            return newCard.getColor().equals(topCard.getColor()) || newCard.getColor().equals("CHOOSE");
        }
}



