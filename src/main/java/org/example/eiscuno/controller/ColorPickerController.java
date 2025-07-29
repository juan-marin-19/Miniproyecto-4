package org.example.eiscuno.controller;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.eiscuno.view.ColorPickerView;

import java.util.List;

public class ColorPickerController {

    private String selectedColor = null;
    private final String[] colorNames = {"RED", "BLUE", "GREEN", "YELLOW"};

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
