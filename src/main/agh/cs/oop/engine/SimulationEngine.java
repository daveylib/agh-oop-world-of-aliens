package agh.cs.oop.engine;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import javafx.util.Duration;
import agh.cs.oop.gui.AlertHelper;
import agh.cs.oop.gui.Settings;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SimulationEngine {
    private final Settings settings;
    public final GameMap gameMap;
    private final ObservableList<Alien> aliveAliens = FXCollections.observableArrayList(
            alien -> new Observable[] {
                    alien.getPositionProperty()
            }
    );
    public final ObservableList<Alien> publicAliveAliens = FXCollections.unmodifiableObservableList(this.aliveAliens);
    private final ArrayList<Alien> deadAliens = new ArrayList<>();
    private final ObservableSet<Mushroom> mushrooms = FXCollections.observableSet();
    public final ObservableSet<Mushroom> publicMushrooms = FXCollections.unmodifiableObservableSet(this.mushrooms);
    private final Timeline simulationTimeline;
    private final ReadOnlyBooleanWrapper isEnd = new ReadOnlyBooleanWrapper(false);
    private int currentDay = 0;
    private final ReadOnlyBooleanWrapper isRunning = new ReadOnlyBooleanWrapper(true);
    private final ObservableList<SimulationStats> simulationStats = FXCollections.observableArrayList();
    public final ObservableList<SimulationStats> publicSimulationStats = FXCollections.unmodifiableObservableList(this.simulationStats);

    public SimulationEngine(Settings settings) {
        this.settings = settings;
        this.gameMap = new GameMap(
                this.settings.getMapWidth(),
                this.settings.getMapHeight(),
                this.settings.getJungleRatio()
        );

        // Wait for initialization of the map and then place initial aliens
        Timeline initializationDelay = new Timeline(new KeyFrame(Duration.seconds(2), event ->
                placeInitialAliens(this.settings.getInitialAliens())
        ));
        initializationDelay.play();

        // Create the simulation timeline and play it with delay
        this.simulationTimeline = new Timeline(new KeyFrame(Duration.millis(1500), event -> {
            this.dayOfSimulation();
        }));

        this.simulationTimeline.setCycleCount(Animation.INDEFINITE);
        this.simulationTimeline.playFrom(Duration.seconds(4));
    }

    // Secure getters for properties
    public ReadOnlyBooleanProperty getIsEndProperty() {
        return this.isEnd.getReadOnlyProperty();
    }

    // Play the simulation
    public void playSimulation() {
        if (!this.isEnd.getValue()) {
            this.simulationTimeline.play();
            this.isRunning.setValue(true);
        }
    }

    // Stop the simulation
    public void stopSimulation() {
        this.simulationTimeline.stop();
        this.isRunning.setValue(false);
    }

    // Secure getters for properties
    public boolean isRunning() {
        return this.isRunning.getValue();
    }

    public ReadOnlyBooleanProperty getIsRunningProperty() {
        return this.isRunning.getReadOnlyProperty();
    }

    public int getCurrentDay() {
        return this.currentDay;
    }

    // Change Day Duration value
    public void changeDayDuration(int msDuration, boolean autoPlay) {
        this.simulationTimeline.stop();

        this.simulationTimeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(msDuration), event -> {
            this.dayOfSimulation();
        }));

        if (autoPlay) this.simulationTimeline.play();
    }

    // Set of operations to do every day of the simulation
    private void dayOfSimulation() {
        try {
            if (this.aliveAliens.size() == 0 && this.currentDay != 0) {
                this.isEnd.setValue(true);
                this.stopSimulation();
            }

//            Mushroom mushroom = new Mushroom(new Vector2d(2, 2));
//            this.addMushroom(mushroom);
//            this.addMushroom(mushroom);

            removeDeadMoveAlive();
            eatMushrooms();
            propagateAliens();
            growMushrooms();

            SimulationStats stats = SimulationStats.createStatsOfDay(this.currentDay, this.aliveAliens, this.deadAliens, this.mushrooms);
            this.simulationStats.add(stats);

            this.currentDay += 1;
        } catch (Exception exception) {
            this.stopSimulation();

            AlertHelper alertHelper = new AlertHelper();
            alertHelper.showException(exception);
        }
    }

    // Add new alien to the simulation and place it on the map
    private void addAlien(Alien alien) {
        if (this.gameMap.placeAlien(alien)) {
            this.aliveAliens.add(alien);
        }
    }

    // Add new mushroom to the simulation and place it on the map
    private void addMushroom(Mushroom mushroom) {
        if (this.gameMap.placeMushroom(mushroom)) {
            this.mushrooms.add(mushroom);
        }
    }

    // Create initial aliens on random positions and place them on the map
    private void placeInitialAliens(int howMany) {
        for (int i = 0; i < howMany; i++) {
            Vector2d randomPosition;

            do {
                int posX = (int) (Math.random() * this.settings.getMapWidth());
                int posY = (int) (Math.random() * this.settings.getMapHeight());

                randomPosition = new Vector2d(posX, posY);
            } while (!this.gameMap.isFreeOfAliens(randomPosition));

            Alien newAlien = new Alien(
                    this.gameMap,
                    randomPosition,
                    WorldDirection.random(),
                    this.settings.getStartEnergy(),
                    new Genotype()
            );

            this.addAlien(newAlien);
        }
    }

    // Iterate through all aliens, remove dead and move alive ones
    private void removeDeadMoveAlive() {
        Iterator<Alien> iterator = this.aliveAliens.iterator();

        while (iterator.hasNext()) {
            Alien alien = iterator.next();

            if (alien.getEnergy() <= 0) {
                this.deadAliens.add(alien);
                this.gameMap.removeAlien(alien);
                iterator.remove();
            } else {
                alien.growOld();

                byte prefRotation = alien.getPrefRotation();

                for (byte i = 1; i <= prefRotation; i++) {
                    alien.move(MoveDirection.RIGHT);
                }

                alien.move(MoveDirection.FORWARD);
                alien.dropEnergy(this.settings.getMoveEnergy());
            }
        }
    }

    // Iterate through all mushrooms, find Aliens on these fields and eat
    private void eatMushrooms() {
        Iterator<Mushroom> iterator = this.mushrooms.iterator();

        while (iterator.hasNext()) {
            Mushroom mushroom = iterator.next();

            ArrayList<Alien> aliens = this.gameMap.getAliensFromField(mushroom.getPosition());

            if (aliens.size() != 0) {
                aliens.sort(Comparator.comparing(Alien::getEnergy).reversed());

                int strongestEnergy = aliens.get(0).getEnergy();
                aliens.removeIf(alien -> alien.getEnergy() != strongestEnergy);

                int energyBoost = this.settings.getPlantEnergy() / aliens.size();
                aliens.forEach(alien -> alien.boostEnergy(energyBoost));

                this.gameMap.removeMushroom(mushroom);
                iterator.remove();
            }
        }
    }

    // Iterate through all aliens and propagate if there is more than one on a field
    private void propagateAliens() {
        TreeSet<Vector2d> checkedFields = new TreeSet();

        ArrayList<ArrayList<Alien>> parentsToPropagate = new ArrayList<>();

        for (Alien alien : aliveAliens) {
            Vector2d alienPos = alien.getPosition();

            if (checkedFields.contains(alienPos)) continue;
            checkedFields.add(alienPos);

            ArrayList<Alien> fieldAliens = this.gameMap.getAliensFromField(alienPos);

            if (fieldAliens.size() >= 2) {
                fieldAliens.sort(Comparator.comparing(Alien::getEnergy).reversed());

                ArrayList<Alien> parents = new ArrayList<>();

                if (fieldAliens.get(0).getEnergy() == fieldAliens.get(1).getEnergy()) {
                    fieldAliens.removeIf(fieldAlien -> fieldAlien.getEnergy() != fieldAliens.get(0).getEnergy());
                    Collections.shuffle(fieldAliens);

                    if (fieldAliens.get(0).getEnergy() >= 0.5 * this.settings.getStartEnergy()) {
                        parents.add(fieldAliens.get(0));
                        parents.add(fieldAliens.get(1));
                    }
                } else {
                    Alien firstAlien = fieldAliens.get(0);

                    fieldAliens.removeIf(fieldAlien -> fieldAlien.getEnergy() != fieldAliens.get(1).getEnergy());
                    Collections.shuffle(fieldAliens);

                    if (fieldAliens.get(0).getEnergy() >= 0.5 * this.settings.getStartEnergy()) {
                        parents.add(firstAlien);
                        parents.add(fieldAliens.get(0));
                    }
                }

                if (parents.size() != 0) parentsToPropagate.add(parents);
            }
        }

        parentsToPropagate.forEach(parents -> this.createChildOf(parents.get(0), parents.get(1)));
    }

    // Create a children of two animals and place it around them
    private void createChildOf(Alien first, Alien second) {
        byte[] crossedGenotype = Genotype.crossGenotypes(first.getLongGenotype(), second.getLongGenotype());

        int firstFractOfEnergy = (int) Math.floor((float) first.getEnergy() / 4);
        first.dropEnergy(firstFractOfEnergy);

        int secondFractOfEnergy = (int) Math.floor((float) second.getEnergy() / 4);
        second.dropEnergy(secondFractOfEnergy);

        Vector2d[] possiblePositions = new Vector2d[] {
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.NORTH.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.NORTH_EAST.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.EAST.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.SOUTH_EAST.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.SOUTH.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.SOUTH_WEST.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.WEST.toUnitVector())),
                this.gameMap.correctPosition(first.getPosition().add(WorldDirection.NORTH_WEST.toUnitVector()))
        };

        short randomPositionIdx = (short) (Math.random() * 7);

        if (!this.gameMap.isFree(possiblePositions[randomPositionIdx])) {
            for (int i = 0; i <= 8; i++) {
                randomPositionIdx = (short) ((randomPositionIdx + 1) % 8);

                if (this.gameMap.isFree(possiblePositions[randomPositionIdx])) break;
            }
        }

        Alien child = new Alien(
                this.gameMap,
                possiblePositions[randomPositionIdx],
                WorldDirection.random(),
                firstFractOfEnergy + secondFractOfEnergy,
                new Genotype(crossedGenotype)
        );

        first.addChild(child);
        second.addChild(child);

        this.addAlien(child);
    }

    // If possible grow one mushroom outside and one inside the jungle
    private void growMushrooms() {
        Vector2d freeOutside = this.gameMap.getRandomFreeOutsideJungle();

        if (freeOutside != null) {
            Mushroom newOutside = new Mushroom(freeOutside);
            this.addMushroom(newOutside);
        }

        Vector2d freeInside = this.gameMap.getRandomFreeInsideJungle();

        if (freeInside != null) {
            Mushroom newInside = new Mushroom(freeInside);
            this.addMushroom(newInside);
        }
    }
}
