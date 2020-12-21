package agh.cs.oop.gui;

import agh.cs.oop.engine.Alien;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlienVisualizer extends ImageView {
    private final Alien alien;
    private final int startEnergy;

    public AlienVisualizer(Alien alien, int animalSize, int startEnergy) {
        this.alien = alien;
        this.startEnergy = startEnergy;

        this.setProperIcon(this.alien.getEnergy());

        // Add listener to the animal's energy to handle changes
        this.alien.getEnergyProperty().addListener((observable, oldValue, newValue) -> setProperIcon(newValue.intValue()));

        // Set proper size and preserve ratio
        this.setFitWidth(animalSize);
        this.setFitHeight(animalSize);
        this.setPreserveRatio(true);
    }

    // Set proper icon of the animal on the basis of animal's energy
    private void setProperIcon(int animalEnergy) {
        String iconPath = "resources/images/";
        double fractionOfInitial = (double) animalEnergy / this.startEnergy;

        if (fractionOfInitial >= 0.9) {
            iconPath += "alien90.png";
        } else if (fractionOfInitial >= 0.8) {
            iconPath += "alien80.png";
        } else if (fractionOfInitial >= 0.7) {
            iconPath += "alien70.png";
        } else if (fractionOfInitial >= 0.6) {
            iconPath += "alien60.png";
        } else if (fractionOfInitial >= 0.5) {
            iconPath += "alien50.png";
        } else if (fractionOfInitial >= 0.4) {
            iconPath += "alien40.png";
        } else if (fractionOfInitial >= 0.3) {
            iconPath += "alien30.png";
        } else if (fractionOfInitial >= 0.2) {
            iconPath += "alien20.png";
        } else if (fractionOfInitial >= 0.1) {
            iconPath += "alien10.png";
        } else
            iconPath += "alien0.png";

        Image alienIcon = new Image(getClass().getResource(iconPath).toExternalForm());
        this.setImage(alienIcon);
    }

    public Alien getAlien() {
        return this.alien;
    }
}
