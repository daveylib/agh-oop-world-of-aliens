package agh.cs.oop.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneChanger {
    private static final String CSSResourcePath = "resources/layout/stylesheet.css";

    private void sceneChanger(ActionEvent actionEvent, String FXMLResourcePath) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLResourcePath));
        Parent root = fxmlLoader.load();

        this.prepareNewScene(actionEvent, root);
    }

    private void prepareNewScene(ActionEvent actionEvent, Parent root) {
        Scene primaryScene = ((Node) actionEvent.getSource()).getScene();
        Stage primaryStage = (Stage) primaryScene.getWindow();

        Bounds localRootBounds = primaryScene.getRoot().getBoundsInLocal();
        Point2D localRootTopLeft = new Point2D(localRootBounds.getMinX(), localRootBounds.getMinY());
        Point2D screenRootTopLeft = primaryScene.getRoot().localToScreen(localRootTopLeft);

        double windowTopFrameHeight = screenRootTopLeft.getY() - primaryStage.getY();

        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight() - windowTopFrameHeight);
        newScene.getStylesheets().add(getClass().getResource(CSSResourcePath).toExternalForm());

        primaryStage.setScene(newScene);
    }

    // Show Simulation
    protected void sceneChangeToSimulation(ActionEvent actionEvent, boolean setTwoMaps) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("resources/layout/simulation.fxml"));
        Parent root = fxmlLoader.load();

        SimulationController simulationController = fxmlLoader.getController();
        simulationController.shouldShowTwoMaps(setTwoMaps);

        this.prepareNewScene(actionEvent, root);
    }

    // Show Settings
    protected void sceneChangeToSettings(ActionEvent actionEvent) throws Exception {
        sceneChanger(actionEvent, "resources/layout/settings.fxml");
    }

    // Show Menu
    protected void sceneChangeToMenu(ActionEvent actionEvent) throws Exception {
        sceneChanger(actionEvent, "resources/layout/menu.fxml");
    }
}
