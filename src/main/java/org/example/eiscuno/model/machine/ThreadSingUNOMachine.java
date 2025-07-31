package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;
/**
 * Runnable that monitors when the human player has exactly one card and,
 * if they haven't already announced "UNO", forces them to draw a penalty card.
 * This simulates the game rule where failing to call UNO when having one card
 * results in a penalty. It uses a small randomized delay before applying the penalty
 * to mimic asynchronous detection.
 */
public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private Player humanPlayer;
    private volatile boolean playerHasSungUno = false;
    private GameUno gameUno;
    private GameUnoController controller; // NUEVA línea arriba



    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, Player humanPlayer, GameUno gameUno,GameUnoController controller) {
        this.cardsPlayer = cardsPlayer;
        this.humanPlayer = humanPlayer;
        this.gameUno = gameUno;
        this.controller = controller;
    }
    /**
     * Main loop that keeps checking the player's hand size.
     * If the player has exactly one card and hasn't "sung UNO", applies the penalty.
     * Resets the flag when the player has more than one card.
     * The loop exits when the thread is interrupted.
     */
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {

            if ( cardsPlayer.size() ==1 ) {
                try {
                    Thread.sleep((long) (Math.random() * 2000) + 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!playerHasSungUno) {
                    hasOneCardTheHumanPlayer();
                }

            }
            else if(cardsPlayer.size() > 1) {
                playerHasSungUno = false;
            }
        }

    }
    /**
     * Applies the penalty for failing to call UNO: forces the human player to draw one card,
     * updates the UI, and marks that the player has "sung UNO" to avoid repeated penalties.
     * This is only invoked when the player currently has exactly one card.
     */
    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){

            System.out.println("UNO");

        //I make the player eat a card implement(implement how to make the player eat a card and the method to eat)
            gameUno.eatCard(humanPlayer,1);

        // IMPLEMENT method to print the player's cards
            Platform.runLater(() -> controller.printCardsHumanPlayer());

            playerHasSungUno = true;
        }
    }

    public void setPlayerHasSungUno(boolean playerHasSungUno) {this.playerHasSungUno = playerHasSungUno;}
}