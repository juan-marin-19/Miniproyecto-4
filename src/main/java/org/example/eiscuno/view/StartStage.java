package org.example.eiscuno.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.controller.StartController;

import java.io.IOException;

/**
 * Implementación Singleton para gestionar la ventana principal de la aplicación.
 * Controla la navegación entre pantallas y la configuración del Stage principal.
 */
public class StartStage {
    // Instancia única para el patrón Singleton
    private static StartStage instance;

    // Referencia al Stage principal de JavaFX
    private Stage primaryStage;

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Evita la instanciación directa de la clase.
     */
    private StartStage() {
        // El constructor está vacío porque la inicialización se hace en initialize()
    }

    /**
     * Obtiene la instancia única de StartStage (patrón Singleton).
     * @return La instancia única de StartStage
     */
    public static StartStage getInstance() {
        // Creación perezosa: solo se instancia cuando se solicita por primera vez
        if (instance == null) {
            instance = new StartStage();
        }
        return instance;
    }

    /**
     * Inicializa el Stage principal de la aplicación.
     * @param stage El Stage principal proporcionado por JavaFX
     */
    public void initialize(Stage stage) {
        this.primaryStage = stage;
        // Configuración básica del Stage
        primaryStage.setResizable(false); // Evita que el usuario redimensione la ventana
    }

    /**
     * Muestra la pantalla de inicio de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML
     */
    public void showStartScreen() throws IOException {
        // Cargar el archivo FXML de la pantalla de inicio
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/start-view.fxml"));
        Parent root = loader.load();

        // Obtener el controlador y configurar el stageManager
        StartController controller = loader.getController();
        controller.setStageManager(this);

        // Configurar y mostrar la escena
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Menú Principal - EISC Uno");
        primaryStage.setFullScreen(true); // Pantalla completa
        primaryStage.show();
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

