package agh.cs.oop.gui;

import agh.cs.oop.engine.Alien;
import agh.cs.oop.engine.Mushroom;
import agh.cs.oop.engine.Vector2d;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class MapVisualizer {
    private final int mapWidth;
    private final int mapHeight;
    private final int startEnergy;
    private final int gridSize;
    private final ObservableList<Alien> aliens;
    private final ObservableSet<Mushroom> mushrooms;
    private final Vector2d jungleUpperLeft;
    private final Vector2d jungleLowerRight;
    public final GridPane gridMap = new GridPane();
    private StackPane[][] mapFields;

    public MapVisualizer(Settings settings, Vector2d jungleUpperLeft, Vector2d jungleLowerRight,
                         ObservableList<Alien> aliens, ObservableSet<Mushroom> mushrooms) {
        this.mapWidth = settings.getMapWidth();
        this.mapHeight = settings.getMapHeight();
        this.startEnergy = settings.getStartEnergy();

        this.gridSize = settings.getGridSize();
        this.jungleUpperLeft = jungleUpperLeft;
        this.jungleLowerRight = jungleLowerRight;
        this.aliens = aliens;
        this.mushrooms = mushrooms;

        handleAnimalsUpdates();
        handleMushroomsUpdates();

        prepareFields();
        generateGridMap();
    }

    // Add listener to the ObservableList of aliens to handle updates
    private void handleAnimalsUpdates() {
        this.aliens.addListener((ListChangeListener.Change<? extends Alien> listChange) -> {
            while (listChange.next()) {
                if (listChange.wasAdded()) {
                    for (Alien addedAlien : listChange.getAddedSubList()) {
                        this.addAlienToField(addedAlien);

                        addedAlien.getPositionProperty().addListener((observable, oldPos, newPos) ->
                                positionChanged(oldPos, newPos)
                        );
                    }
                } else if (listChange.wasRemoved()) {
                    for (Alien removedAlien : listChange.getRemoved()) {
                        this.removeAlienFromField(removedAlien);
                    }
                }
            }
        });
    }

    // Add listener to the ObservableSet of mushrooms to handle updates
    private void handleMushroomsUpdates() {
        this.mushrooms.addListener((SetChangeListener.Change<? extends Mushroom> setChange) -> {
            if (setChange.wasAdded()) {
                this.addMushroomToField(setChange.getElementAdded());
            } else if (setChange.wasRemoved()) {
                this.removeMushroomFromField(setChange.getElementRemoved());
            }
        });
    }

    // Prepare a visualizer of the new mushroom and add it to the field
    private void addMushroomToField(Mushroom mushroom) {
        MushroomVisualizer mushroomVisualizer = new MushroomVisualizer(mushroom, (int) (this.gridSize * 0.38));

        Vector2d mushroomPos = mushroom.getPosition();

        this.mapFields[mushroomPos.getX()][mushroomPos.getY()].getChildren().add(mushroomVisualizer);
        this.mapFields[mushroomPos.getX()][mushroomPos.getY()].setAlignment(mushroomVisualizer, Pos.BOTTOM_LEFT);

        this.cleanField(mushroomPos.getX(), mushroomPos.getY());
    }

    // Remove icon of the mushroom from the field
    private void removeMushroomFromField(Mushroom mushroom) {
        // Get coordinates of mushroom's position
        int posX = mushroom.getPosition().getX();
        int posY = mushroom.getPosition().getY();

        // Find the visualizer of the mushroom to remove
        MushroomVisualizer foundMushroomVisualizer = null;

        for (Node childrenNode : this.mapFields[posX][posY].getChildren()) {
            if (childrenNode instanceof MushroomVisualizer) {
                MushroomVisualizer mushroomVisualizer = (MushroomVisualizer) childrenNode;

                if (mushroomVisualizer.mushroom.getPosition() == mushroom.getPosition()) {
                    foundMushroomVisualizer = mushroomVisualizer;
                }
            }
        }

        // Make sure that we found the mushroom
        if (foundMushroomVisualizer == null) {
            throw new RuntimeException("Mushroom on field (" + posX + "," + posY + ") can not be found");
        }

        // Remove the mushroom from his field
        this.mapFields[posX][posY].getChildren().remove(foundMushroomVisualizer);
        this.cleanField(posX, posY);
    }

    // Prepare a visualizer of the new alien and add it to the field
    private void addAlienToField(Alien alien) {
        AlienVisualizer alienVisualizer = new AlienVisualizer(alien, (int) (this.gridSize * 0.63), this.startEnergy);

        this.mapFields[alien.getPosition().getX()][alien.getPosition().getY()].getChildren().add(alienVisualizer);
        this.cleanField(alien.getPosition().getX(), alien.getPosition().getY());
    }

    // Remove icon of the alien from the field
    private void removeAlienFromField(Alien alien) {
        // Get coordinates of animal's position
        int posX = alien.getPosition().getX();
        int posY = alien.getPosition().getY();

        // Find the visualizer of the animal to remove
        AlienVisualizer foundAlienVisualizer = null;

        for (Node childrenNode : this.mapFields[posX][posY].getChildren()) {
            if (childrenNode instanceof AlienVisualizer) {
                AlienVisualizer alienVisualizer = (AlienVisualizer) childrenNode;

                if (alienVisualizer.getAlien().getPosition() == alien.getPosition()) {
                    foundAlienVisualizer = alienVisualizer;
                }
            }
        }

        // Make sure that we found the animal
        if (foundAlienVisualizer == null) {
            throw new RuntimeException("Alien on field (" + posX + "," + posY + ") can not be found");
        }

        // Remove the animal from his field
        this.mapFields[posX][posY].getChildren().remove(foundAlienVisualizer);
        this.cleanField(posX, posY);
    }

    // Handle changes of animal's position
    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        // Find the visualizer of the animal which moved
        AlienVisualizer foundAlienVisualizer = null;

        for (Node childrenNode : this.mapFields[oldPosition.getX()][oldPosition.getY()].getChildren()) {
            if (childrenNode instanceof AlienVisualizer) {
                AlienVisualizer alienVisualizer = (AlienVisualizer) childrenNode;

                if (alienVisualizer.getAlien().getPosition() == newPosition) {
                    foundAlienVisualizer = alienVisualizer;
                }
            }
        }

        // Make sure that we found the animal which moved
        if (foundAlienVisualizer == null) {
            throw new RuntimeException("Alien which moved from " + oldPosition + " to " + newPosition + " can not be found!");
        }

        // Remove the animal from existing field
        this.mapFields[oldPosition.getX()][oldPosition.getY()].getChildren().remove(foundAlienVisualizer);
        this.cleanField(oldPosition.getX(), oldPosition.getY());

        // Add the visualizer to a new field and clean it
        this.mapFields[newPosition.getX()][newPosition.getY()].getChildren().add(foundAlienVisualizer);
        this.cleanField(newPosition.getX(), newPosition.getY());
    }

    // Find the strongest, set the label if necessary and set proper alignment of alien
    private void cleanField(int posX, int posY) {
        // Count aliens on the field, get the label if exists, get the mushroom if exists and find the strongest alien
        int aliensCount = 0;
        Label aliensCountLabel = null;
        AlienVisualizer strongestAlienVisualizer = null;
        MushroomVisualizer mushroomVisualizer = null;

        for (Node childrenNode : this.mapFields[posX][posY].getChildren()) {
            if (childrenNode instanceof AlienVisualizer) {
                aliensCount += 1;

                AlienVisualizer existingAlienVisualizer = (AlienVisualizer) childrenNode;
                existingAlienVisualizer.setVisible(false);

                if (strongestAlienVisualizer == null) {
                    strongestAlienVisualizer = existingAlienVisualizer;
                } else if (existingAlienVisualizer.getAlien().getEnergy() > strongestAlienVisualizer.getAlien().getEnergy()) {
                    strongestAlienVisualizer = existingAlienVisualizer;
                }
            } else if (childrenNode instanceof MushroomVisualizer) {
                mushroomVisualizer = (MushroomVisualizer) childrenNode;
            } else if (childrenNode instanceof Label) {
                aliensCountLabel = (Label) childrenNode;
            }
        }

        // Make sure that we found the alien if there is something on the field
        if (strongestAlienVisualizer == null && aliensCount >= 1) {
            throw new RuntimeException("Alien on field (" + posX + ", " + posY + ") can not be found!");
        }

        // Add label to the field with quantity of animals on this field if necessary
        if (aliensCount >= 2) {
            if (aliensCountLabel != null) {
                aliensCountLabel.setText(String.valueOf(aliensCount));
            } else {
                aliensCountLabel = new Label(String.valueOf(aliensCount));
                aliensCountLabel.getStyleClass().add("gameFieldLabel");

                this.mapFields[posX][posY].getChildren().add(aliensCountLabel);
                this.mapFields[posX][posY].setAlignment(aliensCountLabel, Pos.TOP_LEFT);
            }
        } else if (aliensCount < 2 && aliensCountLabel != null) {
            aliensCountLabel.setText("");
        }

        // Make the strongest visible if it exists
        if (strongestAlienVisualizer != null) {
            strongestAlienVisualizer.setVisible(true);
            this.mapFields[posX][posY].setAlignment(strongestAlienVisualizer, Pos.CENTER);
        }

        // Set proper alignment
        if (aliensCount >= 2 || (aliensCount >= 1 && mushroomVisualizer != null)) {
            this.mapFields[posX][posY].setAlignment(strongestAlienVisualizer, Pos.CENTER_RIGHT);
        }
    }

    // Prepare fields for animals and mushrooms on the map
    private void prepareFields() {
        this.mapFields = new StackPane[this.mapWidth][this.mapHeight];

        for (int x = 0; x < this.mapWidth; x++) {
            for (int y = 0; y < this.mapHeight; y++) {
                this.mapFields[x][y] = new StackPane();
                this.mapFields[x][y].setMaxSize(this.gridSize, this.gridSize);

                // Check whether it is a jungle
                Vector2d fieldVector = new Vector2d(x, y);
                if (fieldVector.follows(this.jungleUpperLeft) && fieldVector.precedes(this.jungleLowerRight)) {
                    this.mapFields[x][y].getStyleClass().add("gameFieldJungle");
                } else {
                    this.mapFields[x][y].getStyleClass().add("gameField");
                }
            }
        }
    }

    // Generate map with grid lines for fields
    private void generateGridMap() {
        // Add constraints to form the grid
        for (int x = 0 ; x < this.mapWidth ; x++) {
            ColumnConstraints cc = new ColumnConstraints(this.gridSize);
            this.gridMap.getColumnConstraints().add(cc);
        }

        for (int y = 0 ; y < this.mapHeight ; y++) {
            RowConstraints rc = new RowConstraints(this.gridSize);
            this.gridMap.getRowConstraints().add(rc);
        }

        // Add StackPane to all fields
        for (int x = 0 ; x < this.mapWidth ; x++) {
            for (int y = 0 ; y < this.mapHeight ; y++) {
                this.gridMap.add(this.mapFields[x][y], x, this.mapHeight - 1 - y);
            }
        }

        this.gridMap.getStyleClass().add("gameGrid");
    }
}
