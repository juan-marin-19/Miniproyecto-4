package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.eiscuno.view.StartStage;

import java.io.IOException;
/**
 * Controller for the start screen of the Uno application.
 * Handles user actions from the initial menu such as starting a new game,
 * continuing a previous game, or exiting the application.
 */

public class StartController {

    @FXML private Button playButton;
    @FXML private Button continueButton;
    @FXML private Button exitButton;

    @FXML
    public void initialize() {

    }

    private StartStage stageManager;
    /**
     * Called automatically after the FXML is loaded. Currently no initialization logic is required.
     */

    public void setStageManager(StartStage stageManager) {
        this.stageManager = stageManager;
    }
    @FXML
    public void startGame(ActionEvent event) throws IOException {
        stageManager.showGameScreen();
    }
    /**
     * Handles the action to continue a previously started game.
     * <p>
     * Implementation is pending; this should restore the previous game state
     * and transition to the appropriate screen.
     * </p>
     *
     * @param event the triggered action event
     * @throws IOException if restoring or loading the game fails
     */
    @FXML
    public void continueGame(ActionEvent event) throws IOException {

    }

    @FXML
    public void exitGame(ActionEvent event) {
        stageManager.close();
    }
}
