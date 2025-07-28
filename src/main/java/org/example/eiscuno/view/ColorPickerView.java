package org.example.eiscuno.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class ColorPickerView {


        private final HBox colorBox = new HBox(15);
        private final List<Rectangle> colorRects;

        public ColorPickerView() {
            colorBox.setPadding(new Insets(20));
            colorBox.setAlignment(Pos.CENTER);

            Rectangle red = createRect(Color.RED);
            Rectangle blue = createRect(Color.BLUE);
            Rectangle green = createRect(Color.GREEN);
            Rectangle yellow = createRect(Color.GOLD);

            colorRects = List.of(red, blue, green, yellow);
            colorBox.getChildren().addAll(colorRects);
        }

        private Rectangle createRect(Color color) {
            Rectangle rect = new Rectangle(70, 70, color);
            rect.setStroke(Color.BLACK);
            rect.setArcWidth(15);
            rect.setArcHeight(15);
            return rect;
        }

        public HBox getView() {
            return colorBox;
        }

        public List<Rectangle> getColorRects() {
            return colorRects;
        }


}
