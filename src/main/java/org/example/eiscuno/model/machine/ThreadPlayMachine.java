    package org.example.eiscuno.model.machine;
    
    import javafx.scene.control.Label;
    import javafx.scene.image.ImageView;
    import org.example.eiscuno.controller.ColorPickerController;
    import org.example.eiscuno.controller.GameUnoController;
    import org.example.eiscuno.model.card.Card;
    import org.example.eiscuno.model.deck.Deck;
    import org.example.eiscuno.model.game.GameUno;
    import org.example.eiscuno.model.player.Player;
    import org.example.eiscuno.model.table.Table;
    import javafx.application.Platform;


    public class ThreadPlayMachine extends Thread {
        private Table table;
        private Player machinePlayer;
        private Player humanPlayer;
        private ImageView tableImageView;
        private GameUno gameUno;
        private Deck deck;
        private GameUnoController controller; // NUEVA línea arriba
        private Label messageLabel;

        private volatile boolean hasPlayerPlayed;

        private final String[] colorNames = {"RED", "BLUE", "GREEN", "YELLOW"};
    
    
        public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, GameUno gameUno,
                                 Deck deck, Player humanPlayer, GameUnoController controller, Label messageLabel) {
            this.table = table;
            this.machinePlayer = machinePlayer;
            this.humanPlayer = humanPlayer;
            this.tableImageView = tableImageView;
            this.gameUno = gameUno;
            this.deck = deck;
            this.hasPlayerPlayed = false;
            this.controller = controller; // añade esto
            this.messageLabel = messageLabel;

        }
    
        public void run() {
            while (!Thread.currentThread().isInterrupted()){
                if(hasPlayerPlayed){
                    try{
                        Thread.sleep(3000);
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
                    if (table.canAddCardTable(card)) {

                        //necesito saber si la carta es de color "choose" para cambiarle el color
                        if (card.getColor().equals("CHOOSE")) {
                            int color = (int)(Math.random() * 4);
                            card.setColor(colorNames[color]);
                            System.out.println("Color escogido: " + colorNames[color]);


                        }
    
                        if(card.getValue().equals("TWO_WILD")){
                            gameUno.eatCard(humanPlayer,2);
                            Platform.runLater(() -> controller.printCardsHumanPlayer());
                        } else if (card.getValue().equals("FOUR_WILD")) {
                            gameUno.eatCard(humanPlayer,4);
                            Platform.runLater(() -> controller.printCardsHumanPlayer());
                        }

                        Platform.runLater(() -> messageLabel.setText("Color actual: " + card.getColor()));
                        table.addCardOnTheTable(card);
                        machinePlayer.removeCard(i);
                        tableImageView.setImage(card.getImage());
    
                        // Verifica si la carta fue SKIP o RESERVE
                        if (card.getValue().equals("SKIP") || card.getValue().equals("RESERVE")) {
                            System.out.println("Machine mantiene su turno.");
                            Platform.runLater(() -> controller.showTemporaryMessage("Machine mantiene su turno.", 1000));

                            try {
                                Thread.sleep(3000); // pequeño delay antes de volver a jugar
                            } catch (InterruptedException e) {
                                return;
                            }
                            putCardOnTheTable(); // vuelve a jugar
                        } else {
                            hasPlayerPlayed = false;
                        }
    
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
    
           // hasPlayerPlayed = false;
            //ejecutar aqui el comando
    
        }
    
        public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
            this.hasPlayerPlayed = hasPlayerPlayed;
        }
    }