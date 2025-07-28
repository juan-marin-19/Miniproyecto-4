package org.example.eiscuno.model.rules;

import org.example.eiscuno.model.card.Card;



public class ValueRule implements CardRule {

        /**
         *
         * */
        @Override
        public boolean canBePlayed(Card newCard, Card topCard) {
            return newCard.getValue().equals(topCard.getValue());
        }
}


