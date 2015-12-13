package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * This class represents the status bar for the application.
 */
public class StatusBar extends HBox {

  /**
   * Label which displays elapsed time.
   */
  private Label elapsedTimeText;

  /**
   * Helper variable for time.
   */
  private int seconds = 0;

  /**
   * Same.
   */
  private int minutes = 0;
  
  private Label activePlayer;
  

  /**
   * Constructs a new {@link StatusBar} object.
 * @param cardGameApp 
   */
  public StatusBar() {
    this.elapsedTimeText = new Label();
    this.activePlayer = new Label("Waiting on Player 1...");
    setPadding(new Insets(2));
    getChildren().add(elapsedTimeText);
    getChildren().add(activePlayer);
    this.setSpacing(400);
    
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateStatusText()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Updates the status text periodically with the elapsed time.
   */
  private void updateStatusText() {
    if (seconds > 59) {
      minutes++;
      seconds = 0;
    }

    String mins = "";

    if (minutes > 0)
      mins = minutes + " minutes ";

    elapsedTimeText.setText("Elapsed time: " + mins + (seconds++) + " seconds");
  }
  
  /**
   * Updates the active player text.
   */
  public void setActivePlayerText(String playerName) {
	  activePlayer.setText("Waiting on " + playerName + "...");
  }

}
