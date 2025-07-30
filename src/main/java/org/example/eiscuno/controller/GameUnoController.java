package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.game.WinThread;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.StartStage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

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
    private Thread threadSingUNO;

    private WinThread winThread;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Referencia al gestor principal de pantallas (StartStage) que controla la navegación entre ventanas.
     * Se utiliza para cambiar entre pantallas (ej. de juego a menú principal) y cerrar la aplicación.
     *
     * <p>Esta referencia se inyecta mediante el método {@link #setStageManager(StartStage)}
     * cuando se carga la pantalla.</p>
     */
    private StartStage stageManager;

    /**
     * Establece la referencia al gestor de pantallas principal (inyección de dependencia).
     *
     * @param stageManager Instancia de StartStage que gestiona la navegación entre pantallas
     *
     * <p>Este método es llamado automáticamente por {@link StartStage} cuando carga esta pantalla,
     * permitiendo al controlador solicitar cambios de pantalla.</p>
     *
     * @ejemplo
     * // En StartStage:
     * StartController controller = loader.getController();
     * controller.setStageManager(this);
     */

    public void setStageManager(StartStage stageManager) {
        this.stageManager = stageManager;
    }

    /**
     * Maneja el evento de salida del juego, normalmente vinculado a un botón "Salir" en la UI.
     *
     * @param event Objeto ActionEvent con información sobre el evento de clic
     * @throws IOException Si ocurre un error al cargar la pantalla de inicio
     *
     * <p>Acciones realizadas:
     * <ol>
     *   <li>Regresa a la pantalla de inicio principal usando {@link StartStage#showStartScreen()}</li>
     *   <li>Opcionalmente guarda el estado actual del juego antes de salir</li>
     * </ol></p>
     *
     * @nota La implementación actual solo navega de vuelta al inicio.
     *       Para cerrar completamente la aplicación, usar {@link StartStage#close()}.
     *
     * @ejemplo_uso
     * // En el archivo FXML:
     * <Button text="Salir" onAction="#exitGame" />
     */
    @FXML
    public void exitGame(ActionEvent event) throws IOException {
        stageManager.showStartScreen();
        // Guardar el contenido del juego
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(), this.humanPlayer, this.gameUno,this);
        threadSingUNO = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        threadSingUNO.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView,this.gameUno,this.deck,
                                                    this.humanPlayer,this);
        threadPlayMachine.start();

        winThread = new WinThread(gameUno,machinePlayer,humanPlayer, deck,threadPlayMachine,threadSingUNO);
        winThread.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);//tabla gameUno == tabla del controlador( paso por referencia)
        this.posInitCardToShow = 0;
        gameUno.playCard(deck.takeCard());
        tableImageView.setImage(table.getCurrentCardOnTheTable().getImage());

    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    public void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {


                    //AQUI necesito saber si la carta es de color "choose" para cambiarle el color
                    if (table.canAddCardTable(card)) {

                        if (card.getColor().equals("CHOOSE")) {
                            ColorPickerController controller = new ColorPickerController();
                            String color = controller.showAndWait();
                            card.setColor(color);
                            System.out.println("Color escogido: " + color);
                        }

                        if (card.getValue().equals("TWO_WILD")) {
                            gameUno.eatCard(machinePlayer, 2);
                            System.out.println("\nMachine ate 2 cards");
                        } else if (card.getValue().equals("FOUR_WILD")) {
                            gameUno.eatCard(machinePlayer, 4);
                            System.out.println("\nMachine ate 4 cards");
                        }

                        gameUno.playCard(card);
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));

                        if (card.getValue().equals("SKIP") || card.getValue().equals("RESERVE")) {
                            System.out.println("\nEl jugador sigue en su turno");
                        } else {
                            System.out.println("\nTurno de la maquina");
                            threadPlayMachine.setHasPlayerPlayed(true);
                        }



                    } else {
                        System.out.println("Can't add card");
                    }

                    printCardsHumanPlayer();


            });



            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {


            if (gameUno.mustDrawFromDeck(humanPlayer)) {
                humanPlayer.addCard(this.deck.takeCard());
                threadPlayMachine.setHasPlayerPlayed(true);

                printCardsHumanPlayer();
            } else {
                System.out.println("puedes añadir al menos una carta de tu mazo");
            }


    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Implement logic to handle Uno event here
        threadSingUNOMachine.setPlayerHasSungUno(true);


    }


}
