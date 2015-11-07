package controller;

import java.util.List;
import java.util.ListIterator;

import view.CardPileView;
import view.CardView;
import view.GameBoard;
import model.IdiotGameState;
import model.card.Card;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * This class serves as the controller for the application.
 */
public class InputManager {

  /**
   * Helper inner class for determining the position of the mouse.
   */
  private final MousePos mousePos = new MousePos(); 

  /**
   * Dragged cards will be put into this list.
   */
  private Card draggedCard;

  /**
   * Same for the view of the cards.
   */
  private CardView draggedCardView;
  
  private int draggedCardViewIndex;
  
  private int draggedCardIndex;
  
  /**
   * The {@link IIdiotGameEngine} object to manipulate.
   */
  private IIdiotGameEngine game;

  /**
   * The {@link GameBoard} object to manipulate.
   */
  private GameBoard gameBoard;

  /**
   * This event handler is attached to cards that are still on the stock.
   * When the user clicks on a card, it will be flipped and put on the waste.
   */
  EventHandler<MouseEvent> onMouseClickedHandler = e -> {
    // put card from stock to waste and flip them
    /** get current cardView. */
    CardView cardView = (CardView) e.getSource();
    /** get current card. */
    Card card = game.getCurrentGameState().GetFullDeck().getById(cardView.getShortID());

    //game.drawFromStock(card);
    
    //TODO: The mouse click listener should not be moving the cards to the pile directly.. It should be submitting 
    //      the move to the controller and redrawing the updated the state from the controller if the move is successful.  
    
    gameBoard.getDrawCardsView().moveCardViewToPile(cardView, gameBoard.getPileView());
    cardView.setToFaceUp();
    cardView.setMouseTransparent(false);
    makeDraggable(cardView);
  };

  /**
   * This event handler is attached to the stock itself.
   * It puts all cards on the waste back on the stock.
   */
  EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
    // put all cards on waste back to stock and flip them
    //game.refillStockFromWaste();

    /** get view for waste. */
    CardPileView wasteView = gameBoard.getPileView();

    /** get view for stock. */
    CardPileView stockView = gameBoard.getDrawCardsView();

    /** reverse iterator for list. */
    ListIterator<CardView> revIt = wasteView.getCards().listIterator(wasteView.numOfCards());

    while (revIt.hasPrevious()) {
      /** get current card view. */
      CardView currentCardView = revIt.previous();
      currentCardView.setToFaceDown();
      makeClickable(currentCardView);
      stockView.addCardView(currentCardView);
      revIt.remove();
    }

  };

  /**
   * This event handler is attached to all the cards that are not on the stock.
   * Stores the position where the user clicked.
   */
  EventHandler<MouseEvent> onMousePressedHandler = e -> {
    // Store mouse click position
    mousePos.x = e.getSceneX();
    mousePos.y = e.getSceneY();
  };
	
	/**
   * This event handler is attached to all the cards that are not on the stock.
   * Handles the card movements, applies a drop shadow effect, and others.
   */
  EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
    // Calculate difference vector from clicked point
    /** x component. */
    double offsetX = e.getSceneX() - mousePos.x;
    /** y component. */
    double offsetY = e.getSceneY() - mousePos.y;

    // Get the actual card
    /** get current cardView. */
    CardView cardView = (CardView) e.getSource();
    /** get current card. */
    Card card = game.getCurrentGameState().GetFullDeck().getById(cardView.getShortID());

    // Setup drop shadow
    cardView.getDropShadow().setRadius(20);
    cardView.getDropShadow().setOffsetX(10);
    cardView.getDropShadow().setOffsetY(10);

    // Get the pile that contained the actual card
    /** get current pile view. */
    CardPileView activePileView = cardView.getContainingPile();
    /** get current pile. */
    List<Card> activePile = game.getPileById(activePileView.getShortID());

    // Put this card and all above it to the list of dragged cards
    draggedCardView = cardView;
    draggedCardViewIndex = activePileView.getCardViewIndex(cardView);
    draggedCard = card;
    
    // Handles dragging coordinates
    draggedCardView.toFront();
    draggedCardView.setTranslateX(offsetX);
    draggedCardView.setTranslateY(offsetY);
    
    // Bring them to front & apply difference vector to dragged cards
  /*  draggedCardView.forEach(cw -> {
        cw.toFront();
        cw.setTranslateX(offsetX);
        cw.setTranslateY(offsetY);
      });
*/
  };

  /**
   * This event handler is attached to all the cards that are not on the stock.
   * Decides if the move is valid, and acts appropriately.
   */
  EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
    // if no cards are dragged, return immediately
    if (draggedCard == null && draggedCardView == null)
      return;

    // Get the actual card
    /** get current card view. */
    CardView cardView = (CardView) e.getSource();
    /** get current card. */
    Card card = game.getCurrentGameState().GetFullDeck().getById(cardView.getShortID());

    // Get the pile that contained the actual card
    /** get current pile view. */
    CardPileView activePileView = cardView.getContainingPile();
    /** get current pile. */
    List<Card> activePile = game.getPileById(activePileView.getShortID());
    
    System.out.println("CardView ID: " + cardView.getShortID() + " in pile: " + activePileView.getShortID());
    
    // check if card(s) are intersecting with any of the piles
    
    if (checkAllPiles(card, cardView, activePile, activePileView)) {    	
        draggedCard = null;
	    draggedCardView = null;
	    return;
    }
    
    // if not intersecting with any valid pile, slide them back
    slideBack(draggedCardView);
    
    //reorder CardViews
    for (CardView cv : activePileView){
    	cv.toFront();
    }
    
    // throw away dragged cards info
    draggedCard = null;
    draggedCardView = null;
  };
  

  /**
   * Constructs a {@link InputManager} object for the given
   * {@link CardGame} and {@link GameBoard} objects.
   *
   * @param game     The {@link CardGame object}.
   * @param gameBoard The {@link GameBoard} object.
   */
  public InputManager(IIdiotGameEngine game, GameBoard gameBoard) {
    this.game = game;
    this.gameBoard = gameBoard;
  }

  /**
   * Applies the appropriate event handlers for cards not on the stock.
   *
   * @param card The {@link CardView} to apply event listeners for.
   */
  public void makeDraggable(CardView card) {
    card.setOnMouseClicked(null);
    card.setOnMousePressed(onMousePressedHandler);
    card.setOnMouseDragged(onMouseDraggedHandler);
    card.setOnMouseReleased(onMouseReleasedHandler);
  }
  
  public void removeDraggable(CardView card){
	  card.setOnMouseClicked(null);
	  card.setOnMousePressed(null);
	  card.setOnMouseDragged(null);
	  card.setOnMouseReleased(null);
  }

  /**
   * Applies the appropriate event handlers for cards on the stock.
   *
   * @param card The {@link CardView} to apply event listeners for.
   */
  public void makeClickable(CardView card) {
    card.setOnMousePressed(null);
    card.setOnMouseDragged(null);
    card.setOnMouseReleased(null);
    card.setOnMouseClicked(onMouseClickedHandler);
  }
  
  /**
   * Applies the appropriate event handlers for cards on the stock.
   *
   * @param card The {@link CardView} to apply event listeners for.
   */
  public void removeClickable(CardView card) {
    card.setOnMousePressed(null);
    card.setOnMouseDragged(null);
    card.setOnMouseReleased(null);
    card.setOnMouseClicked(null);
  }

  /**
   * Check if the actual card is intersecting with any of the piles.
   *
   * @param card           The card to check.
   * @param cardView       The view of the card.
   * @param activePile     The pile the card is currently in.
   * @param activePileView The view for the pile.
   * @return true if intersects with any pile, false otherwise.
   */
  private boolean checkAllPiles(
      Card card, CardView cardView, List<Card> activePile,
      CardPileView activePileView) {
	  
	// check player 1 table piles
    if(checkPiles(card, cardView, activePile,
        activePileView, gameBoard.getP1_FoundationPileViews()))
    	return true;
    
    // check player 1 hand pile
    if(checkPiles(card, cardView, activePile,
        activePileView, gameBoard.getP1_HandPileView()))
    	return true;
    
    // check player 2 table piles
    if (checkPiles(card, cardView, activePile,
        activePileView, gameBoard.getP2_FoundationPileViews()))
      return true;
    
    // check player 2 hand pile
    if(checkPiles(card, cardView, activePile,
        activePileView, gameBoard.getP2_HandPileView()))
    	return true;

    // check draw pile
    if (checkPiles(card, cardView, activePile,
        activePileView, gameBoard.getDrawCardsView()))
      return true;
    
    // check pile
    if (checkPiles(card, cardView, activePile,
        activePileView, gameBoard.getPileView()))
      return true;
    
    return false;
    
  }

  /**
   * Check a list of pile views for card intersection.
   *
   * @param card           The card to check.
   * @param cardView       The view for the card.
   * @param activePile     The pile the card is currently in.
   * @param activePileView The view for the pile.
   * @param pileViews      The list of piles to check.
   * @return true if intersects with any pile, false otherwise.
   */
  private boolean checkPiles(
      Card card, CardView cardView, List<Card> activePile,
      CardPileView activePileView, List<CardPileView> pileViews) {

    boolean result = false;

    for (CardPileView pileView : pileViews) {
      // skip checking the same pile
      if (pileView.equals(activePileView))
        continue;

      // check for intersection
//      if (isOverPile(cardView, pileView) &&
//          handleValidMove(card, activePile, activePileView, pileView)){
//          return true;
//      }
      
      if(isOverPile(cardView, pileView)){
    	  System.out.println("Dropped on pile: " + pileView.getShortID());
    	  
    	  if(game.getCurrentGameState().CurrentGamePhase().equals(IdiotGameState.GamePhases.CardSwapping)){
    		  //TODO Validate if pile the card is dropped on is a valid move during the CardSwapping phase
    	  } else if (game.getCurrentGameState().CurrentGamePhase().equals(IdiotGameState.GamePhases.GamePlay)){
    		//TODO Validate if pile the card is dropped on is a valid move during the GamePlay phase
    	  }
      }
    }

    return result;
  }
  
  /**
   * Check a single pile for intersection
   * @param card
   * @param cardView
   * @param activePile
   * @param activePileView
   * @param pileView
   * @return
   */
  private boolean checkPiles(
	      Card card, CardView cardView, List<Card> activePile,
	      CardPileView activePileView, CardPileView pileView) {

	    boolean result = false;

	    // skip checking the same pile
	      if (pileView.equals(activePileView))
	        return result;

	      // check for intersection
//	      if (isOverPile(cardView, pileView) &&
//	          handleValidMove(card, activePile, activePileView, pileView)){
//	          return true;
//	      }
	      
	      if(isOverPile(cardView, pileView)){
	    	  System.out.println("Dropped on pile: " + pileView.getShortID());
	      }
	    
	    return result;
}

  /**
   * Checks if a cardView is over a pile.
   *
   * @param cardView The cardView to check.
   * @param pileView The pile to check.
   * @return true if the card is over the pile, false otherwise.
   */
  private boolean isOverPile(CardView cardView, CardPileView pileView) {
    if (pileView.isEmpty())
      return cardView.getBoundsInParent().intersects(pileView.getBoundsInParent());
    else
      return cardView.getBoundsInParent().intersects(pileView.getTopCardView().getBoundsInParent());
  }

  /**
   * Handles a move. If valid, move the model cards to the destination pile,
   * as well as their views.
   *
   * @param card           The card to move.
   * @param activePile     The view of the moved card.
   * @param sourcePileView The source pile view.
   * @param destPileView   The destination pile view.
   * @return true if the move is valid, false otherwise.
  */
  private boolean handleValidMove(Card card, List<Card> activePile,
                                  CardPileView sourcePileView,
                                  CardPileView destPileView) {
    List<Card> destPile = game.getPileById(destPileView.getShortID());
/**    
    if(!game.isGameInProgress()){
    	Player activePlayer = game.getActivePlayer();
    	
    	if(game.getP1_Foundations().contains(destPile) && sourcePile.equals(game.getP1_HandPile()) 
    			&& activePlayer.equals(game.getPlayer1())){
    		Card cardToSwitch = destPile.getTopCard();
    		CardView cardViewToSwitch = destPileView.getTopCardView();
    		
    		//switch cards in piles
    		game.switchCards(draggedCard, cardToSwitch, sourcePile, destPile);
    		
    		//change draggable status
    		removeDraggable(draggedCardView);
    		makeDraggable(cardViewToSwitch);
    		
    		//slide dragged card to player pile
    		slideToPile(draggedCardView, sourcePileView, destPileView);
    		
    		//slide pile card to hand
    		slideToHand(cardViewToSwitch, destPileView, sourcePileView, 
    				draggedCardView.getLayoutX(), draggedCardView.getLayoutY(), draggedCardViewIndex);    		
    	    
    		
    		return true;
    	} else if(game.getP2_Foundations().contains(destPile) && activePlayer.equals(game.getPlayer2())){
    		
    	}
    	
    	return false;
    	
    }else if (game.getRules().isMoveValid(card, destPile, sourcePile)) {
      game.moveCard(draggedCard, sourcePile, destPile);
      slideToPile(draggedCardView, sourcePileView, destPileView);
      draggedCard = null;
      draggedCardView = null;
      return true;
    } 
    
*/    return false;
 }
 
  /**
   * Slide back card to its original position if the move was not valid.
   *
   * @param card The card view to slide back.
   */
  private void slideBack(CardView card) {
    double sourceX = card.getLayoutX() + card.getTranslateX();
    double sourceY = card.getLayoutY() + card.getTranslateY();

    double targetX = card.getLayoutX();
    double targetY = card.getLayoutY();

    animateCardMovement(card, sourceX, sourceY,
        targetX, targetY, Duration.millis(150), e -> {
          card.getDropShadow().setRadius(2);
          card.getDropShadow().setOffsetX(0);
          card.getDropShadow().setOffsetY(0);
        });
  }

  /**
   * Slides the list of dragged cards from the source pile to the destination
   * pile.
   *
   * @param cardsToSlide The list of dragged cards.
   * @param sourcePile   The source pile.
   * @param destPile     The destination pile.
   */
  private void slideToPile(CardView cardToSlide, CardPileView sourcePile,
                           CardPileView destPile) {
    if (cardToSlide == null)
      return;

    double destCardGap = destPile.getCardGapVertical();

    double targetX;
    double targetY;

    if (destPile.isEmpty()) {
      targetX = destPile.getLayoutX();
      targetY = destPile.getLayoutY();
    } else {
      targetX = destPile.getTopCardView().getLayoutX();
      targetY = destPile.getTopCardView().getLayoutY();
    }

    CardView currentCardView = cardToSlide;
      double sourceX =
          currentCardView.getLayoutX() + currentCardView.getTranslateX();
      double sourceY =
          currentCardView.getLayoutY() + currentCardView.getTranslateY();

      animateCardMovement(currentCardView, sourceX, sourceY, targetX,
          targetY,
          Duration.millis(150),
          e -> {
            sourcePile.moveCardViewToPile(currentCardView, destPile);
            currentCardView.getDropShadow().setRadius(2);
            currentCardView.getDropShadow().setOffsetX(0);
            currentCardView.getDropShadow().setOffsetY(0);
          });
      
  }
  
  /**
   * Slides the list of dragged cards from the source pile to the destination
   * pile.
   *
   * @param cardsToSlide The list of dragged cards.
   * @param sourcePile   The source pile.
   * @param destPile     The destination pile.
   */
  private void slideToHand(CardView cardToSlide, CardPileView sourcePile,
                           CardPileView destPile, double x, double y, int index) {
    if (cardToSlide == null)
      return;
    
    double destHorizontalCardGap = destPile.getcardGapHorizontal();

    double sourceX = cardToSlide.getLayoutX() + cardToSlide.getTranslateX();
    double sourceY = cardToSlide.getLayoutY() + cardToSlide.getTranslateY();
    
    double targetX = x;
    double targetY = y;
    
    animateCardMovement(cardToSlide, sourceX, sourceY,
        targetX, targetY, Duration.millis(150), e -> {
        	sourcePile.replaceCardViewOnPile(cardToSlide, destPile, targetX, targetY, index);
        	cardToSlide.getDropShadow().setRadius(2);
        	cardToSlide.getDropShadow().setOffsetX(0);
        	cardToSlide.getDropShadow().setOffsetY(0);
        });
  }

  /**
   * Animates card movements.
   *
   * @param card     The card view to animate.
   * @param sourceX  Source X coordinate of the card view.
   * @param sourceY  Source Y coordinate of the card view.
   * @param targetX  Destination X coordinate of the card view.
   * @param targetY  Destination Y coordinate of the card view.
   * @param duration The duration of the animation.
   * @param doAfter  The action to perform after the animation has been completed.
   */
  private void animateCardMovement(
      CardView card, double sourceX, double sourceY,
      double targetX, double targetY, Duration duration,
      EventHandler<ActionEvent> doAfter) {

    Path path = new Path();
    path.getElements().add(new MoveToAbs(card, sourceX, sourceY));
    path.getElements().add(new LineToAbs(card, targetX, targetY));

    PathTransition pathTransition =
        new PathTransition(duration, path, card);
    pathTransition.setInterpolator(Interpolator.EASE_IN);
    pathTransition.setOnFinished(doAfter);

    Timeline blurReset = new Timeline();
    KeyValue bx = new KeyValue(card.getDropShadow().offsetXProperty(), 0, Interpolator.EASE_IN);
    KeyValue by = new KeyValue(card.getDropShadow().offsetYProperty(), 0, Interpolator.EASE_IN);
    KeyValue br = new KeyValue(card.getDropShadow().radiusProperty(), 2, Interpolator.EASE_IN);
    KeyFrame bKeyFrame = new KeyFrame(duration, bx, by, br);
    blurReset.getKeyFrames().add(bKeyFrame);

    ParallelTransition pt = new ParallelTransition(card, pathTransition, blurReset);
    pt.play();
  }

  /**
   * Helper class for calculating card positions.
   */
  private static class MoveToAbs extends MoveTo {
    /**
     * Creates a new instance.
     *
     * @param node The {@link Node} object to calculate position for.
     * @param x    The x coordinate.
     * @param y    The y coordinate.
     */
    public MoveToAbs(Node node, double x, double y) {
      super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2,
          y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
    }
  }

  /**
   * Helper class for calculating card positions.
   */
  private static class LineToAbs extends LineTo {
    /**
     * Creates a new instance.
     *
     * @param node The {@link Node} object to calculate position for.
     * @param x    The x coordinate.
     * @param y    The y coordinate.
     */
    public LineToAbs(Node node, double x, double y) {
      super(x - node.getLayoutX() + node.getLayoutBounds().getWidth() / 2,
          y - node.getLayoutY() + node.getLayoutBounds().getHeight() / 2);
    }
  }

  /**
   * Helper class for determining mouse position.
   */
  private static class MousePos {
    /**
     * x and y coordinate.
     */
    double x, y;
  }
  

}
