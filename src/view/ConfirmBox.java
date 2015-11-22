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
public class ConfirmBox {
	
	private static final double minWIDTH = 256;
	private static boolean answer;

    /**
     * @param title Title of pop-up alert. 
     * @param message Message to display in alert box.
     */
    public static boolean display(String title, String message) {
        Stage window = new Stage();
        
        window.initStyle(StageStyle.TRANSPARENT);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(minWIDTH);

        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Yes");
        yesButton.setId("default-btn");
        yesButton.setOnAction(e -> {
        	answer = true;
        	window.close();
        });
        
        Button noButton = new Button("No");
        noButton.setId("default-btn");
        noButton.setOnAction(e -> {
        	answer = false;
        	window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(false);        

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("stylesheets/idiotMain.css");
        
        layout.setId("popup");
        
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }

}