package org.example.eiscuno.model.game;

import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;

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
