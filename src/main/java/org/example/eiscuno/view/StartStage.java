package org.example.eiscuno.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.controller.StartController;

import java.io.IOException;

/**
 * Singleton implementation for managing the application's main window.
 * Controls navigation between screens and configuration of the main Stage.
 */
public class StartStage {

    // Single instance for the Singleton pattern
    private static StartStage instance;


    // Reference to the main JavaFX Stage
    private Stage primaryStage;

    /**
     * Private constructor to implement the Singleton pattern.
     * Prevents direct instantiation of the class.
     */
    private StartStage() {
        // The constructor is empty because initialization is done in initialize()
    }

    /**
     * Gets the singleton instance of StartStage (Singleton pattern).
     * @return The singleton instance of StartStage
     */
    public static StartStage getInstance() {
        /**
         * Gets the singleton instance of StartStage (Singleton pattern).
         * @return The singleton instance of StartStage
         */
        if (instance == null) {
            instance = new StartStage();
        }
        return instance;
    }

    /**
     * Initializes the application's main Stage.
     * @param stage The main Stage provided by JavaFX
     */
    public void initialize(Stage stage) {
        this.primaryStage = stage;

    // Basic Stage Configuration
        primaryStage.setResizable(false);
    // Prevents the user from resizing the window
    }

    /**
     * Displays the application's home screen.
     * @throws IOException If an error occurs while loading the FXML file
     */
    public void showStartScreen() throws IOException {
        // Load the FXML file
        try {

        // Checked exception
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/start-view.fxml"));
            Parent root = loader.load(); // Puede lanzar IOException

            // Get the handler - Unchecked exception
            StartController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El archivo FXML no tiene un controlador definido (fx:controller)");
            }
            controller.setStageManager(this);

            // Set up the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menú Principal - EISC Uno");
            primaryStage.setFullScreen(false);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo FXML: " + e.getMessage());
            e.printStackTrace();

        // Optional: Show an alert to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de carga");
            alert.setHeaderText("No se pudo cargar la interfaz");
            alert.setContentText("El archivo de la pantalla inicial no se encontró o está dañado.");
            alert.showAndWait();
        } catch (IllegalStateException e) {
            System.err.println("Error de estado: " + e.getMessage());
            // Terminate the application or recover
            Platform.exit();
        }
    }


    /**
     * Muestra la pantalla del juego UNO.
     * @throws IOException Si ocurre un error al cargar el archivo FXML
     */
    public void showGameScreen() throws IOException {
        // Cargar el archivo FXML del juego
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/game-uno-view.fxml"));
        Parent root = loader.load();

        // Obtener el controlador y configurar el stageManager
        GameUnoController controller = loader.getController();
        controller.setStageManager(this);

        // Configurar y mostrar la escena
        primaryStage.setScene(new Scene(root));
        primaryStage.setFullScreen(true); // Mantener pantalla completa
        primaryStage.setTitle("Juego UNO - EISC Uno");
    }

    /**
     * Cierra la ventana principal y finaliza la aplicación.
     */
    public void close() {
        // Cierra el Stage principal
        if (primaryStage != null) {
            primaryStage.close();
        }

        // Termina el proceso de JavaFX
        Platform.exit();

        System.exit(0);
    }
}

