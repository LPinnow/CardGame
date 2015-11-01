package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Generic AlertBox to display pop-up messages.
 */
public class AlertBox {
	
	private static final double minWIDTH = 512;

    /**
     * @param title Title of pop-up alert. 
     * @param message Message to display in alert box.
     */
    public static void display(String title, String message) {
        Stage window = new Stage();
        
        window.initStyle(StageStyle.TRANSPARENT);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(minWIDTH);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Okay");
        closeButton.setId("default-btn");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER); 

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("styles/main.css");
        
        layout.setId("popup");
        
        window.setScene(scene);
        window.showAndWait();
    }

}