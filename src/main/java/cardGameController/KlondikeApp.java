package cardGameController;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import cardGameModel.Card;
import cardGameModel.CardPile;
import cardGameView.CardPileView;
import cardGameView.CardTheme;
import cardGameView.CardView;
import cardGameView.CardViewFactory;

import java.util.Iterator;

/**
 * This class serves as the entry point of the application.
 */
public class KlondikeApp extends Application {

  /**
   * Width of the main window.
   */
  private static final double WIDTH = 1280;

  /**
   * Height of the main window.
   */
  private static final double HEIGHT = 800;

  /**
   * Reference to a {@link KlondikeGame} instance.
   */
  KlondikeGame game;

  /**
   * Reference to a {@link KlondikeGameArea} instance.
   */
  KlondikeGameArea gameArea;

  /**
   * Reference to a {@link KlondikeMouseUtil} instance.
   */
  KlondikeMouseUtil mouseUtil;

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
    gameArea = new KlondikeGameArea(new Image("/tableaous/green-felt.png"));

    // Menu bar
    KlondikeMenu menuBar = new KlondikeMenu(this);

    // Status bar
    KlondikeStatusBar statusBar = new KlondikeStatusBar();

    // Main element
    BorderPane bord = new BorderPane();
    bord.setCenter(gameArea);
    bord.setTop(menuBar);
    bord.setBottom(statusBar);

    Scene scene = new Scene(bord, WIDTH, HEIGHT);

    cardTheme = new CardTheme("/cardfaces/classic/theme.json", "/backfaces/bb.png");
    CardViewFactory.setCardTheme(cardTheme);

    game = new KlondikeGame();
    game.startNewGame();
    mouseUtil = new KlondikeMouseUtil(game, gameArea);
    prepareGameAreaForNewGame();
    
    // auto-flip cards
/*    for (int i = 0; i < game.getStandardPiles().size(); i++) {
      CardPileView actPileView = gameArea.getStandardPileViews().get(i);
      CardPile actPile = game.getPileById(actPileView.getShortID());

      actPileView.getCards().addListener(
          (ListChangeListener<CardView>) c -> {
            while (c.next()) {
              if (c.wasRemoved()) {
                if (!actPileView.isEmpty()) {
                  CardView toFlip = actPileView.getTopCardView();
                  toFlip.setMouseTransparent(false);
                  if (toFlip.isFaceDown())
                    toFlip.flip();
                }

                if (!actPile.isEmpty() && actPile.getTopCard().isFaceDown()) {
                  actPile.getTopCard().flip();
                  LOG.info("Flipped {}", actPile.getTopCard());
                }
              }
            }
          });
    }
*/
    primaryStage.setTitle("Card Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Sets up the {@link KlondikeGameArea} object for a new game.
   */
  private void prepareGameAreaForNewGame() {
    // deal to piles
    Iterator<Card> deckIterator = game.getDeck().iterator();

    int cardsToPut = 3;
    
    //deal to player 1 card piles
    for (CardPileView p1_foundationPileView : gameArea.getP1_FoundationPileViews()){
    	for (int i = 0; i < cardsToPut; i++) {
    		p1_foundationPileView.addCardView(CardViewFactory.createCardView(deckIterator.next()));
            gameArea.getChildren().add(p1_foundationPileView.getTopCardView());
            gameArea.cardViewList.add(p1_foundationPileView.getTopCardView());
            p1_foundationPileView.getTopCardView().setMouseTransparent(true);
    	}
    	
    	p1_foundationPileView.getTopCardView().setMouseTransparent(false);
    }
    
    //deal to player 2 card piles
    for (CardPileView p2_foundationPileView : gameArea.getP2_FoundationPileViews()){
    	for (int i = 0; i < cardsToPut; i++) {
    		p2_foundationPileView.addCardView(CardViewFactory.createCardView(deckIterator.next()));
            gameArea.getChildren().add(p2_foundationPileView.getTopCardView());
            gameArea.cardViewList.add(p2_foundationPileView.getTopCardView());
            p2_foundationPileView.getTopCardView().setMouseTransparent(true);
    	}
    	
    	p2_foundationPileView.getTopCardView().setMouseTransparent(false);
    }
    
    //deal to player 1 hand
    for (int i = 0; i < cardsToPut; i++) {
    	gameArea.getP1_HandPileView().addCardView(CardViewFactory.createCardView(deckIterator.next()));
        mouseUtil.makeClickable(gameArea.getP1_HandPileView().getTopCardView());
        gameArea.cardViewList.add(gameArea.getP1_HandPileView().getTopCardView());
        mouseUtil.makeDraggable(gameArea.getP1_HandPileView().getTopCardView());
        gameArea.getChildren().add(gameArea.getP1_HandPileView().getTopCardView());
	}
    
    //deal to player 2 hand
    for (int i = 0; i < cardsToPut; i++) {
    	gameArea.getP2_HandPileView().addCardView(CardViewFactory.createCardView(deckIterator.next()));
        mouseUtil.makeClickable(gameArea.getP2_HandPileView().getTopCardView());
        gameArea.cardViewList.add(gameArea.getP2_HandPileView().getTopCardView());
        mouseUtil.makeDraggable(gameArea.getP2_HandPileView().getTopCardView());
        gameArea.getChildren().add(gameArea.getP2_HandPileView().getTopCardView());
	}
    
/*
    for (CardPileView standardPileView : gameArea.getStandardPileViews()) {
      for (int i = 0; i < cardsToPut; i++) {
        standardPileView.addCardView(CardViewFactory.createCardView(deckIterator.next()));
        gameArea.getChildren().add(standardPileView.getTopCardView());
        gameArea.cardViewList.add(standardPileView.getTopCardView());
        mouseUtil.makeDraggable(standardPileView.getTopCardView());
        standardPileView.getTopCardView().setMouseTransparent(true);
      }

      standardPileView.getTopCardView().setMouseTransparent(false);
      cardsToPut++;
    }
*/
    deckIterator.forEachRemaining(card -> {
      gameArea.getStockView().addCardView(CardViewFactory.createCardView(card));
      mouseUtil.makeClickable(gameArea.getStockView().getTopCardView());
      gameArea.cardViewList.add(gameArea.getStockView().getTopCardView());
      gameArea.getChildren().add(gameArea.getStockView().getTopCardView());
    });

    gameArea.getStockView().setOnMouseClicked(mouseUtil.stockReverseCardsHandler);
  }

}
