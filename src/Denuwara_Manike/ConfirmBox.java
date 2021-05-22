package Denuwara_Manike;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class creates a Confirm box for the GUI.
 */
public class ConfirmBox {

    static String answer;

    /**
     * This Method Display the Confirm Box.
     * @param title A String containing the title of the Confirm Box.
     * @param message A String Containing the message of the Confirm Box.
     * @param button1Text A String Containing the text of the left Button.
     * @param button2Text A String containing the text of the right Button.
     * @return This returns a String containing the Button text if a button is clicked or "Cancel" otherwise.
     */
    protected static String display(String title, String message, String button1Text, String button2Text) {

        // Initializing the default outcome.
        answer = "Cancel";

        // Window of the Confirm Box.
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setResizable(false);

        // Creating the Label Containing the message.
        Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);

        // Create two buttons
        Button button1 = new Button(button1Text);
        button1.setPrefWidth(70);
        Button button2 = new Button(button2Text);
        button2.setPrefWidth(70);

        // Creating layout of the Confirm Box.
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(button1, button2);
        hBox.setAlignment(Pos.CENTER);

        // Button Click Event.
        button1.setOnAction(e -> {
            answer = button1Text;
            window.close();
        });
        button2.setOnAction(e -> {
            answer = button2Text;
            window.close();
        });

        // Setting the layout.
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, hBox);
        layout.setAlignment(Pos.CENTER);

        // Setting the window.
        Scene scene = new Scene(layout, 250, 100);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
