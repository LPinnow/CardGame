package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import model.IdiotGameStateFacade;
import model.card.Card;

/**
 * This class represents the area where the game is taking place.
 * 
 * This class has code based upon the following project:
 * Zoltan Dalmadi, "JCardGamesFX", 2015, GitHub repository, github.com/ZoltanDalmadi/JCardGamesFX.
 */
public class GameBoard extends Pane {

  /**
   * The list of {@link CardView} objects that are on the playing area.
   */
  List<CardView> cardViewList = new ArrayList<>(); 

  private List<List<CardPileView>> playerHands;
  
  private Map<Integer, List<CardPileView>> foundationPiles;
  

  /**
   * The {@link CardPileView} object that serves as the view for the stock.
   */
  private CardPileView deckView;

  /**
   * The {@link CardPileView} object that serves as the view for the pile.
   */
  private CardPileView wasteView;

  
  /**
   * Represents string of player 1's name
   */
  private Label p1_name;
  
  /**
   * Represents string of player 2's name
   */
  private Label p2_name;
  
  private Label messageLabel;
  
  /**
   * Ready button for player 1
   */
  private Button p1_ready;
  
  /**
   * Ready button for player 2
   */
  private Button p2_ready;
  
  private List<CardPileView> allPileViews;
  
  private InputManager mouseUtility;
  
  private IdiotGameStateFacade currentGameState;

  /**
   * Constructs a new {@link DeCoupGameBoard} object.
   */
  public GameBoard(ArrayList<CardPileView> piles) {
	playerHands = new ArrayList<List<CardPileView>>();
	foundationPiles = new HashMap<Integer, List<CardPileView>>();
	int TOTAL_NUM_OF_PLAYERS = 2;
	//feed game rules # of players in
	playerHands.add(0, new ArrayList<CardPileView>());
	for (int i = 1; i < TOTAL_NUM_OF_PLAYERS + 1; i++) {
		foundationPiles.put(i, FXCollections.observableArrayList());
		playerHands.add(i, new ArrayList<CardPileView>());
	}
	this.p1_ready = new Button("Player 1 Ready?");
    this.p1_ready.setId("default-btn");
    this.p2_ready = new Button("Player 2 Ready?");
    this.p2_ready.setId("default-btn");
    
    this.messageLabel = new Label("");
    this.messageLabel.setTextFill(Color.RED);
    
    allPileViews = new ArrayList<CardPileView>();
	
	initGameArea(piles);
  }

  /**
   * Constructs a new {@link DeCoupGameBoard} object, with the given image
   * as the background.
   *
   * @param tableauBackground The {@link Image} object for the background.
   */
  public GameBoard(ArrayList<CardPileView> piles, Image tableBackground) {
    this(piles);
    setTableBackground(tableBackground);
  }

  /**
   * Initializes the game area.
   * Calls methods to build deck, waste, hand, and card pile views.
   */
  private void initGameArea(ArrayList<CardPileView> piles) {
	  this.p1_name = new Label("Player 1");
	  this.p2_name = new Label("Player 2");
	  
	  placeLabel(p1_name, 525, 600, 18);
	  placeLabel(p2_name, 525, 75, 18);
	  
	  for (CardPileView pileView : piles) {
			if (pileView.getShortID().toLowerCase().contains("hand")) {
				playerHands.get(Integer.parseInt(pileView.getShortID().toLowerCase().substring(1, 2))).add(pileView);
				buildPile(pileView);
			} else if (pileView.getShortID().toLowerCase().contains("foundation")) {
				foundationPiles.get(Integer.parseInt(pileView.getShortID().toLowerCase().substring(1, 2))).add(pileView);
				buildPile(pileView);
			} else if (pileView.getShortID().toLowerCase().contains("deck")) {
				this.deckView = pileView;
				buildPile(deckView);
			} else if (pileView.getShortID().toLowerCase().contains("waste")){
				this.wasteView = pileView;
				buildPile(wasteView);
			}
			
			allPileViews.add(pileView);
		}
	  
	    placeReadyButton(p1_ready, 1000, 625);
	    placeReadyButton(p2_ready, 1000, 75);
	    p2_ready.setVisible(false);
 
		placeLabel(messageLabel, 50, 500, 14);
  }
  
  private void buildPile(CardPileView cardPileView) {
	  BackgroundFill backgroundFill = new BackgroundFill(
		        Color.gray(0.0, 0.2), null, null);

		    Background background = new Background(backgroundFill);

		    GaussianBlur gaussianBlur = new GaussianBlur(10);

		    cardPileView.setPrefSize(130, 180);
		    cardPileView.setBackground(background);
		    cardPileView.setLayoutX(cardPileView.getInitialX());
		    cardPileView.setLayoutY(cardPileView.getInitialY());
		    cardPileView.setEffect(gaussianBlur);
		    getChildren().add(cardPileView);	    
  }
  
  private void placeLabel(Label label, int x, int y, int fontSize){
	  label.setTranslateX(x);
	  label.setTranslateY(y);
	  label.setStyle("-fx-font-size: " + fontSize + "pt; -fx-font-weight: bold;");
	  getChildren().add(label);
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
  
  
  public CardPileView getPlayerHandView(int playerNumber) {
	 return playerHands.get(playerNumber).get(0);
  }

  
  public List<CardPileView> getFoundationPileViews(int playerNumber) {
	  return foundationPiles.get(playerNumber);
  }
  

  /**
   * Returns the {@link CardPileView} object that serves as the view
   * of the draw cards pile.
   *
   * @return The {@link CardPileView} object.
   */
  public CardPileView getDeckView() {
    return deckView;
  }

  /**
   * Returns the {@link CardPileView} object that serves as the view
   * of the waste.
   *
   * @return The {@link CardPileView} object.
   */
  public CardPileView getWasteView() {
    return wasteView;
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
      /*TODO:  solve update issue for theme files */
      
    });
  }
  
  private CardView getCardViewById(String id) {
	  for(CardView cardView: cardViewList) {
		  if(cardView.getShortID() == id) {
			  return cardView;
		  }
	  }
	  return null;
  }
  
  public CardPileView getNearestPile(CardView cardView) {
	  double cardWidth = cardView.getFitWidth();
	  double cardHeight = cardView.getFitHeight();
	  
	  double viewX = cardView.getLayoutX() + cardView.getTranslateX() + (cardWidth/2);
	  double viewY = cardView.getLayoutY() + cardView.getTranslateY() + (cardHeight/2);
	  double diffX, diffY;
	  double distance = 0, prevDistance = 0;
	  int closestPileByIndex = 0;	  
	  
	  for(int i = 0; i < allPileViews.size(); i++) {
		  CardPileView currentPile = allPileViews.get(i);
		  
		  //Don't check pile against itself
		  if(currentPile == cardView.getContainingPile()) {
			  continue;
		  }
		  double cardPileWidth = (currentPile.getCards().size() * cardWidth) + (currentPile.getCards().size()-2 * currentPile.getCardGapHorizontal());
		  double cardPileHeight = (currentPile.getCards().size() * cardHeight) + (currentPile.getCards().size()-2 * currentPile.getCardGapVertical());
		  
		  diffX = viewX - (currentPile.getLayoutX() + cardPileWidth/2);
		  diffY = viewY - (currentPile.getLayoutY() + cardPileHeight/2);
		  
		  distance = Math.hypot(diffX, diffY);
		  if(i == 0 || distance < prevDistance) {
			  prevDistance = distance;
			  closestPileByIndex = i;
		  }
	  }
	  return allPileViews.get(closestPileByIndex);
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
  
  public void updateCurrentState(IdiotGameStateFacade currentGameState) {
	  this.currentGameState = currentGameState;
  }
  
  public void setMessageLabelText(String text){
	  messageLabel.setText(text);
  }
  
  /**
   * Draws the deck of cards
   * @param drawCards
   */
  public void drawDeck(){
	  Iterator<Card> deckIterator = currentGameState.GetFullDeck().iterator();
	  
	    deckIterator.forEachRemaining(card -> {
	      getDeckView().addCardView(CardViewFactory.createCardView(card));
	      cardViewList.add(getDeckView().getTopCardView());
	      getChildren().add(getDeckView().getTopCardView());
	      mouseUtility.makeClickable(getDeckView().getTopCardView());
	    });
	    deckView.getTopCardView().setMouseTransparent(false);
  }
  
  public void drawBothPlayerPlaces() {
	  drawPlayerPlace(1);
	  drawPlayerPlace(2);
  }
  
  	/**
  	 * Called at the start of the game to draw initialized board state
  	 * @param playerPlace
  	 */
	public void drawPlayerPlace(int playerNumber) {
		CardPileView foundationPileView_1;
		CardPileView foundationPileView_2;
		CardPileView foundationPileView_3;
		CardPileView handPileView;

		if(playerNumber == 1){
			foundationPileView_1 = foundationPiles.get(1).get(0);
			foundationPileView_2 =  foundationPiles.get(1).get(1);
			foundationPileView_3 =  foundationPiles.get(1).get(2);
			handPileView = playerHands.get(1).get(0);
		} else {
			foundationPileView_1 = foundationPiles.get(2).get(0);
			foundationPileView_2 = foundationPiles.get(2).get(1);
			foundationPileView_3 = foundationPiles.get(2).get(2);
			handPileView = playerHands.get(2).get(0);
		}
		
		//draw card pile 1

		List<Card> tableCards1 = currentGameState.getPlayerPlaces().get(playerNumber - 1).getAllTableCards1();
		foundationPileView_1.clearContents();
		for (Card card : tableCards1) {
	    	foundationPileView_1.addCardView(CardViewFactory.createCardView(card));
	    	getChildren().add(foundationPileView_1.getTopCardView());
	    	mouseUtility.slideFromDeck(foundationPileView_1.getTopCardView(), 300);
	    	foundationPileView_1.getTopCardView().setMouseTransparent(false);	
	    }
		
//		if (tableCards1.getTopCard() != null) {
//			foundationPileView_1.clearContents();
//			
//			foundationPileView_1.addCardView(CardViewFactory.createCardView(tableCards1.getTopCard()));
//			
//	        getChildren().add(foundationPileView_1.getTopCardView());
//	        mouseUtility.slideFromDeck(foundationPileView_1.getTopCardView(), 300);
//			foundationPileView_1.getTopCardView().setMouseTransparent(false);	
//		}
			
		//draw card pile 2
		
		List<Card> tableCards2 = currentGameState.getPlayerPlaces().get(playerNumber - 1).getAllTableCards2();
    	foundationPileView_2.clearContents();
	    for (Card card : tableCards2) {
	    	foundationPileView_2.addCardView(CardViewFactory.createCardView(card));
	    	getChildren().add(foundationPileView_2.getTopCardView());
	    	mouseUtility.slideFromDeck(foundationPileView_2.getTopCardView(), 600);
	    	foundationPileView_2.getTopCardView().setMouseTransparent(false);	
	    }
//		if (tableCards2.getTopCard() != null) {
//			foundationPileView_2.clearContents();
//			foundationPileView_2.addCardView(CardViewFactory.createCardView(tableCards2.getTopCard()));
//	        getChildren().add(foundationPileView_2.getTopCardView());
//	        mouseUtility.slideFromDeck(foundationPileView_2.getTopCardView(), 600);
//			foundationPileView_2.getTopCardView().setMouseTransparent(false);	
//		}
		
		//draw card pile 3
		
		List<Card> tableCards3 = currentGameState.getPlayerPlaces().get(playerNumber - 1).getAllTableCards1();
	    //TopCardUpStack tableCards3 = currentGameState.getPlayerPlaces().get(playerNumber - 1).getTableCards3();
    	foundationPileView_3.clearContents();
		for (Card card : tableCards3) {
	    	foundationPileView_3.addCardView(CardViewFactory.createCardView(card));
	    	getChildren().add(foundationPileView_3.getTopCardView());
	    	mouseUtility.slideFromDeck(foundationPileView_3.getTopCardView(), 900);
	    	foundationPileView_3.getTopCardView().setMouseTransparent(false);	
	    }
//		if (tableCards3.getTopCard() != null) {
//			foundationPileView_3.clearContents();
//			foundationPileView_3.addCardView(CardViewFactory.createCardView(tableCards3.getTopCard()));
//	        getChildren().add(foundationPileView_3.getTopCardView());
//	        mouseUtility.slideFromDeck(foundationPileView_3.getTopCardView(), 900);
//			foundationPileView_3.getTopCardView().setMouseTransparent(false);	
//		}
		
		

		
		handPileView.clearContents();
		
		int dealDelay = 1200;
		List<Card> hand = currentGameState.getPlayerPlaces().get(playerNumber - 1).getHand().getCards();

		for (int i = 0; i < hand.size(); i++) {
			handPileView.addCardView(CardViewFactory.createCardView(hand.get(i)));
			mouseUtility.makeClickable(handPileView.getTopCardView());
        	mouseUtility.makeDraggable(handPileView.getTopCardView());
			
			getChildren().add(handPileView.getTopCardView());
	        mouseUtility.slideFromDeck(handPileView.getTopCardView(), dealDelay);
			dealDelay += 300;
		}
		
	}
	
	public void updatePlayerPlace(int playerNumber) {
		CardPileView foundationPileView_1;
		CardPileView foundationPileView_2;
		CardPileView foundationPileView_3;
		CardPileView handPileView;

		if(playerNumber == 1){
			foundationPileView_1 = foundationPiles.get(1).get(0);
			foundationPileView_2 =  foundationPiles.get(1).get(1);
			foundationPileView_3 =  foundationPiles.get(1).get(2);
			handPileView = playerHands.get(1).get(0);
		} else {
			foundationPileView_1 = foundationPiles.get(2).get(0);
			foundationPileView_2 = foundationPiles.get(2).get(1);
			foundationPileView_3 = foundationPiles.get(2).get(2);
			handPileView = playerHands.get(2).get(0);
		}
		
		//update foundation cards.

		
		List<Card> hand = currentGameState.getPlayerPlaces().get(playerNumber - 1).getHand().getCards();
		
			
		
		for (int i = 0; i < hand.size(); i++) {
			CardView repView = getCardViewById(hand.get(i).getId());

			if(!handPileView.getCards().contains(repView) && repView != null) {				
				mouseUtility.slideToPile(repView, repView.getContainingPile(), handPileView, false);
					repView.setToFaceUp();
					mouseUtility.makeClickable(repView);
					mouseUtility.makeDraggable(repView);
				
			}
		}
//		deckIterator.forEachRemaining(card -> {
//			handPileView.addCardView(CardViewFactory.createCardView(card));
//	        
//	        if(playerNumber == 1){
//	        	mouseUtility.makeClickable(handPileView.getTopCardView());
//	        	mouseUtility.makeDraggable(handPileView.getTopCardView());
//	        }
//	        else{
//	        	mouseUtility.makeClickable(handPileView.getTopCardView());
//	        	mouseUtility.makeDraggable(handPileView.getTopCardView());
//	        }
//
//	        getChildren().add(handPileView.getTopCardView());
//		});
		
	}
	
	  /**
	   * Deactivates current player's cards and activates the other player's cards.
	   * Changes drag event status.
	   */
	  public void setActivePlayer(int activePlayerNumber) {
		  if(activePlayerNumber == 1){
			  for(CardView cardView : playerHands.get(1).get(0)){
					mouseUtility.makeDraggable(cardView);
					cardView.setToFaceUp();
				}
				
				for(CardView cardView : playerHands.get(2).get(0)){
					mouseUtility.removeDraggable(cardView);
					cardView.setToFaceDown();
				}
			  
		  } else if (activePlayerNumber == 2){
			  for(CardView cardView : playerHands.get(2).get(0)){
					mouseUtility.makeDraggable(cardView);
					cardView.setToFaceUp();
				}
				
				for(CardView cardView : playerHands.get(1).get(0)){
					mouseUtility.removeDraggable(cardView);
					cardView.setToFaceDown();
				}
		  }
	  }
	  
	  public void changePlayerTurn(int currentPlayer) {
		  if(currentPlayer == 1) {
			  setActivePlayer(2);
		  } else {
			  setActivePlayer(1);
		  }
		  
	  }
}
