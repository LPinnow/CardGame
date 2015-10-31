package controller;

import java.util.Iterator;

import view.CardPileView;
import view.CardTheme;
import view.CardViewFactory;
import model.Card;
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
   * Reference to a {@link CardGame} instance.
   */
  CardGame game;

  /**
   * Reference to a {@link GameBoard} instance.
   */
  GameBoard gameBoard;
  
  /**
   * Reference to a StatusBar
   */
  StatusBar statusBar;

  /**
   * Reference to a {@link MouseUtility} instance.
   */
  MouseUtility mouseUtility;

  /**
   * The current theme of the cards.
   */
  CardTheme cardTheme;

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

    cardTheme = new CardTheme("/cardfaces/classic/theme.json", "/backfaces/bb.png");
    CardViewFactory.setCardTheme(cardTheme);

    game = new CardGame();
    game.startNewGame();
    mouseUtility = new MouseUtility(game, gameBoard);
    
    //Get number of cards to deal player hands and extra piles
    int cardsToHandPiles = game.getRules().getHandCardNum();
    int cardsToPiles = game.getRules().getPileCardNum();
    prepareGameBoardForNewGame(cardsToHandPiles, cardsToPiles);
    
    primaryStage.setTitle("Card Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Sets up the {@link GameBoard} object for a new game.
   */
  private void prepareGameBoardForNewGame(int numCardsToHand, int numCardsToPile) {
    // deal to piles
    Iterator<Card> deckIterator = game.getDeck().iterator();
    
    //deal to player 1 card piles
    for (CardPileView p1_foundationPileView : gameBoard.getP1_FoundationPileViews()){
    	for (int i = 0; i < numCardsToPile; i++) {
    		p1_foundationPileView.addCardView(CardViewFactory.createCardView(deckIterator.next()));
            gameBoard.getChildren().add(p1_foundationPileView.getTopCardView());
            gameBoard.cardViewList.add(p1_foundationPileView.getTopCardView());
            p1_foundationPileView.getTopCardView().setMouseTransparent(true);
    	}
    	
    	p1_foundationPileView.getTopCardView().setMouseTransparent(false);
    }
    
    //deal to player 2 card piles
    for (CardPileView p2_foundationPileView : gameBoard.getP2_FoundationPileViews()){
    	for (int i = 0; i < numCardsToPile; i++) {
    		p2_foundationPileView.addCardView(CardViewFactory.createCardView(deckIterator.next()));
            gameBoard.getChildren().add(p2_foundationPileView.getTopCardView());
            gameBoard.cardViewList.add(p2_foundationPileView.getTopCardView());
            p2_foundationPileView.getTopCardView().setMouseTransparent(true);
    	}
    	
    	p2_foundationPileView.getTopCardView().setMouseTransparent(false);
    }
    
    //deal to player 1 hand
    for (int i = 0; i < numCardsToHand; i++) {
    	gameBoard.getP1_HandPileView().addCardView(CardViewFactory.createCardView(deckIterator.next()));
        mouseUtility.makeClickable(gameBoard.getP1_HandPileView().getTopCardView());
        gameBoard.cardViewList.add(gameBoard.getP1_HandPileView().getTopCardView());
        mouseUtility.makeDraggable(gameBoard.getP1_HandPileView().getTopCardView());
        gameBoard.getChildren().add(gameBoard.getP1_HandPileView().getTopCardView());
	}
    
    //deal to player 2 hand
    for (int i = 0; i < numCardsToHand; i++) {
    	gameBoard.getP2_HandPileView().addCardView(CardViewFactory.createCardView(deckIterator.next()));
        mouseUtility.makeClickable(gameBoard.getP2_HandPileView().getTopCardView());
        gameBoard.cardViewList.add(gameBoard.getP2_HandPileView().getTopCardView());
        mouseUtility.makeDraggable(gameBoard.getP2_HandPileView().getTopCardView());
        gameBoard.getChildren().add(gameBoard.getP2_HandPileView().getTopCardView());
	}
    
    //put the rest of the cards in the deck
    deckIterator.forEachRemaining(card -> {
      gameBoard.getStockView().addCardView(CardViewFactory.createCardView(card));
      //mouseUtility.makeClickable(gameBoard.getStockView().getTopCardView());
      gameBoard.cardViewList.add(gameBoard.getStockView().getTopCardView());
      gameBoard.getChildren().add(gameBoard.getStockView().getTopCardView());
    });

    gameBoard.getStockView().setOnMouseClicked(mouseUtility.stockReverseCardsHandler);
    
    /**
     * Hide ready button when clicked and start game if other player is ready
     */
    gameBoard.getP1_ReadyButton().setOnMouseClicked(new EventHandler<Event>(){
    	@Override
		public void handle(Event e) {
			gameBoard.setButtonVisibility(gameBoard.getP1_ReadyButton(), false);
			game.getPlayer1().setReady(true);
			game.getPlayer1().setActiveTurn(false);
			game.getPlayer2().setActiveTurn(true);
			statusBar.setActivePlayerText(game.getPlayer2().getName());
			gameBoard.setButtonVisibility(gameBoard.getP2_ReadyButton(), true);
			
		}});
    
    /**
     * Hide ready button when clicked and start game if other player is ready
     */
    gameBoard.getP2_ReadyButton().setOnMouseClicked(new EventHandler<Event>(){
    	@Override
		public void handle(Event e) {
			gameBoard.setButtonVisibility(gameBoard.getP2_ReadyButton(), false);
			game.getPlayer2().setReady(true);
			
			if(game.getPlayer1().isReady()){
				mouseUtility.makeClickable(gameBoard.getStockView().getTopCardView());
				game.setGameInProgress(true);
				game.getPlayer2().setActiveTurn(false);
				game.getPlayer1().setActiveTurn(true);
				statusBar.setActivePlayerText(game.getPlayer1().getName());
			}
		}});
  }
  
  public MouseUtility getMouseUtility(){
	  return mouseUtility;
  }

}
