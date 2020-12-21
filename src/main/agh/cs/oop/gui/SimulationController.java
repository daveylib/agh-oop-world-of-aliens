package agh.cs.oop.gui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;

public class SimulationController extends SceneChanger {
    @FXML
    private HBox simulationsContainer;

    private final SimpleBooleanProperty showTwoMaps = new SimpleBooleanProperty();

    public void shouldShowTwoMaps(boolean status) {
        this.showTwoMaps.setValue(status);
    }

    @FXML
    private void initialize() {
        this.showSimulation();
        this.showTwoMaps.addListener((observable, oldValue, newValue) -> this.showSimulation());
    }

    private void showSimulation() {
        try {
            SimulationPresenter mainSimulation = new SimulationPresenter();
            this.simulationsContainer.getChildren().add(mainSimulation.simulationBox);
        } catch (Exception exception) {
            AlertHelper alertHelper = new AlertHelper();
            alertHelper.showException(exception);
        }
    }

    @FXML
    private void backToMenu(ActionEvent actionEvent) throws Exception {
        sceneChangeToMenu(actionEvent);
    }
}
