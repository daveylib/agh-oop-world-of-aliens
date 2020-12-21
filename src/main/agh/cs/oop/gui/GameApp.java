package agh.cs.oop.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("resources/layout/menu.fxml"));

            primaryStage.setTitle("The Amazing World of Aliens");
            primaryStage.setResizable(true);

            Scene menuScene = new Scene(root, 1400, 650);
            menuScene.getStylesheets().add(getClass().getResource("resources/layout/stylesheet.css").toExternalForm());

            primaryStage.setScene(menuScene);
            primaryStage.show();
        } catch (Exception exception) {
            AlertHelper alertHelper = new AlertHelper();
            alertHelper.showException(exception);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
