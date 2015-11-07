package controller;

import view.CardTheme;
import view.CardViewFactory;
import view.GameBoard;
import view.GameMenu;
import controller.validators.MoveValidator;
import controller.validators.TableSwapValidator;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This class serves as the entry point of the application.
 */
public class CardGameMain extends Application {

	/**	
   * Width of the main window.
   */
  private static final double WIDTH = 1280;

  /**
   * Height of the main window.
   */
  private static final double HEIGHT = 800;

  /**
   * Reference to a {@link IIdiotGameEngine} instance.
   */
  IIdiotGameEngine gameEngine;

  /**
   * Reference to a {@link GameBoard} instance.
   */
  public GameBoard gameBoard;
  
  /**
   * Reference to a StatusBar
   */
  StatusBar statusBar;

  /**
   * Reference to a {@link InputManager} instance.
   */
  InputManager mouseUtility;

  /**
   * The current theme of the cards.
   */
  public CardTheme cardTheme;

  /**
   * Main function of the application.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * The main function effectively launches this method, as in all
   * JavaFX applications.
   *
   * @param primaryStage Reference to the main {@link Stage} object.
   */
  @Override
  public void start(Stage primaryStage) {

    // Game area
    gameBoard = new GameBoard(new Image("/tableaous/green-felt.png"));

    // Menu bar
    GameMenu menuBar = new GameMenu(this);

    // Status bar
    statusBar = new StatusBar();

    // Main element
    BorderPane bord = new BorderPane();
    bord.setCenter(gameBoard);
    bord.setTop(menuBar);
    bord.setBottom(statusBar);

    Scene scene = new Scene(bord, WIDTH, HEIGHT);
    scene.getStylesheets().add("styles/main.css");

    cardTheme = new CardTheme("/cardfaces/classic/theme.json", "/backfaces/bb.png");
    CardViewFactory.setCardTheme(cardTheme);

    gameEngine = new IdiotGameEngine(new TableSwapValidator(), new MoveValidator(), new EndGameChecker());
    
    mouseUtility = new InputManager(gameEngine, gameBoard);
    gameBoard.setInputManager(mouseUtility);
    gameEngine.initializeNewGame(2, new RuleConfigurationLoader("/configuration/idiotRules.json"));
    gameBoard.updateCurrentState(gameEngine.getCurrentGameState());
    gameBoard.drawDeck();
    gameBoard.drawBothPlayerPlaces();
    gameBoard.setActivePlayer(1);
    setReadyButtonEventHandlers();
    
    primaryStage.setTitle("Card Game");
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  /**
   * Sets up the {@link GameBoard} object for a new game.
   */
  private void setReadyButtonEventHandlers() {
    
   /**
    * Hide ready button when clicked and start game if other player is ready
    */  
    gameBoard.getP1_ReadyButton().setOnMouseClicked(new EventHandler<Event>(){
    	@Override
		public void handle(Event e) {
			gameBoard.setButtonVisibility(gameBoard.getP1_ReadyButton(), false);
			gameBoard.setButtonVisibility(gameBoard.getP2_ReadyButton(), true);
			
			gameBoard.setActivePlayer(2);
			
		}});
    
    /**
     * Hide ready button when clicked and start game if other player is ready
    */  
    gameBoard.getP2_ReadyButton().setOnMouseClicked(new EventHandler<Event>(){
    	@Override
		public void handle(Event e) {
			gameBoard.setButtonVisibility(gameBoard.getP2_ReadyButton(), false);
			gameBoard.setActivePlayer(1);
			
			mouseUtility.makeClickable(gameBoard.getDrawCardsView().getTopCardView());
			gameEngine.beginPlay();
		}});
  }
  
  public InputManager getMouseUtility(){
	  return mouseUtility;
  }

}
