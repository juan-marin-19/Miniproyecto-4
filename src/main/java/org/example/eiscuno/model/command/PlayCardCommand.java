package org.example.eiscuno.model.command;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class PlayCardCommand implements Command {
    private Player player;
    private Table table;
    private Card card;
    private int index;

    public PlayCardCommand(Player player, Table table, Card card, int index) {
        this.player = player;
        this.table = table;
        this.card = card;
        this.index = index;
    }

    @Override
    public void execute() {
        player.removeCard(index);
        table.addCardOnTheTable(card);
        System.out.println(player.getTypePlayer() + " jugó: " + card);
    }
}