package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Generic AlertBox to display pop-up messages.
 */
public class InputBox {

	private static final double minWIDTH = 256;
	
	private static String input;

    /**
     * @param title Title of pop-up alert. 
     * @param message Message to display in alert box.
     */
    public static String display(String title, String message) {
    	input = "";
    	
        Stage window = new Stage();
        
        window.initStyle(StageStyle.TRANSPARENT);

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(minWIDTH);

        Label label = new Label();
        label.setText(message);
        
        TextField userInput = new TextField();
        
        Button confirmButton = new Button("Submit");
        confirmButton.setId("default-btn");
        confirmButton.setOnAction(e -> {
        	validateInput();
        	input = userInput.getText();
        	window.close();
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);   
        layout.getChildren().addAll(label, userInput, confirmButton);

        Scene scene = new Scene(layout);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("styles/main.css");
        
        layout.setId("popup");
        
        window.setScene(scene);
        window.showAndWait();
        return input;
    }
    
    private static void validateInput() {
    
    }

}