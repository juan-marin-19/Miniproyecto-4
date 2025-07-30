package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.eiscuno.view.StartStage;

import java.io.IOException;

public class StartController {

    @FXML private Button playButton;
    @FXML private Button continueButton;
    @FXML private Button exitButton;

    @FXML
    public void initialize() {

    }

    private StartStage stageManager;


    public void setStageManager(StartStage stageManager) {
        this.stageManager = stageManager;
    }
    @FXML
    public void startGame(ActionEvent event) throws IOException {
        stageManager.showGameScreen();
    }

    @FXML
    public void continueGame(ActionEvent event) throws IOException {
        // Implementar la continuacion del juego.
    }

    @FXML
    public void exitGame(ActionEvent event) {
        stageManager.close();
    }
}
