package agh.cs.oop.gui;

import agh.cs.oop.engine.Mushroom;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MushroomVisualizer extends ImageView {
    public Mushroom mushroom;

    public MushroomVisualizer(Mushroom mushroom, int mushroomSize) {
        this.mushroom = mushroom;

        // Set the icon of the mushroom
        String iconPath = "resources/images/mushroom.png";
        Image mushroomIcon = new Image(getClass().getResource(iconPath).toExternalForm());
        this.setImage(mushroomIcon);

        // Set proper size and preserve ratio
        this.setFitWidth(mushroomSize);
        this.setFitHeight(mushroomSize);
        this.setPreserveRatio(true);
    }
}
