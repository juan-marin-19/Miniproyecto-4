package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.exception.InvalidCardPlayException;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.game.WinThread;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.StartStage;

import java.io.*;
import java.util.Optional;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private transient Thread threadSingUNO;

    private WinThread winThread;
    private transient ThreadPlayMachine threadPlayMachine;

    private transient StartStage stageManager;

    public void setStageManager(StartStage stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    public void exitGame(ActionEvent event) throws IOException {
        stageManager.showStartScreen();
    }

    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this.humanPlayer, this.gameUno, this);
        threadSingUNO = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        threadSingUNO.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno, this.deck, this.humanPlayer, this);
        threadPlayMachine.start();

        winThread = new WinThread(gameUno, machinePlayer, humanPlayer, deck, threadPlayMachine, threadSingUNO);
        winThread.start();
    }

    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
        gameUno.playCard(deck.takeCard());
        tableImageView.setImage(table.getCurrentCardOnTheTable().getImage());
    }

    public void printCardsHumanPlayer() {
        try {
            this.gridPaneCardsPlayer.getChildren().clear();

            if (this.gameUno == null || this.table == null || this.humanPlayer == null) {
                throw new IllegalStateException("Componentes del juego no inicializados correctamente");
            }

            Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

            if (currentVisibleCardsHumanPlayer == null) {
                throw new InvalidCardPlayException("No se pudieron cargar las cartas del jugador");
            }

            for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
                Card card = currentVisibleCardsHumanPlayer[i];
                if (card == null) throw new InvalidCardPlayException("Carta nula en posición " + i);

                ImageView cardImageView = card.getCard();
                if (cardImageView == null) throw new InvalidCardPlayException("Imagen de carta no disponible");

                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    try {
                        if (!table.canAddCardTable(card)) {
                            throw new InvalidCardPlayException("No puedes jugar la carta " + card.getValue() + " de color " + card.getColor());
                        }

                        if (card.getColor().equals("CHOOSE")) {
                            ColorPickerController controller = new ColorPickerController();
                            String color = controller.showAndWait();
                            if (color == null || color.isEmpty()) {
                                throw new InvalidCardPlayException("Debes seleccionar un color para la carta especial");
                            }
                            card.setColor(color);
                            registrarEventoEnArchivo("Jugador eligió el color: " + color);
                        }

                        if (card.getValue().equals("TWO_WILD")) {
                            gameUno.eatCard(machinePlayer, 2);
                            registrarEventoEnArchivo("Machine comió 2 cartas");
                        } else if (card.getValue().equals("FOUR_WILD")) {
                            gameUno.eatCard(machinePlayer, 4);
                            registrarEventoEnArchivo("Machine comió 4 cartas");
                        }

                        gameUno.playCard(card);
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));

                        if (card.getValue().equals("SKIP") || card.getValue().equals("RESERVE")) {
                            registrarEventoEnArchivo("Jugador retiene su turno");
                        } else {
                            registrarEventoEnArchivo("Turno de la máquina");
                            threadPlayMachine.setHasPlayerPlayed(true);
                        }

                    } catch (InvalidCardPlayException e) {
                        System.err.println("Error al jugar carta: " + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(this::printCardsHumanPlayer);
                    }
                });

                this.gridPaneCardsPlayer.add(cardImageView, i, 0);
            }

        } catch (Exception e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error mostrando cartas");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
            e.printStackTrace();
        }
    }

    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    @FXML
    void onHandleTakeCard(ActionEvent event) {
        if (gameUno.mustDrawFromDeck(humanPlayer)) {
            humanPlayer.addCard(this.deck.takeCard());
            threadPlayMachine.setHasPlayerPlayed(true);
            registrarEventoEnArchivo("Jugador tomó una carta del mazo");
            printCardsHumanPlayer();
        } else {
            registrarEventoEnArchivo("Jugador puede jugar una carta, no necesita tomar");
        }
    }

    @FXML
    void onHandleUno(ActionEvent event) {
        threadSingUNOMachine.setPlayerHasSungUno(true);
        registrarEventoEnArchivo("Jugador dijo ¡UNO!");
    }

    private void registrarEventoEnArchivo(String mensaje) {
        try (FileWriter fw = new FileWriter("eventos_uno.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(mensaje);
        } catch (IOException e) {
            System.err.println("Error escribiendo en archivo: " + e.getMessage());
        }
    }
}