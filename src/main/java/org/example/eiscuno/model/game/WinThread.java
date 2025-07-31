package org.example.eiscuno.model.game;

import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
/**
 * Thread that monitors win conditions for the Uno game.
 * It continuously checks whether the machine player, human player, or deck has run out of cards.
 * When any of those terminal conditions occur, it marks the game as ended and interrupts the
 * other worker threads to stop game activity.
 */
public class WinThread extends Thread{

    private Player machinePlayer;
    private Player humanPlayer;
    private Deck deck;
    private Thread threadSingUNO;
    private Thread threadPlayMachine;
    private GameUno gameUno;

    public WinThread(GameUno gameUno, Player machinePlayer, Player humanPlayer, Deck deck, Thread threadPlayMachine, Thread threadSingUNO) {
        this.machinePlayer = machinePlayer;
        this.humanPlayer = humanPlayer;
        this.deck = deck;
        this.threadPlayMachine = threadPlayMachine;
        this.threadSingUNO = threadSingUNO;
        this.gameUno = gameUno;

    }
    /**
     * Main loop that periodically checks for end-of-game conditions.
     * If the machine or player has no cards left, or the deck is empty, the game is marked
     * as ended, and the auxiliary threads are interrupted to halt further actions.
     */
    public void run() {
        while (true) {
            if (machinePlayer.getCardsPlayer().isEmpty()) {
                System.out.println("Machine gano");
                gameUno.setGameEnded(true);
                threadPlayMachine.interrupt();
                threadSingUNO.interrupt();
                break;

            } else if (humanPlayer.getCardsPlayer().isEmpty()) {
                System.out.println("Human gano");
                gameUno.setGameEnded(true);  // ¡Aquí lo agregas también!
                threadPlayMachine.interrupt();
                threadSingUNO.interrupt();
                break;

            } else if (deck.isEmpty()) {
                System.out.println("El deck se vacio");
                gameUno.setGameEnded(true);  // Y aquí también
                threadPlayMachine.interrupt();
                threadSingUNO.interrupt();
                break;
            }

            try {
                Thread.sleep(500); // revisar cada medio segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
