package org.example.eiscuno.model.rules;

import org.example.eiscuno.model.card.Card;


public class ColorRule implements CardRule {

        /**
         *
         * */
        @Override
        public boolean canBePlayed(Card newCard, Card topCard) {
            return newCard.getColor().equals(topCard.getColor()) || newCard.getColor().equals("CHOOSE");
        }
}



