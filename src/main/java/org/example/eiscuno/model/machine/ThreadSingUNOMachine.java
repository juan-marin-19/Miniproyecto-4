package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

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

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){

            System.out.println("UNO");
            //hago comer una carta al jugador implementar( implementar como hacer comer una carta al jugador y el metodo para comer)
            gameUno.eatCard(humanPlayer,1);
            // IMPLEMENTAR metodo para printear las cartas del jugadorr
            Platform.runLater(() -> controller.printCardsHumanPlayer());

            playerHasSungUno = true;
        }
    }

    public void setPlayerHasSungUno(boolean playerHasSungUno) {this.playerHasSungUno = playerHasSungUno;}
}