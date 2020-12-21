package agh.cs.oop.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SettingsController extends SceneChanger {
    private final SettingsHandler settingsHandler = new SettingsHandler();
    private final AlertHelper alertHelper = new AlertHelper();

    @FXML
    private TextField mapWidth;

    @FXML
    private TextField mapHeight;

    @FXML
    private TextField initialAliens;

    @FXML
    private TextField startEnergy;

    @FXML
    private TextField moveEnergy;

    @FXML
    private TextField plantEnergy;

    @FXML
    private Slider jungleRatio;

    @FXML
    private Label jungleRatioValue;

    @FXML
    private TextField gridSize;

    @FXML
    public void initialize() {
        // Get values from settings and set them in TextFields
        this.mapWidth.setText(String.valueOf(this.settingsHandler.settings.getMapWidth()));
        this.mapHeight.setText(String.valueOf(this.settingsHandler.settings.getMapHeight()));

        this.initialAliens.setText(String.valueOf(this.settingsHandler.settings.getInitialAliens()));

        this.startEnergy.setText(String.valueOf(this.settingsHandler.settings.getStartEnergy()));
        this.moveEnergy.setText(String.valueOf(this.settingsHandler.settings.getMoveEnergy()));
        this.plantEnergy.setText(String.valueOf(this.settingsHandler.settings.getPlantEnergy()));

        this.jungleRatio.adjustValue(this.settingsHandler.settings.getJungleRatio());
        this.jungleRatioValue.setText(String.valueOf(this.settingsHandler.settings.getJungleRatio()));

        this.gridSize.setText(String.valueOf(this.settingsHandler.settings.getGridSize()));

        // Add listener to the slider of the jungleRatio
        this.jungleRatio.valueProperty().addListener((observable, oldValue, newValue) ->
                jungleRatioValue.setText(String.format("%.2f", newValue))
        );
    }

    public SettingsController() throws IOException {}

    @FXML
    protected void exitWithoutSave(ActionEvent actionEvent) throws Exception {
        sceneChangeToMenu(actionEvent);
    }

    @FXML
    protected void exitWithSave(ActionEvent actionEvent) {
        try {
            // Get values from TextFields and change settings
            int mapWidth = this.checkAndParseInt(this.mapWidth.getText(), "width of the map");
            int mapHeight = this.checkAndParseInt(this.mapHeight.getText(), "height of the map");
            this.settingsHandler.settings.setMapSize(mapWidth, mapHeight);

            int initialAliens = this.checkAndParseInt(this.initialAliens.getText(), "quantity of initial Aliens");
            this.settingsHandler.settings.setInitialAliens(initialAliens);

            int startEnergy = this.checkAndParseInt(this.startEnergy.getText(), "initial energy of Aliens");
            int moveEnergy = this.checkAndParseInt(this.moveEnergy.getText(), "energy drop while moving everyday");
            int plantEnergy = this.checkAndParseInt(this.plantEnergy.getText(), "energy boost after eating mushrooms");
            this.settingsHandler.settings.setEnergyValues(startEnergy, moveEnergy, plantEnergy);

            double jungleRatio = this.checkAndParseDouble(this.jungleRatioValue.getText(), "ratio of the jungle to the map");
            this.settingsHandler.settings.setJungleRatio(jungleRatio);

            int gridSize = this.checkAndParseInt(this.gridSize.getText(), "size of the grid on the map");
            this.settingsHandler.settings.setGridSize(gridSize);

            // Save settings to the file
            this.settingsHandler.saveSettings();

            sceneChangeToMenu(actionEvent);
        } catch (Exception exception) {
            this.alertHelper.showException(exception);
        }
    }

    // Check if given String is integer and if yes parse it
    private int checkAndParseInt(String value, String property) {
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Provided " + property + " is not an integer");
        }

        return Integer.parseInt(value);
    }

    // Check if given String is double and if yes parse it
    private double checkAndParseDouble(String value, String property) {
        if (!value.matches("[0-9]*\\.?[0-9]+")) {
            throw new IllegalArgumentException("Provided " + property + " is not a double");
        }

        return Double.parseDouble(value);
    }
}
