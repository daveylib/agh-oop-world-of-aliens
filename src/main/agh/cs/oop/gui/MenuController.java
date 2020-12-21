package agh.cs.oop.gui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import javafx.event.ActionEvent;

public class MenuController extends SceneChanger {
    @FXML
    private CheckBox showTwoMaps;

    @FXML
    private void runSimulation(ActionEvent actionEvent) throws Exception {
        boolean shouldShowTwoMaps = false;
        if (showTwoMaps.isSelected()) shouldShowTwoMaps = true;

        sceneChangeToSimulation(actionEvent, shouldShowTwoMaps);
    }

    @FXML
    private void showSettings(ActionEvent actionEvent) throws Exception {
        sceneChangeToSettings(actionEvent);
    }
}
