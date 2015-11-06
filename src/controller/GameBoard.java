package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import model.PlayerZone;
import model.card.Card;
import model.card.CardDeck;
import view.CardPileView;
import view.CardTheme;
import view.CardView;
import view.CardViewFactory;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class represents the area where the game is taking place.
 */
public class GameBoard extends Pane {

  /**
   * The list of {@link CardView} objects that are on the playing area.
   */
  List<CardView> cardViewList = new ArrayList<>();

  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * player 1's hand pile.
   */
  private CardPileView p1_handPileView;
  
  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * player 2's hand pile.
   */
  private CardPileView p2_handPileView;
  
  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * the foundation piles.
   */
  private List<CardPileView> p1_foundationPileViews;
  
  /**
   * The list of {@link CardPileView} objects that serves as the view for
   * the foundation piles.
   */
  private List<CardPileView> p2_foundationPileViews;

  /**
   * The {@link CardPileView} object that serves as the view for the stock.
   */
  private CardPileView drawCardsView;

  /**
   * The {@link CardPileView} object that serves as the view for the pile.
   */
  private CardPileView pileView;

  /**
   * The gap (offset) between the cards.
   */
  private double cardGapVertical = 30;
  
  /**
   * Represents string of player 1's name
   */
  private Label p1_name;
  
  /**
   * Represents string of player 2's name
   */
  private Label p2_name;
  
  /**
   * Ready button for player 1
   */
  private Button p1_ready;
  
  /**
   * Ready button for player 2
   */
  private Button p2_ready;
  
  private InputManager mouseUtility;

  /**
   * Constructs a new {@link GameBoard} object.
   */
  public GameBoard() {
    this.p1_handPileView = new CardPileView(0, 40, 0, 0, "1hand");
    this.p2_handPileView = new CardPileView(0, 40, 0, 0, "2hand");
    this.p1_foundationPileViews = FXCollections.observableArrayList();
    this.p2_foundationPileViews = FXCollections.observableArrayList();
    this.drawCardsView = new CardPileView(1, 0, 0, 0, "drawCards");
    this.pileView = new CardPileView(1, 0, 0, 0, "pile");
    
    this.p1_ready = new Button("Player 1 Ready?");
    this.p1_ready.setId("default-btn");
    this.p2_ready = new Button("Player 2 Ready?");
    this.p2_ready.setId("default-btn");
    
    this.p1_name = new Label("Player 1");
    this.p2_name = new Label("Player 2");
    
    
    initGameArea();
  }

  /**
   * Constructs a new {@link GameBoard} object, with the given image
   * as the background.
   *
   * @param tableauBackground The {@link Image} object for the background.
   */
  public GameBoard(Image tableBackground) {
    this();
    setTableBackground(tableBackground);
  }

  /**
   * Initializes the game area.
   * Calls methods to build deck, waste, hand, and card pile views.
   */
  private void initGameArea() {
	//place player name labels
	placeLabel(p1_name, 525, 75);
	placeLabel(p2_name, 525, 600);
	  
	//pass in x,y locations for deck and waste piles
    buildStock(500, 275);
    buildWaste(650, 275);
    
    //pass in x,y locations and number for player 1 card piles
    int p1_numOfPiles = 3;
    buildFoundationPiles(50, 20, 1, p1_foundationPileViews, p1_numOfPiles);
    
    //pass in x,y locations and number for player 2 card piles
    int p2_numOfPiles = 3;
    buildFoundationPiles(50, 550, 2, p2_foundationPileViews, p2_numOfPiles);
    
    //pass in x,y locations for hand piles
    buildHandPiles(650, 20, p1_handPileView);
    buildHandPiles(650, 550, p2_handPileView);
    
    //place ready buttons on game board
    placeReadyButton(p1_ready, 1000, 75);
    placeReadyButton(p2_ready, 1000, 625);
    p2_ready.setVisible(false);
  }
  
  private void placeLabel(Label label, int x, int y){
	  label.setTranslateX(x);
	  label.setTranslateY(y);
	  label.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");
	  getChildren().add(label);
  }

  /**
   * Configures the {@link CardPileView} object that serves as the view
   * of the stock.
   */
  private void buildStock(int x, int y) {
    BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    drawCardsView.setPrefSize(130, 180);
    drawCardsView.setBackground(background);
    drawCardsView.setLayoutX(x);
    drawCardsView.setLayoutY(y);
    drawCardsView.setEffect(gaussianBlur);
    getChildren().add(drawCardsView);
  }

  /**
   * Configures the {@link CardPileView} object that serves as the view
   * of the waste.
   */
  private void buildWaste(int x, int y) {
    BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    pileView.setPrefSize(130, 180);
    pileView.setBackground(background);
    pileView.setLayoutX(x);
    pileView.setLayoutY(y);
    pileView.setEffect(gaussianBlur);
    getChildren().add(pileView);
  }

  /**
   * Configures the {@link CardPileView} objects that serves as the view
   * of the foundation piles.
   */
  private void buildFoundationPiles(int x, int y, int playerNumber, List<CardPileView> foundationPileViews, int numOfPiles) {
	 BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    IntStream.range(0, numOfPiles).forEach(i -> {
      foundationPileViews.add(new CardPileView(2, 0, (x + i * 160), y, playerNumber+"tableCards"+(i+1)));
      foundationPileViews.get(i).setPrefSize(130, 180);
      foundationPileViews.get(i).setBackground(background);
      foundationPileViews.get(i).setLayoutX(x + i * 160);
      foundationPileViews.get(i).setLayoutY(y);
      foundationPileViews.get(i).setEffect(gaussianBlur);
      getChildren().add(foundationPileViews.get(i));
    });
  }

  /**
   * Configures the {@link CardPileView} objects that serves as the view
   * of the standard piles.
 * @param p1_handView 
 * @param y 
 * @param x 
   */
  private void buildHandPiles(int x, int y, CardPileView p1_handView) {
    BackgroundFill backgroundFill = new BackgroundFill(
        Color.gray(0.0, 0.2), null, null);

    Background background = new Background(backgroundFill);

    GaussianBlur gaussianBlur = new GaussianBlur(10);

    p1_handView.setPrefSize(130, 180);
    p1_handView.setBackground(background);
    p1_handView.setLayoutX(x);
    p1_handView.setLayoutY(y);
    p1_handView.setEffect(gaussianBlur);
    getChildren().add(p1_handView);
  }
  
  /**
   * Place ready button
   * @param button
   * @param x
   * @param y
   */
  private void placeReadyButton(Button button, int x, int y) {
	  button.setTranslateX(x);
	  button.setTranslateY(y);
	  getChildren().add(button);
  }
  
  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of player 1's hand.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public CardPileView getP1_HandPileView() {
    return p1_handPileView;
  }

  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of player 1's hand.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public CardPileView getP2_HandPileView() {
    return p2_handPileView;
  }
  
  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of the player card piles.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public List<CardPileView> getP1_FoundationPileViews() {
	return p1_foundationPileViews;
  }
  
  /**
   * Returns the {@link List} of {@link CardPileView} objects that serves
   * as the view of the player card piles.
   *
   * @return The {@link List} of {@link CardPileView} objects.
   */
  public List<CardPileView> getP2_FoundationPileViews() {
    return p2_foundationPileViews;
  }

  /**
   * Returns the {@link CardPileView} object that serves as the view
   * of the draw cards pile.
   *
   * @return The {@link CardPileView} object.
   */
  public CardPileView getDrawCardsView() {
    return drawCardsView;
  }

  /**
   * Returns the {@link CardPileView} object that serves as the view
   * of the waste.
   *
   * @return The {@link CardPileView} object.
   */
  public CardPileView getPileView() {
    return pileView;
  }

  /**
   * Sets the background image for the table.
   *
   * @param tableBackground The {@link Image} object to set.
   */
  public void setTableBackground(Image tableBackground) {
    setBackground(new Background(new BackgroundImage(tableBackground,
        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
        BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
  }

  /**
   * Updates the card images of each card on the game area.
   * Mainly used when switching card themes.
   *
   * @param cardTheme The current card theme to update from.
   */
  public void updateCardViews(CardTheme cardTheme) {
    cardViewList.forEach(cardView -> {
      cardView.setFrontFace(cardTheme.getFrontFace(cardView.getShortID()));
      cardView.setBackFace(cardTheme.getBackFace());
    });
  }

  public Button getP1_ReadyButton(){
	  return p1_ready;
  }
  
  public Button getP2_ReadyButton(){
	  return p2_ready;
  }
  
  public void setButtonVisibility(Button b, boolean visible){
	  b.setVisible(visible);
  }
  
  public void setInputManager(InputManager mouseUtility){
	  this.mouseUtility = mouseUtility;
  }
  
  /**
   * Draws the deck of cards
   * @param drawCards
   */
  public void drawDeck(List<Card> drawCards){
	  Iterator<Card> deckIterator = drawCards.iterator();
		
	    deckIterator.forEachRemaining(card -> {
	      getDrawCardsView().addCardView(CardViewFactory.createCardView(card));
	      cardViewList.add(getDrawCardsView().getTopCardView());
	      getChildren().add(getDrawCardsView().getTopCardView());
	    });
	    drawCardsView.getTopCardView().setMouseTransparent(false);
	    
  }
  
  	/**
  	 * Called at the start of the game to draw initialized board state
  	 * @param playerPlace
  	 */
	public void drawPlayerPlace(PlayerZone playerPlace) {
		CardPileView foundationPileView_1;
		CardPileView foundationPileView_2;
		CardPileView foundationPileView_3;
		CardPileView handPileView;
		
		if(playerPlace.playerNumber == 1){
			foundationPileView_1 = getP1_FoundationPileViews().get(0);
			foundationPileView_2 = getP1_FoundationPileViews().get(1);
			foundationPileView_3 = getP1_FoundationPileViews().get(2);
			handPileView = getP1_HandPileView();
		} else {
			foundationPileView_1 = getP2_FoundationPileViews().get(0);
			foundationPileView_2 = getP2_FoundationPileViews().get(1);
			foundationPileView_3 = getP2_FoundationPileViews().get(2);
			handPileView = getP2_HandPileView();
		}
		
		//draw card pile 1
		Iterator<Card> deckIterator = playerPlace.tableCards1.iterator();
		
		deckIterator.forEachRemaining(card -> {
			foundationPileView_1.addCardView(CardViewFactory.createCardView(card));
            getChildren().add(foundationPileView_1.getTopCardView());
            cardViewList.add(foundationPileView_1.getTopCardView());
            foundationPileView_1.getTopCardView().setMouseTransparent(true);
		});
		foundationPileView_1.getTopCardView().setMouseTransparent(false);	
		
		//draw card pile 2
		deckIterator = playerPlace.tableCards2.iterator();
		deckIterator.forEachRemaining(card -> {
			foundationPileView_2.addCardView(CardViewFactory.createCardView(card));
            getChildren().add(foundationPileView_2.getTopCardView());
            cardViewList.add(foundationPileView_2.getTopCardView());
            foundationPileView_2.getTopCardView().setMouseTransparent(true);
		});
		foundationPileView_2.getTopCardView().setMouseTransparent(false);
		
		//draw card pile 3
		deckIterator = playerPlace.tableCards3.iterator();
		deckIterator.forEachRemaining(card -> {
			foundationPileView_3.addCardView(CardViewFactory.createCardView(card));
            getChildren().add(foundationPileView_3.getTopCardView());
            cardViewList.add(foundationPileView_3.getTopCardView());
            //mouseUtility.makeClickable(foundationPileView_3.getTopCardView());
            //mouseUtility.makeDraggable(foundationPileView_3.getTopCardView());
            foundationPileView_3.getTopCardView().setMouseTransparent(true);
		});
		foundationPileView_3.getTopCardView().setMouseTransparent(false);
		
		//draw hand
		deckIterator = playerPlace.hand.iterator();
		deckIterator.forEachRemaining(card -> {
			handPileView.addCardView(CardViewFactory.createCardView(card));
	        cardViewList.add(handPileView.getTopCardView());
	        
	        if(playerPlace.playerNumber == 1){
	        	mouseUtility.makeClickable(handPileView.getTopCardView());
	        	mouseUtility.makeDraggable(handPileView.getTopCardView());
	        }
	        else{
	        	handPileView.getTopCardView().flip();
	        }
	        	
	        
	        getChildren().add(handPileView.getTopCardView());
		});
	}
	
	  /**
	   * Deactivates current player's cards and activates the other player's cards.
	   * Changes drag event status.
	   */
	  public void switchActivePlayer(int activePlayerNumber) {
		  if(activePlayerNumber == 1){
			  for(CardView cardView : getP1_HandPileView()){
					mouseUtility.removeDraggable(cardView);
					cardView.flip();
				}
				
				for(CardView cardView : getP2_HandPileView()){
					mouseUtility.makeDraggable(cardView);
					cardView.flip();
				}
			  
		  } else if (activePlayerNumber == 2){
			  for(CardView cardView : getP2_HandPileView()){
					mouseUtility.removeDraggable(cardView);
					cardView.flip();
				}
				
				for(CardView cardView : getP1_HandPileView()){
					mouseUtility.makeDraggable(cardView);
					cardView.flip();
				}
		  }
	  }
}
