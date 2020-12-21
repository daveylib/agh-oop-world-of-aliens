package agh.cs.oop.gui;

import agh.cs.oop.engine.Alien;
import agh.cs.oop.engine.Genotype;
import agh.cs.oop.engine.SimulationEngine;
import agh.cs.oop.engine.SimulationStats;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Separator;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationPresenter {
    private final SettingsHandler settingsHandler = new SettingsHandler();
    private final SimulationEngine simulationEngine;
    private MapVisualizer mapVisualizer;
    public final HBox simulationBox = new HBox();
    private final VBox simulationBoxControls = new VBox();

    public SimulationPresenter() throws IOException {
        this.simulationEngine = new SimulationEngine(this.settingsHandler.settings);

        this.simulationBox.setSpacing(5);

        presentMap();

        this.simulationBoxControls.getStyleClass().add("gameBoxControls");
        this.simulationBox.getChildren().add(this.simulationBoxControls);

        showControls();
        showStats();
        listAnimals();
    }

    // Present map with aliens and mushrooms on fields
    private void presentMap() {
        VBox VBoxContainer = new VBox();
        VBoxContainer.setSpacing(5);

        // Create a scrollable and pannable container for the map
        ScrollPane simulationMap = new ScrollPane();
        simulationMap.getStyleClass().add("gameScrollPane");
        simulationMap.setPannable(true);

        // Initialize visualizer of the map
        this.mapVisualizer = new MapVisualizer(
                this.settingsHandler.settings,
                this.simulationEngine.gameMap.getJungleUpperLeft(),
                this.simulationEngine.gameMap.getJungleLowerRight(),
                this.simulationEngine.publicAliveAliens,
                this.simulationEngine.publicMushrooms
        );

        // Add the map to the box
        simulationMap.setContent(mapVisualizer.gridMap);
        VBoxContainer.getChildren().add(simulationMap);

        // Add info about listing aliens from field on the map
        VBox additionalMapInfo = new VBox();
        additionalMapInfo.setSpacing(10);
        VBoxContainer.getChildren().add(additionalMapInfo);

        Label aliensInfo = new Label("Click twice on the Alien to list and follow everyone on that field");
        aliensInfo.getStyleClass().add("gameMapInfo");
        additionalMapInfo.getChildren().add(aliensInfo);

        // Add button to generate stats and show all aliens with dominant genotype
        HBox additionalButtons = new HBox();
        additionalButtons.setSpacing(10);
        additionalMapInfo.getChildren().add(additionalButtons);

        Button generateStatsButton = new Button("Generate stats from the simulation");
        generateStatsButton.getStyleClass().add("gameButton");
        additionalButtons.getChildren().add(generateStatsButton);

        generateStatsButton.setOnAction(clickEvent -> {
            this.simulationEngine.stopSimulation();
            SimulationStats.generateStats(this.simulationEngine.publicSimulationStats, this.simulationEngine.getCurrentDay() - 1);
        });

        Button showDominantAliens = new Button("Show Aliens with dominant genotype");
        showDominantAliens.getStyleClass().add("gameButton");
        additionalButtons.getChildren().add(showDominantAliens);

        showDominantAliens.setOnAction(clickEvent -> {
            this.simulationEngine.stopSimulation();

            int currentSimulationStatsIdx = this.simulationEngine.publicSimulationStats.size() - 1;
            SimulationStats currentSimulationStats = this.simulationEngine.publicSimulationStats.get(currentSimulationStatsIdx);
            this.showDominantAliens(currentSimulationStats, this.simulationEngine.publicAliveAliens);
        });

        this.simulationBox.getChildren().add(VBoxContainer);
    }

    // Show new windows with list of all aliens that have dominant genotype
    private void showDominantAliens(SimulationStats currentSimulationStats, ObservableList<Alien> aliveAliens) {
        VBox sceneContainer = new VBox();
        sceneContainer.setPadding(new Insets(10));
        sceneContainer.setSpacing(15);

        // Prepare newScene and newWindow
        Scene newScene = new Scene(sceneContainer, 600, 400);

        Stage newWindow = new Stage();
        newWindow.setTitle("Aliens with dominant genotype");
        newWindow.setScene(newScene);

        // Show the dominant genotype
        String dominantGenotype = Genotype.prettyPrintOf(currentSimulationStats.getDominantGenotype());

        Label genotypeLabel = new Label("Dominant genotype: " + dominantGenotype);
        sceneContainer.getChildren().add(genotypeLabel);

        // Show the list with all aliens
        VBox listOfAliens = new VBox();

        ScrollPane listOfAliensScroll = new ScrollPane();
        listOfAliensScroll.setContent(listOfAliens);
        sceneContainer.getChildren().add(listOfAliensScroll);

        for (Alien alien : aliveAliens) {
            if (alien.getLongGenotype() == currentSimulationStats.getDominantGenotype()) {
                VBox alienContainer = new VBox();
                alienContainer.setSpacing(5);
                listOfAliens.getChildren().add(alienContainer);

                Separator separator = new Separator();
                alienContainer.getChildren().add(separator);

                HBox alienDetails = new HBox();
                alienDetails.setAlignment(Pos.CENTER);
                alienDetails.setSpacing(15);
                alienContainer.getChildren().add(alienDetails);

                AlienVisualizer alienVisualizer = new AlienVisualizer(alien, 25,
                        this.settingsHandler.settings.getStartEnergy());
                alienDetails.getChildren().add(alienVisualizer);

                VBox alienProperties = new VBox();
                alienDetails.getChildren().add(alienProperties);

                Label alienAge = new Label("Age: " + alien.getAge() + " days");
                alienProperties.getChildren().add(alienAge);

                int fractOfStartEnergy = (int) ((double) alien.getEnergy() / this.settingsHandler.settings.getStartEnergy() * 100);
                Label alienEnergy = new Label("Energy: " + alien.getEnergy() + " (" + fractOfStartEnergy +
                        "% of initial energy)");
                alienProperties.getChildren().add(alienEnergy);

                Label alienPosition = new Label("Position: " + alien.getPosition());
                alienProperties.getChildren().add(alienPosition);

                Label alienChildrens = new Label("Quantity of children: " + alien.getChildrenQuantity());
                alienProperties.getChildren().add(alienChildrens);

                Label alienDescendants = new Label("Quantity of all descendants: " + alien.getDescendantQuantity());
                alienProperties.getChildren().add(alienDescendants);
            }
        }

        // Show the window
        newWindow.show();
    }

    // Show such controls as Play/Stop button and Day Duration slider
    private void showControls() {
        HBox HBoxContainer = new HBox();
        HBoxContainer.setAlignment(Pos.CENTER);
        HBoxContainer.setId("gameControls");

        // Add Play/Stop button
        Button playStopButton = new Button("Stop");
        playStopButton.setMinWidth(Region.USE_PREF_SIZE);
        playStopButton.setMinHeight(Region.USE_PREF_SIZE);
        playStopButton.getStyleClass().add("gameButton");

        playStopButton.setOnAction(event -> {
            if (this.simulationEngine.isRunning()) {
                this.simulationEngine.stopSimulation();
                playStopButton.setText("Play");
            } else {
                this.simulationEngine.playSimulation();
                playStopButton.setText("Stop");
            }
        });

        HBoxContainer.getChildren().add(playStopButton);

        // Add listener to isEnd SimpleBooleanProperty to handle end of the simulation
        this.simulationEngine.getIsEndProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                playStopButton.setText("Play");
                playStopButton.setDisable(true);
            }
        });

        // Add listener to isRunning SimpleBooleanProperty to handle unexpected stop of the simulation
        this.simulationEngine.getIsRunningProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                playStopButton.setText("Play");
            }
        });

        // Add Day Duration slider
        Slider dayDurationSlider = new Slider(50, 1500, 1000);
        dayDurationSlider.setShowTickLabels(true);
        dayDurationSlider.setShowTickMarks(true);
        dayDurationSlider.setMajorTickUnit(500);
        dayDurationSlider.setBlockIncrement(10);
        dayDurationSlider.setId("dayDurationSlider");

        HBoxContainer.getChildren().add(dayDurationSlider);
        HBoxContainer.setHgrow(dayDurationSlider, Priority.ALWAYS);

        // Add label with current Day Duration slider value
        Label dayDurationLabel = new Label("1000 ms");
        dayDurationLabel.setMinWidth(Region.USE_PREF_SIZE);
        dayDurationLabel.setMinHeight(Region.USE_PREF_SIZE);
        dayDurationLabel.setId("dayDurationLabel");

        HBoxContainer.getChildren().add(dayDurationLabel);

        // Add listener to the Day Duration slider
        dayDurationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            dayDurationLabel.setText(newValue.intValue() + " ms");
            this.simulationEngine.changeDayDuration(newValue.intValue(), this.simulationEngine.isRunning());
        });

        this.simulationBoxControls.getChildren().add(HBoxContainer);
    }

    // Show stats for simulation and handle updates
    private void showStats() {
        VBox VBoxContainer = new VBox();
        VBoxContainer.setId("gameStats");

        Label currentDayLabel = new Label("Day of simulation: none");
        currentDayLabel.getStyleClass().add("gameStatsLabel");
        VBoxContainer.getChildren().add(currentDayLabel);

        Label aliensQuantityLabel = new Label("Quantity of alive Aliens: none");
        aliensQuantityLabel.getStyleClass().add("gameStatsLabel");
        VBoxContainer.getChildren().add(aliensQuantityLabel);

        Label mushroomsQuantityLabel = new Label("Quantity of mushrooms: none");
        mushroomsQuantityLabel.getStyleClass().add("gameStatsLabel");
        VBoxContainer.getChildren().add(mushroomsQuantityLabel);

        Label dominantGenotypeLabel = new Label("Dominant genotype: none");
        dominantGenotypeLabel.getStyleClass().add("gameStatsLabel");
        dominantGenotypeLabel.setWrapText(true);
        VBoxContainer.getChildren().add(dominantGenotypeLabel);

        Label avgAliveEnergyLabel = new Label("Average energy of alive Aliens: none");
        avgAliveEnergyLabel.getStyleClass().add("gameStatsLabel");
        VBoxContainer.getChildren().add(avgAliveEnergyLabel);

        Label avgDeadLifetimeLabel = new Label("Average lifetime of dead Aliens: none");
        avgDeadLifetimeLabel.getStyleClass().add("gameStatsLabel");
        VBoxContainer.getChildren().add(avgDeadLifetimeLabel);

        Label avgAliveChildrenLabel = new Label("Average children quantity of alive Aliens: none");
        avgAliveChildrenLabel.getStyleClass().add("gameStatsLabel");
        VBoxContainer.getChildren().add(avgAliveChildrenLabel);

        // Add listener to the ObservableList of SimulationStats in SimulationEngine
        this.simulationEngine.publicSimulationStats.addListener((ListChangeListener<SimulationStats>) listChange -> {
            while (listChange.next()) {
                if (listChange.wasAdded()) {
                    for (SimulationStats simulationStats : listChange.getAddedSubList()) {
                        currentDayLabel.setText("Day of simulation: " + simulationStats.getDay());
                        aliensQuantityLabel.setText("Quantity of alive Aliens: " + simulationStats.getAliensQuantity());
                        mushroomsQuantityLabel.setText("Quantity of mushrooms: " + simulationStats.getMushroomsQuantity());

                        if (simulationStats.getDominantGenotype() == 0) {
                            dominantGenotypeLabel.setText("Dominant genotype: none");
                        } else {
                            dominantGenotypeLabel.setText("Dominant genotype: " + Genotype.prettyPrintOf(simulationStats.getDominantGenotype()));
                        }

                        String avgAliveEnergy = String.format("%.2f", simulationStats.getAvgAliveEnergy());
                        avgAliveEnergyLabel.setText("Average energy of alive Aliens: " + avgAliveEnergy);

                        String avgDeadLifetime = String.format("%.2f", simulationStats.getAvgDeadLifetime());
                        avgDeadLifetimeLabel.setText("Average lifetime of dead Aliens: " + avgDeadLifetime);

                        String avgAliveChildren = String.format("%.2f", simulationStats.getAvgAliveChildren());
                        avgAliveChildrenLabel.setText("Average children quantity of alive Aliens: " + avgAliveChildren);
                    }
                }
            }
        });

        this.simulationBoxControls.getChildren().add(VBoxContainer);
    }

    // Add listener to all fields on the map and list animals after click
    private void listAnimals() {
        ScrollPane scrollList = new ScrollPane();
        scrollList.getStyleClass().add("gameAliensList");
        this.simulationBoxControls.getChildren().add(scrollList);

        VBox VBoxContainer = new VBox();
        scrollList.setContent(VBoxContainer);
        VBoxContainer.setVisible(false);
        VBoxContainer.getStyleClass().add("gameAliensListContainer");

        Label titleList = new Label("You're following these Aliens:");
        titleList.getStyleClass().add("gameAliensListLabel");
        VBoxContainer.getChildren().add(titleList);

        this.mapVisualizer.gridMap.getChildren().forEach(gridCell -> gridCell.setOnMouseClicked(event -> {
            if (event.getClickCount() != 2) return;

            StackPane mapField = (StackPane) gridCell;

            boolean isFirst = true;

            for (Node mapFieldNode : mapField.getChildren()) {
                if (mapFieldNode instanceof AlienVisualizer) {
                    AlienVisualizer alienVisualizer = (AlienVisualizer) mapFieldNode;

                    if (isFirst) {
                        VBoxContainer.setVisible(true);
                        isFirst = false;
                    }

                    // Prepare container for alien
                    VBox alienContainer = new VBox();
                    alienContainer.getStyleClass().add("gameAliensListEach");

                    // Add separator to each alien
                    Separator separator = new Separator();
                    alienContainer.getChildren().add(separator);

                    // Prepare container with all information about alien
                    HBox alienInList = new HBox();
                    alienInList.setSpacing(10);
                    alienInList.setAlignment(Pos.CENTER);
                    alienContainer.getChildren().add(alienInList);

                    // Add X button to stop following and icon of alien
                    VBox alienControls = new VBox();
                    alienControls.setSpacing(10);
                    alienControls.setAlignment(Pos.CENTER);
                    alienInList.getChildren().add(alienControls);

                    Button stopFollowing = new Button("X");
                    stopFollowing.getStyleClass().add("gameAliensListStopFollowing");
                    alienControls.getChildren().add(stopFollowing);

                    stopFollowing.setOnAction(clickEvent -> VBoxContainer.getChildren().remove(alienContainer));

                    alienControls.getChildren().add(new AlienVisualizer(alienVisualizer.getAlien(), 25,
                            this.settingsHandler.settings.getStartEnergy()));

                    // Add details of alien
                    VBox alienDetails = new VBox();
                    alienInList.getChildren().add(alienDetails);

                    // Shows it's age
                    Label alienAgeLabel = new Label("Age: " + alienVisualizer.getAlien().getAge() + " days");
                    alienAgeLabel.getStyleClass().add("gameAliensListLabel");
                    alienDetails.getChildren().add(alienAgeLabel);

                    alienVisualizer.getAlien().getAgeProperty().addListener((observable, oldValue, newValue) ->
                            alienAgeLabel.setText("Age: " + newValue + " days")
                    );

                    // Show it's energy
                    int alienEnergy = alienVisualizer.getAlien().getEnergy();
                    int fractOfStart = (int) (((double) alienEnergy / this.settingsHandler.settings.getStartEnergy()) * 100);
                    Label alienEnergyLabel = new Label("Energy: " + alienEnergy + " (" + fractOfStart + "% of initial energy)");
                    alienEnergyLabel.getStyleClass().add("gameAliensListLabel");
                    alienDetails.getChildren().add(alienEnergyLabel);

                    alienVisualizer.getAlien().getEnergyProperty().addListener((observable, oldValue, newValue) -> {
                        int newFract = (int) (newValue.doubleValue() / this.settingsHandler.settings.getStartEnergy() * 100);
                        alienEnergyLabel.setText("Energy: " + newValue + " (" + newFract + "% of initial energy)");
                    });

                    // Show it's position
                    Label alienPositionLabel = new Label("Position: " + alienVisualizer.getAlien().getPosition());
                    alienPositionLabel.getStyleClass().add("gameAliensListLabel");
                    alienDetails.getChildren().add(alienPositionLabel);

                    alienVisualizer.getAlien().getPositionProperty().addListener((observable, oldValue, newValue) ->
                            alienPositionLabel.setText("Position: " + newValue)
                    );

                    // Show it's genotype
                    Label alienGenotypeLabel = new Label("Genotype: " + alienVisualizer.getAlien().getPrettyGenotype());
                    alienGenotypeLabel.getStyleClass().add("gameAliensListLabel");
                    alienDetails.getChildren().add(alienGenotypeLabel);

                    // Shows it's quantity of children
                    Label alienChildrenLabel = new Label("Quantity of children: " + alienVisualizer.getAlien().getChildrenQuantity());
                    alienChildrenLabel.getStyleClass().add("gameAliensListLabel");
                    alienDetails.getChildren().add(alienChildrenLabel);

                    alienVisualizer.getAlien().getChildrenQuantityProperty().addListener((observable, oldValue, newValue) ->
                            alienChildrenLabel.setText("Quantity of children: " + newValue)
                    );

                    // Show it's quantity of all descendants
                    Label alienDescendantLabel = new Label("Quantity of all descendants: " + alienVisualizer.getAlien().getDescendantQuantity());
                    alienDescendantLabel.getStyleClass().add("gameAliensListLabel");
                    alienDetails.getChildren().add(alienDescendantLabel);

                    alienVisualizer.getAlien().getDescendantQuantityProperty().addListener((observable, oldValue, newValue) ->
                            alienDescendantLabel.setText("Quantity of all descendants: " + newValue)
                    );

                    // Add alien container to the main box
                    VBoxContainer.getChildren().add(alienContainer);
                }
            }
        }));
    }
}
