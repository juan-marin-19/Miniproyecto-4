package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
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

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView,this.gameUno,this.deck,this.humanPlayer,this);
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
    }

    /**
     * Prints the human player's cards on the grid pane and own exception.
     */
    public void printCardsHumanPlayer() {
        try {
            // Limpiar el GridPane
            this.gridPaneCardsPlayer.getChildren().clear();

            // Validar que los componentes esenciales estén inicializados
            if (this.gameUno == null || this.table == null || this.humanPlayer == null) {
                throw new IllegalStateException("Componentes del juego no inicializados correctamente");
            }

            // Obtener cartas visibles del jugador humano
            Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

            if (currentVisibleCardsHumanPlayer == null) {
                throw new InvalidCardPlayException("No se pudieron cargar las cartas del jugador");
            }

            for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
                Card card = currentVisibleCardsHumanPlayer[i];
                if (card == null) {
                    throw new InvalidCardPlayException("Carta nula encontrada en la posición " + i);
                }

                ImageView cardImageView = card.getCard();
                if (cardImageView == null) {
                    throw new InvalidCardPlayException("La imagen de la carta no está disponible");
                }

                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    try {
                        if (!table.canAddCardTable(card)) {
                            throw new InvalidCardPlayException("No puedes jugar la carta " + card.getValue() +
                                    " de color " + card.getColor() +
                                    " en este momento");
                        }

                        // Manejo de cartas especiales CHOOSE
                        if (card.getColor().equals("CHOOSE")) {
                            ColorPickerController controller = new ColorPickerController();
                            String color = controller.showAndWait();

                            if (color == null || color.isEmpty()) {
                                throw new InvalidCardPlayException("Debes seleccionar un color para la carta especial");
                            }
                            card.setColor(color);
                            System.out.println("Color escogido: " + color);
                        }

                        // Manejo de cartas WILD
                        if (card.getValue().equals("TWO_WILD")) {
                            gameUno.eatCard(machinePlayer, 2);
                            System.out.println("\nMachine ate 2 cards");
                        } else if (card.getValue().equals("FOUR_WILD")) {
                            gameUno.eatCard(machinePlayer, 4);
                            System.out.println("\nMachine ate 4 cards");
                        }

                        // Jugar la carta
                        gameUno.playCard(card);
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));

                        // Manejo de turnos especiales
                        if (card.getValue().equals("SKIP") || card.getValue().equals("RESERVE")) {
                            System.out.println("\nEl jugador sigue en su turno");
                        } else {
                            System.out.println("\nTurno de la maquina");
                            threadPlayMachine.setHasPlayerPlayed(true);
                        }

                    } catch (InvalidCardPlayException e) {
                        System.err.println("Error al jugar carta: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error inesperado: " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        // Actualizar la vista de todas formas
                        Platform.runLater(() -> printCardsHumanPlayer());
                    }
                });

                this.gridPaneCardsPlayer.add(cardImageView, i, 0);
            }
        } catch (InvalidCardPlayException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error crítico");
                alert.setHeaderText("No se pueden mostrar las cartas");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
            System.err.println("Error crítico al imprimir cartas: " + e.getMessage());
        } catch (Exception e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error inesperado");
                alert.setHeaderText("Ocurrió un problema grave");
                alert.setContentText("El juego no puede continuar: " + e.getMessage());
                alert.showAndWait();
            });
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
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
