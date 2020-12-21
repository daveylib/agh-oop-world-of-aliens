package agh.cs.oop.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class AlertHelper {
    public void showException(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        // Prepare content of the alert
        VBox DialogContainer = new VBox();
        DialogContainer.setSpacing(10);

        // Add main cause of the exception
        Label mainText = new Label(exception.getMessage());
        DialogContainer.getChildren().add(mainText);

        // Add stack trace to cause of the exception in ScrollPane
        ScrollPane scrollContainer = new ScrollPane();
        DialogContainer.getChildren().add(scrollContainer);

        TextArea alertText = new TextArea();
        scrollContainer.setContent(alertText);

        alertText.appendText(exception.toString() + "\n");

        for (StackTraceElement lineOfText : exception.getStackTrace()) {
            alertText.appendText(lineOfText.toString() + "\n");
        }

        // Show the alert
        alert.getDialogPane().setContent(DialogContainer);
        alert.show();
    }
}
