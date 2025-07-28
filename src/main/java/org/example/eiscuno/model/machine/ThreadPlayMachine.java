package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private GameUno gameUno;
    private Deck deck;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUno gameUno, Deck deck) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.gameUno = gameUno;
        this.deck = deck;
        this.hasPlayerPlayed = false;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            if(hasPlayerPlayed){
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //  Revisar si fue interrumpido después de despertar
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("ThreadPlayMachine interrumpido justo antes de jugar.");
                    break;
                }

                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable(){
        //AMBOS table se refieren al mismo objeto (table en controlador thread y gameUno)

        if (gameUno.isGameOver()) {
            System.out.println("ThreadPlayMachine detectó que el juego terminó.");
            return;
        }

        if (machinePlayer.getCardsPlayer().isEmpty()) {
            System.out.println("Machine player has no cards to play.");
            return;
        }


        if(!gameUno.mustDrawFromDeck(machinePlayer)){

            for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
                Card card = machinePlayer.getCard(i);

                //AQUI necesito saber si la carta es de color "choose" para cambiarle el color
                if (table.canAddCardTable(card)) {
                    table.addCardOnTheTable(card);
                    machinePlayer.removeCard(i);
                    tableImageView.setImage(card.getImage());
                    break;
                }
            }

        }else{
            machinePlayer.addCard(deck.takeCard());
            System.out.println("Machine took a card from the deck");
        }

        for(int i =0; i < machinePlayer.getCardsPlayer().size(); i++){
            System.out.println(machinePlayer.getCardsPlayer().get(i).getUrl() + ",    ");
        }
        System.out.println(" ");

        hasPlayerPlayed = false;
        //ejecutar aqui el comando

    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }
}