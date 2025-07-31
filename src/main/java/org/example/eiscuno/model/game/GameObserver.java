package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;

public interface GameObserver {
    void onCardPlayed(Card playedCard);
    void onPlayerChanged(Player currentPlayer);
    void onGameStarted();
    void onCardDrawn(Player player, Card drawnCard);
    void onGameEnded(Player winner);
}
