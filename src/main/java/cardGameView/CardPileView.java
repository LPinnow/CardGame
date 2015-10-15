package cardGameView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Visual representation of a pile of cards.
 */
public class CardPileView extends Pane implements Iterable<CardView> {

  /**
   * The {@link List} containing the {@link CardView} objects,
   * which are lying on this pile.
   */
  private ObservableList<CardView> cards = FXCollections.observableArrayList();

  /**
   * Vertical gap to lay out the cards.
   */
  private double cardGapVertical;

  /**
   * Vertical gap to lay out the cards.
   */
  private double cardGapHorizontal;
  
  /**
   * Layout of initial X coordinate
   */
  private double initialX;
  
  /**
   * Layout of initial Y coordinate
   */
  private double initialY;
  
  /**
   * The identifier of the pile.
   */
  private String shortID;

  /**
   * Constructs a {@link CardPileView} object, with the given gap.
   *
   * @param cardGap The vertical gap to lay out the cards.
   */
  public CardPileView(double cardGapVertical, double cardGapHorizontal, double initialX, double initialY) {
    this.cardGapVertical = cardGapVertical;
    this.cardGapHorizontal = cardGapHorizontal;
    this.setInitialX(initialX);
    this.setInitialY(initialY);
  }

  /**
   * Constructs a {@link CardPileView} object, with the given gap
   * and identifier.
   *
   * @param cardGap The vertical gap to lay out the cards.
   * @param shortID The short identifier of the pile.
   */
  public CardPileView(double cardGapVertical, double cardGapHorizontal, 
		  double initialX, double initialY, String shortID) {
    this.cardGapVertical = cardGapVertical;
    this.cardGapHorizontal = cardGapHorizontal;
    this.setInitialX(initialX);
    this.setInitialY(initialY);
    this.shortID = shortID;
  }

  /**
   * Returns the vertical gap.
   *
   * @return The vertical gap.
   */
  public double getCardGapVertical() {
    return cardGapVertical;
  }

  /**
   * Sets the vertical gap.
   *
   * @param cardGap The new value of the gap.
   */
  public void setCardGapVertical(double cardGapVertical) {
    this.cardGapVertical = cardGapVertical;
  }
  
  /**
   * Returns the horizontal gap.
   *
   * @return The horizontal gap.
   */
  public double getcardGapHorizontal() {
    return cardGapHorizontal;
  }

  /**
   * Sets the horizontal gap.
   *
   * @param cardGap The new value of the gap.
   */
  public void setcardGapHorizontal(double cardGapHorizontal) {
    this.cardGapVertical = cardGapHorizontal;
  }

  /**
   * Returns the short identifier.
   *
   * @return The short identifier.
   */
  public String getShortID() {
    return shortID;
  }

  /**
   * Sets the short identifier.
   *
   * @param shortID The new short identifier to be set.
   */
  public void setShortID(String shortID) {
    this.shortID = shortID;
  }

  /**
   * Returns the {@link List} of cards.
   *
   * @return The {@link List} of cards.
   */
  public ObservableList<CardView> getCards() {
    return cards;
  }

  /**
   * Returns the number of cards on this pile.
   *
   * @return The number of cards on this pile.
   */
  public int numOfCards() {
    return cards.size();
  }

  /**
   * Adds a {@link CardView} to this pile.
   *	Also positions the card. 
   *
   * @param cardView The {@link CardView} to be added.
   */
  public void addCardView(CardView cardView) {
    cards.add(cardView);
    cardView.setContainingPile(this);
    cardView.toFront();
    layoutCard(cardView);
  }

  /**
   * Lays out the {@link CardView} object to sit nicely in this pile.
   *
   * @param cardView The {@link CardView} object to lay out.
   */
  private void layoutCard(CardView cardView) {
    cardView.relocate(cardView.getLayoutX() + cardView.getTranslateX(),
        cardView.getLayoutY() + cardView.getTranslateY());

    cardView.setTranslateX(0);
    cardView.setTranslateY(0);
    cardView.setLayoutX(getLayoutX() + (cards.size() - 1) * cardGapHorizontal);
    cardView.setLayoutY(getLayoutY() + (cards.size() - 1) * cardGapVertical);
  }

  /**
   * Returns whether the pile is empty.
   *
   * @return <code>true</code> if the pile is empty, <code>false</code> otherwise.
   */
  public boolean isEmpty() {
    return cards.isEmpty();
  }
  
  public void emptyCardViewPile(){
	 cards.clear();
  }

  /**
   * Gets the card which lies on top of the pile.
   *
   * @return The card on top of the pile.
   */
  public CardView getTopCardView() {
    return cards.get(cards.size() - 1);
  }

  /**
   * Returns the card passed in the parameter and all the cards above in a
   * <code>List</code>.
   *
   * @param cardView The starting card of the <code>List</code>.
   * @return The <code>List</code> containing the card passed as parameter
   * and all cards above it in the pile.
   */
  public List<CardView> cardViewsAbove(CardView cardView) {
    return cards.subList(cards.indexOf(cardView), cards.size());
  }

  public List<CardView> cardViewsBelow(CardView cardView) {
	    return cards.subList(0 , cards.indexOf(cardView));
  }
  
  public int getCardViewIndex(CardView cardView){
	  return cards.indexOf(cardView);
  }
  
  public CardView getCardView(int index){
	  return cards.get(index);
  }
  
  /**
   * Moves cards to another pile. Intended to be used in conjunction with
   * <code>cardViewsAbove()</code>.
   *
   * @param cardsToMove The list of cards to move to the destination pile.
   * @param destPile    The destination pile.
   */
  public void moveCardViewsToPile(List<CardView> cardsToMove, CardPileView destPile) {
    cardsToMove.forEach(destPile::addCardView);
    cardsToMove.clear();
  }

  /**
   * Moves a single card to another pile.
   *
   * @param cardToMove The card to move to the destination pile.
   * @param destPile   The destination pile.
   */
  public void moveCardViewToPile(CardView cardToMove, CardPileView destPile) {
    destPile.addCardView(cardToMove);
    cards.remove(cardToMove);
  }
  
  public void replaceCardViewOnPile(CardView cardToMove, CardPileView destPile, double x, double y, int index){
	  destPile.replaceCardView(cardToMove, x, y,index);
	  cards.remove(cardToMove);
  }
  
  public void replaceCardView(CardView cardView, double x, double y, int index){
	    cards.add(index, cardView);
	    cardView.setContainingPile(this);
	    cardView.toFront();
	    layoutReplaceCard(cardView, x, y);
	  }

	  /**
	   * Lays out the {@link CardView} object to sit nicely in this pile.
	   *
	   * @param cardView The {@link CardView} object to lay out.
	   */
	  private void layoutReplaceCard(CardView cardView, double x, double y) {
	    
		  cardView.relocate(x,y);

	    cardView.setTranslateX(0);
	    cardView.setTranslateY(0);
	    cardView.setLayoutX(x);
	    cardView.setLayoutY(y);
	    
	  }
  
  public void removeCardViewFromPile(CardView cardToRemove){
	  cards.remove(cardToRemove);
  }
  
  public void addCardViewToPile(CardView cardToAdd, CardPileView destPile) {
	cards.add(cardToAdd);
	cardToAdd.setContainingPile(destPile);
  }
  
  /**
   * Returns an iterator for iterating through the cards.
   *
   * @return The iterator.
   */
  public Iterator<CardView> iterator() {
    return cards.iterator();
  }

  /**
   * Performs the given action for each of the cards in this pile.
   *
   * @param action The action to perform.
   */
  @Override
  public void forEach(Consumer<? super CardView> action) {
    cards.forEach(action);
  }

public double getInitialY() {
	return initialY;
}

public void setInitialY(double initialY) {
	this.initialY = initialY;
}

public double getInitialX() {
	return initialX;
}

public void setInitialX(double initialX) {
	this.initialX = initialX;
}



}
