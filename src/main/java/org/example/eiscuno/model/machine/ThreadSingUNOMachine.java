package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private volatile boolean playerHasSungUno = false;


    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
    }

    @Override
    public void run(){
        while (true) {
            if ( cardsPlayer.size() ==1 ) {
                try {
                    Thread.sleep((long) (Math.random() * 5000));
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
            playerHasSungUno = true;
        }
    }

    public void setPlayerHasSungUno(boolean playerHasSungUno) {this.playerHasSungUno = playerHasSungUno;}
}