package controller;

import view.CardTheme;
import view.CardViewFactory;
import view.GameBoard;
import view.GameMenu;
import view.InputManager;
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
	
	public ConfigurationLoader configurationManager;

	/**
	 * Main function of the application.
	 *
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * The main function effectively launches this method, as in all JavaFX
	 * applications.
	 *
	 * @param primaryStage
	 *            Reference to the main {@link Stage} object.
	 */
	@Override
	public void start(Stage primaryStage) {
		configurationManager = new ConfigurationLoader("/configurationfiles/idiotCards.json",
				"/configurationfiles/idiotGameBoard.json",
				"/configurationfiles/idiotPlayers.json",
				"/configurationfiles/idiotRules.json","/configurationfiles/idiotTheme.json");
		
		configurationManager.loadTheme();
		// Game area
		gameBoard = new GameBoard(configurationManager.loadGameBoardLayout(),
				new Image(configurationManager.getDefaultBackground()));

		// Menu bar
		GameMenu menuBar = new GameMenu(this);

		// Status bar
		statusBar = new StatusBar();

		// Main element
		BorderPane bord = new BorderPane();
		bord.setCenter(gameBoard);
		bord.setTop(menuBar);
		bord.setBottom(statusBar);

		Scene scene = new Scene(bord, configurationManager.getLayoutWidth(), configurationManager.getLayoutHeight());
		scene.getStylesheets().add(configurationManager.getStylesheet());

		cardTheme = new CardTheme(configurationManager.getFrontFace(),
				configurationManager.getBackFace());
		CardViewFactory.setCardTheme(cardTheme);

		EndGameChecker endGameChecker = new EndGameChecker();

		gameEngine = new IdiotGameEngine(new TableSwapValidator(),
				new MoveValidator(), endGameChecker, new MoveExecutor(
						endGameChecker));

		mouseUtility = new InputManager(gameEngine, gameBoard);
		gameBoard.setInputManager(mouseUtility);
		gameEngine.initializeNewGame(2, new RuleConfigurationLoader("/configurationfiles/idiotRules.json"));
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
		 * Hide ready button when clicked and start game if other player is
		 * ready
		 */
		gameBoard.getP1_ReadyButton().setOnMouseClicked(
				new EventHandler<Event>() {
					@Override
					public void handle(Event e) {
						gameBoard.setButtonVisibility(
								gameBoard.getP1_ReadyButton(), false);
						gameBoard.setButtonVisibility(
								gameBoard.getP2_ReadyButton(), true);
						gameEngine.playerOneDoneSwapping();
						gameBoard.setActivePlayer(2);

					}
				});

		/**
		 * Hide ready button when clicked and start game if other player is
		 * ready
		 */
		gameBoard.getP2_ReadyButton().setOnMouseClicked(
				new EventHandler<Event>() {
					@Override
					public void handle(Event e) {
						gameBoard.setButtonVisibility(
								gameBoard.getP2_ReadyButton(), false);
						gameBoard.setActivePlayer(1);

						mouseUtility.makeClickable(gameBoard.getDeckView()
								.getTopCardView());
						gameEngine.beginPlay();
						gameBoard.setActivePlayer(gameEngine
								.getCurrentGameState().CurrentPlayerTurn());
					}
				});
	}

	public InputManager getMouseUtility() {
		return mouseUtility;
	}

}
