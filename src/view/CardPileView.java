package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.layout.Pane;

/**
 * Visual representation of a pile of cards.
 * 
 * This class has code based upon the following project: Zoltan Dalmadi,
 * "JCardGamesFX", 2015, GitHub repository,
 * github.com/ZoltanDalmadi/JCardGamesFX.
 */
public class CardPileView extends Pane implements Iterable<CardView> {

	/**
	 * The {@link List} containing the {@link CardView} objects, which are lying
	 * on this pile.
	 */
	// private ObservableList<CardView> cards =
	// FXCollections.observableArrayList();
	private List<CardView> cards = new ArrayList<CardView>();

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
	 * @param cardGap
	 *            The vertical gap to lay out the cards.
	 */
	public CardPileView(double cardGapVertical, double cardGapHorizontal,
			double initialX, double initialY) {
		this.cardGapVertical = cardGapVertical;
		this.cardGapHorizontal = cardGapHorizontal;
		this.setInitialX(initialX);
		this.setInitialY(initialY);
	}

	/**
	 * Constructs a {@link CardPileView} object, with the given gap and
	 * identifier.
	 *
	 * @param cardGap
	 *            The vertical gap to lay out the cards.
	 * @param shortID
	 *            The short identifier of the pile.
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
	 * @param cardGap
	 *            The new value of the gap.
	 */
	public void setCardGapVertical(double cardGapVertical) {
		this.cardGapVertical = cardGapVertical;
	}

	/**
	 * Returns the horizontal gap.
	 *
	 * @return The horizontal gap.
	 */
	public double getCardGapHorizontal() {
		return cardGapHorizontal;
	}

	/**
	 * Sets the horizontal gap.
	 *
	 * @param cardGap
	 *            The new value of the gap.
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
	 * @param shortID
	 *            The new short identifier to be set.
	 */
	public void setShortID(String shortID) {
		this.shortID = shortID;
	}

	/**
	 * Returns the {@link List} of cards.
	 *
	 * @return The {@link List} of cards.
	 */
	public List<CardView> getCards() {
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
	 * Adds a {@link CardView} to this pile. Also positions the card.
	 *
	 * @param cardView
	 *            The {@link CardView} to be added.
	 */
	public void addCardView(CardView cardView) {
		cards.add(cardView);
		cardView.setContainingPile(this);
		cardView.toFront();
//		restackCards();
	}
	
	public void moveToEnd(CardView cardView) {
		cards.remove(cardView);
		cards.add(cardView);

	}

	
	public void restackCards() {
		if(cards.size() > 10) {
			cardGapHorizontal /= 2;
			cardGapVertical /= 2;
		}
		for(int i = 0; i < cards.size(); i++) {
			CardView tempView = cards.get(i);
			tempView.relocate(cards.get(i).getLayoutX() + tempView.getTranslateX(),
					tempView.getLayoutY() + tempView.getTranslateY());
			tempView.setTranslateX(0);
			tempView.setTranslateY(0);
			tempView.setLayoutX(getLayoutX() + (i
					* cardGapHorizontal));
			tempView.setLayoutY(getLayoutY() + (i * cardGapVertical));
		}
		
		if(cards.size() > 10) {
			cardGapHorizontal *= 2;
			cardGapVertical *= 2;
		}
		
	}

	/**
	 * Returns whether the pile is empty.
	 *
	 * @return <code>true</code> if the pile is empty, <code>false</code>
	 *         otherwise.
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public void emptyCardViewPile() {
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
	 * @param cardView
	 *            The starting card of the <code>List</code>.
	 * @return The <code>List</code> containing the card passed as parameter and
	 *         all cards above it in the pile.
	 */
	public List<CardView> cardViewsAbove(CardView cardView) {
		return cards.subList(cards.indexOf(cardView), cards.size());
	}

	public List<CardView> cardViewsBelow(CardView cardView) {
		return cards.subList(0, cards.indexOf(cardView));
	}

	public int getCardViewIndex(CardView cardView) {
		return cards.indexOf(cardView);
	}

	public CardView getCardView(int index) {
		return cards.get(index);
	}

	/**
	 * Moves cards to another pile. Intended to be used in conjunction with
	 * <code>cardViewsAbove()</code>.
	 *
	 * @param cardsToMove
	 *            The list of cards to move to the destination pile.
	 * @param destPile
	 *            The destination pile.
	 */
	public void moveCardViewsToPile(List<CardView> cardsToMove,
			CardPileView destPile) {
		cardsToMove.forEach(destPile::addCardView);
		cardsToMove.clear();
	}

	public void clearContents() {
		cards.clear();
	}
	

	/**
	 * Moves a single card to another pile.
	 *
	 * @param cardToMove
	 *            The card to move to the destination pile.
	 * @param destPile
	 *            The destination pile.
	 */

	public void moveCardViewToPile(CardView cardToMove, CardPileView destPile) {
		System.out.println("Adding " + cardToMove.getShortID() + " to " + destPile.getShortID() + " from " + getShortID());
		destPile.addCardView(cardToMove);
		cards.remove(cardToMove);
//		restackCards();
	}
	
	public void moveCardViewToPile(CardView cardToMove, CardPileView destPile, boolean toRestack) {
		System.out.println("Adding " + cardToMove.getShortID() + " to " + destPile.getShortID() + " from " + getShortID());
		destPile.addCardView(cardToMove);
		cards.remove(cardToMove);
		if(toRestack) {
//			restackCards();
		}
	}

	public void replaceCardViewOnPile(CardView cardToMove,
			CardPileView destPile, double x, double y, int index, boolean replaceAtIndex) {
		destPile.replaceCardView(cardToMove, x, y, index, replaceAtIndex);
		cards.remove(cardToMove);
	}

	public void replaceCardView(CardView cardView, double x, double y, int index, boolean replaceAtIndex) {
		if(replaceAtIndex) {
			cards.add(index, cardView);
			layoutReplaceCard(cardView, x, y);
		} else {
			cards.add(cardView);
//			restackCards();
		}
		cardView.setContainingPile(this);
		cardView.toFront();
		
	}

	/**
	 * Lays out the {@link CardView} object to sit nicely in this pile.
	 *
	 * @param cardView
	 *            The {@link CardView} object to lay out.
	 */
	private void layoutReplaceCard(CardView cardView, double x, double y) {
		cardView.relocate(x, y);

		cardView.setTranslateX(0);
		cardView.setTranslateY(0);
		cardView.setLayoutX(x);
		cardView.setLayoutY(y);

	}

	public void removeCardViewFromPile(CardView cardToRemove) {
		cards.remove(cardToRemove);
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
	 * @param action
	 *            The action to perform.
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
