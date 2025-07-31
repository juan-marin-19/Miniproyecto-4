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
        // Cargar el archivo FXML
        try {
            // Excepecion marcada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/start-view.fxml"));
            Parent root = loader.load(); // Puede lanzar IOException

            // Obtener el controlador - Excepecion no marcada
            StartController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El archivo FXML no tiene un controlador definido (fx:controller)");
            }
            controller.setStageManager(this);

            // Configurar la escena
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menú Principal - EISC Uno");
            primaryStage.setFullScreen(false);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar el archivo FXML: " + e.getMessage());
            e.printStackTrace();
            // Opcional: Mostrar un alert al usuario
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de carga");
            alert.setHeaderText("No se pudo cargar la interfaz");
            alert.setContentText("El archivo de la pantalla inicial no se encontró o está dañado.");
            alert.showAndWait();
        } catch (IllegalStateException e) {
            System.err.println("Error de estado: " + e.getMessage());
            // Terminar la aplicación o recuperarse
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

