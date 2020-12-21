package agh.cs.oop.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import agh.cs.oop.gui.AlertHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimulationStats {
    private final int day;
    private final int aliensQuantity;
    private final int mushroomsQuantity;
    private final long dominantGenotype;
    private final HashMap<Long, Integer> aliensGenotypes;
    private final double avgAliveEnergy;
    private final double avgDeadLifetime;
    private final double avgAliveChildren;

    public SimulationStats(int day, int aliensQuantity, int mushroomsQuantity, long dominantGenotype,
                           HashMap<Long, Integer> aliensGenotypes, double avgAliveEnergy, double avgDeadLifetime,
                           double avgAliveChildren) {
        this.day = day;
        this.aliensQuantity = aliensQuantity;
        this.mushroomsQuantity = mushroomsQuantity;
        this.dominantGenotype = dominantGenotype;
        this.aliensGenotypes = aliensGenotypes;
        this.avgAliveEnergy = avgAliveEnergy;
        this.avgDeadLifetime = avgDeadLifetime;
        this.avgAliveChildren = avgAliveChildren;
    }

    // Getters for immutable primitive properties
    public int getDay() {
        return this.day;
    }

    public int getAliensQuantity() {
        return this.aliensQuantity;
    }

    public int getMushroomsQuantity() {
        return this.mushroomsQuantity;
    }

    public long getDominantGenotype() {
        return this.dominantGenotype;
    }

    public HashMap<Long, Integer> getAliensGenotypes() {
        return this.aliensGenotypes;
    }

    public double getAvgAliveEnergy() {
        return this.avgAliveEnergy;
    }

    public double getAvgDeadLifetime() {
        return this.avgDeadLifetime;
    }

    public double getAvgAliveChildren() {
        return this.avgAliveChildren;
    }

    // Helper for creating SimulationStats for current day
    public static SimulationStats createStatsOfDay(int currentDay, ObservableList<Alien> aliveAliens,
                                                   ArrayList<Alien> deadAliens, ObservableSet<Mushroom> mushrooms) {
        // Gather stats from alive aliens
        int aliveAliensCounter = 0;
        int sumOfAliveEnergy = 0;
        int sumOfAliveChildren = 0;
        HashMap<Long, Integer> aliensGenotypes = new HashMap<>();

        long dominantGenotype = 0;
        int dominantGenotypeQuantity = 0;

        for (Alien alien : aliveAliens) {
            aliveAliensCounter += 1;
            sumOfAliveEnergy += alien.getEnergy();
            sumOfAliveChildren += alien.getChildrenQuantity();

            long genotype = alien.getLongGenotype();

            int currentGenotypeQuantity = 1;
            if (aliensGenotypes.containsKey(genotype)) currentGenotypeQuantity += aliensGenotypes.get(genotype);

            aliensGenotypes.put(genotype, currentGenotypeQuantity);

            if (currentGenotypeQuantity > dominantGenotypeQuantity) {
                dominantGenotypeQuantity = currentGenotypeQuantity;
                dominantGenotype = genotype;
            }
        }

        double avgAliveEnergy = 0;
        if (aliveAliensCounter != 0) avgAliveEnergy = (double) sumOfAliveEnergy / aliveAliensCounter;

        double avgAliveChildren = 0;
        if (aliveAliensCounter != 0) avgAliveChildren = (double) sumOfAliveChildren / aliveAliensCounter;

        // Gather stats from dead aliens
        int deadAliensCounter = 0;
        int sumOfDeadLifetime = 0;

        for (Alien alien : deadAliens) {
            deadAliensCounter += 1;
            sumOfDeadLifetime += alien.getAge();
        }

        double avgDeadLifetime = 0;
        if (deadAliensCounter != 0) avgDeadLifetime = (double) sumOfDeadLifetime / deadAliensCounter;

        // Create stats
        return new SimulationStats(currentDay, aliveAliensCounter, mushrooms.size(), dominantGenotype, aliensGenotypes,
                avgAliveEnergy, avgDeadLifetime, avgAliveChildren);
    }

    public static void generateStats(ObservableList<SimulationStats> simulationStats, int currentDay) {
        VBox sceneContainer = new VBox();
        sceneContainer.setPadding(new Insets(10));
        sceneContainer.setSpacing(15);

        // Prepare newScene and newWindow
        Scene newScene = new Scene(sceneContainer, 600, 150);

        Stage newWindow = new Stage();
        newWindow.setTitle("Simulation stats generator");
        newWindow.setScene(newScene);

        // Prepare input for days of simulation
        HBox inputContainer = new HBox();
        inputContainer.setSpacing(10);
        sceneContainer.getChildren().add(inputContainer);

        Label daysLabel = new Label("How many days should be included in the stats: ");
        inputContainer.getChildren().add(daysLabel);

        TextField daysField = new TextField(String.valueOf(currentDay));
        inputContainer.getChildren().add(daysField);

        // Prepare getter for file's path
        HBox fileContainer = new HBox();
        fileContainer.setSpacing(10);
        sceneContainer.getChildren().add(fileContainer);

        Label fileLabel = new Label("Where should the stats be saved: ");
        fileContainer.getChildren().add(fileLabel);

        TextField fileField = new TextField();
        fileField.setEditable(false);
        fileContainer.getChildren().add(fileField);

        DirectoryChooser directoryChooser = new DirectoryChooser();

        Button fileButton = new Button("Select directory");
        fileContainer.getChildren().add(fileButton);

        fileButton.setOnAction(clickEvent -> {
            File selectedDirectory = directoryChooser.showDialog(newWindow);
            fileField.setText(selectedDirectory.getAbsolutePath());
        });

        // Prepare getter for file's name and generate button
        HBox generateContainer = new HBox();
        generateContainer.setSpacing(10);
        sceneContainer.getChildren().add(generateContainer);

        Label nameLabel = new Label("Name of the file (without extension): ");
        generateContainer.getChildren().add(nameLabel);

        TextField nameField = new TextField();
        generateContainer.getChildren().add(nameField);

        Button generateButton = new Button("Generate & Close");
        generateContainer.getChildren().add(generateButton);

        // Set the action by clicking generate button
        generateButton.setOnAction(clickEvent -> {
            try {
                int howManyDays = checkAndParseInt(daysField.getText(), "quantity of days included in the stats");

                if (howManyDays <= 0) {
                    throw new IllegalArgumentException("Quantity of days included in the stats can not be lesser than 1");
                } else if (howManyDays > currentDay) {
                    throw new IllegalArgumentException("Quantity of days included in the stats can not be greater than current day");
                }

                if (fileField.getText().isEmpty()) throw new IllegalArgumentException("Path of the directory can not be empty");
                if (nameField.getText().isEmpty()) throw new IllegalArgumentException("Name of the file can not be empty");
                if (nameField.getText().contains(".")) throw new IllegalArgumentException("Name of the file can not contain extension");

                // Sum every property of stats
                int simulationsCounter = 0;
                long sumOfAliens = 0;
                long sumOfMushrooms = 0;
                long sumOfAvgAliveEnergy = 0;
                long sumOfAvgDeadLifetime = 0;
                long sumOfAvgAliveChildren = 0;
                HashMap<Long, Integer> aliensGenotypes = new HashMap<>();

                for (SimulationStats eachSimulationStats : simulationStats) {
                    if (eachSimulationStats.getDay() == 0) continue;
                    if (eachSimulationStats.getDay() > howManyDays) break;

                    simulationsCounter += 1;
                    sumOfAliens += eachSimulationStats.aliensQuantity;
                    sumOfMushrooms += eachSimulationStats.mushroomsQuantity;
                    sumOfAvgAliveEnergy += eachSimulationStats.getAvgAliveEnergy();
                    sumOfAvgDeadLifetime += eachSimulationStats.getAvgDeadLifetime();
                    sumOfAvgAliveChildren += eachSimulationStats.getAvgAliveChildren();

                    for (Map.Entry<Long, Integer> pair : eachSimulationStats.getAliensGenotypes().entrySet()) {
                        int currentGenotypeQuantity = pair.getValue();
                        if (aliensGenotypes.containsKey(pair.getKey())) currentGenotypeQuantity += aliensGenotypes.get(pair.getKey());

                        aliensGenotypes.put(pair.getKey(), currentGenotypeQuantity);
                    }
                }

                // Get dominang genotype
                long dominantGenotype = 0;
                long dominantGenotypeQuantity = 0;

                for (Map.Entry<Long, Integer> pair : aliensGenotypes.entrySet()) {
                    if (dominantGenotype == 0 || pair.getValue() > dominantGenotypeQuantity) {
                        dominantGenotype = pair.getKey();
                        dominantGenotypeQuantity = pair.getValue();
                    }
                }

                // Calculate average values of properties for each day
                double avgSumOfAliens = (double) sumOfAliens / simulationsCounter;
                double avgSumOfMushrooms = (double) sumOfMushrooms / simulationsCounter;
                double avgSumOfAvgAliveEnergy = (double) sumOfAvgAliveEnergy / simulationsCounter;
                double avgSumOfAvgDeadLifetime = (double) sumOfAvgDeadLifetime / simulationsCounter;
                double avgSumOfAvgAliveChildren = (double) sumOfAvgAliveChildren / simulationsCounter;

                // Save the file and close window
                File generatedFile = new File(fileField.getText() + "/" + nameField.getText() + ".json");

                Stats generatedStats = new Stats(howManyDays, avgSumOfAliens, avgSumOfMushrooms,
                        avgSumOfAvgAliveEnergy, avgSumOfAvgDeadLifetime, avgSumOfAvgAliveChildren,
                        dominantGenotype, Genotype.prettyPrintOf(dominantGenotype));

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(generatedFile, generatedStats);

                newWindow.close();
            } catch (Exception exception) {
                AlertHelper alertHelper = new AlertHelper();
                alertHelper.showException(exception);
            }
        });

        // Show the window
        newWindow.show();
    }

    // Check if given String is integer and if yes parse it
    private static int checkAndParseInt(String value, String property) {
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Provided " + property + " is not an integer");
        }

        return Integer.parseInt(value);
    }
}
