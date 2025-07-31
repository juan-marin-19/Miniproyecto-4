package org.example.eiscuno.controller;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.eiscuno.view.ColorPickerView;

/**
 * Controller for displaying a modal color selection dialog.
 * <p>
 * It shows a view with multiple rectangles representing colors and waits
 * for the user to click on one. Once a color is selected, the dialog closes
 * and the color name is returned.
 * </p>
 */

import java.util.List;


public class ColorPickerController {

    private String selectedColor = null;
    /**
     * Names of the available colors, in the same order as the rectangles in the view.
     */
    private final String[] colorNames = {"RED", "BLUE", "GREEN", "YELLOW"};
    /**
     * Displays a modal dialog that blocks the UI until the user
     * selects a color by clicking one of the rectangles.
     *
     * @return the name of the selected color (e.g., "RED", "BLUE", etc.),
     *         or {@code null} if no color was selected.
     */
    public String showAndWait() {
        ColorPickerView view = new ColorPickerView();
        List<Rectangle> rects = view.getColorRects();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Selecciona un color");
        dialogStage.setScene(new Scene(view.getView(), 400, 100));

        dialogStage.setOnCloseRequest(event -> {
            event.consume(); // Evita que se cierre
        });
// Assign click handlers to each rectangle to capture color selection
        for (int i = 0; i < rects.size(); i++) {
            int index = i;
            rects.get(i).setOnMouseClicked((MouseEvent e) -> {
                selectedColor = colorNames[index];
                dialogStage.close();
            });
        }

        dialogStage.showAndWait();
        return selectedColor;
    }
}
