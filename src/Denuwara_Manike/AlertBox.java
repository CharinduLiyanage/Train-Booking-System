package Denuwara_Manike;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is the creation of AlertBox for the GUI.
 */
public class AlertBox {

    /**
     * This Method displays the AlertBox.
     * @param title A String containing the title for the Alert Box.
     * @param message A String containing the message for the Alert Box
     * @param buttonText A String containing the text for the button of the alert Box.
     */
    protected static void display(String title, String message, String buttonText) {

        // Creating the window.
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);

        // Creating the label to display the message.
        Label label = new Label();
        label.setText(message);
        label.setPadding(new Insets(15));
        label.setAlignment(Pos.TOP_CENTER);

        // Creating the Button of the Alert Box.
        Button closeButton = new Button(buttonText);
        closeButton.setPadding(new Insets(5));
        closeButton.setPrefWidth(70);
        closeButton.setOnAction(e -> window.close());

        // Creating and setting the layout of the Alert Box.
        VBox layout = new VBox();
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        // Setting the window.
        Scene scene = new Scene(layout,300,125);
        window.setScene(scene);
        window.showAndWait();

    }
}
