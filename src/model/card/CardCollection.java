package model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class representing a pile of cards.
 */
public class CardCollection implements Iterable<Card> {

  /**
   * List holding the cards on this pile.
   */
  protected List<Card> cards = new ArrayList<>();

  /**
   * The type of the pile.
   */
  private Type type;

  /**
   * Identifier of the pile.
   */
  private String id;

  /**
   * Constructs an empty pile.
   */
  public CardCollection() {
  }

  /**
   * Constructs a pile with the given type.
   *
   * @param type The type.
   */
  public CardCollection(Type type) {
    this.type = type;
  }

  /**
   * Constructs a pile with the given type and identifier.
   *
   * @param type The type.
   * @param id   The identifier.
   */
  public CardCollection(Type type, String id) {
    this.type = type;
    this.id = id;
  }

  /**
   * Returns the list of cards.
   *
   * @return The list of cards.
   */
  public List<Card> getCards() {
    return cards;
  }

  /**
   * Returns the number of cards.
   *
   * @return The number of cards.
   */
  public int numOfCards() {
    return cards.size();
  }

  /**
   * Returns the current type of the pile.
   *
   * @return The type of the pile.
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the type of the pile.
   *
   * @param type The type to be set.
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * Returns the pile identifier.
   *
   * @return The pile identifier.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the pile identifier.
   *
   * @param id The new identifier to be set.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Adds a card to the pile.
   *
   * @param card The card to be added to the pile.
   */
  public void addCard(Card card) {
    cards.add(card);
  }

  /**
   * Returns whether the pile is empty.
   *
   * @return <code>true</code> if the pile is empty, <code>false</code> otherwise.
   */
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  /**
   * Gets the card which lies on top of the pile.
   *
   * @return The card on top of the pile.
   */
  public Card getTopCard() {
    return cards.get(cards.size() - 1);
  }

  /**
   * Returns the card passed in the parameter and all the cards above in a
   * <code>List</code>.
   *
   * @param card The starting card of the <code>List</code>.
   * @return The <code>List</code> containing the card passed as parameter
   * and all cards above it in the pile.
   */
  public List<Card> cardsAbove(Card card) {
    return cards.subList(cards.indexOf(card), cards.size());
  }

  /**
   * Moves a single card to another pile.
   *
   * @param cardToMove The card to move to the destination pile.
   * @param destPile   The destination pile.
   */
  public void moveCardToPile(Card cardToMove, CardCollection destPile) {
    destPile.addCard(cardToMove);
    cards.remove(cardToMove);
  }

  /**
   * Moves cards to another pile. Intended to be used in conjunction with
   * <code>cardsAbove()</code>.
   *
   * @param cardsToMove The list of cards to move to the destination pile.
   * @param destPile    The destination pile.
   */
  public void moveCardsToPile(List<Card> cardsToMove, CardCollection destPile) {
    cardsToMove.forEach(destPile::addCard);
    cardsToMove.clear();
  }

  /**
   * Returns an iterator for iterating through the cards.
   *
   * @return The iterator.
   */
  @Override
  public Iterator<Card> iterator() {
    return cards.iterator();
  }

  /**
   * Shuffles the deck.
   */
  public void shuffle() {
    // Research proved that if we want a deck of cards to be well shuffled,
    // we have to do it about seven times.
    for (int i = 0; i < 7; i++) {
      Collections.shuffle(cards);
    }
  }

  /**
   * Finds the card in the deck by its short identifier.
   *
   * @param id The short identifier.
   * @return The card object if found, null otherwise.
   */
  public Card getById(String id) {
    return cards.stream()
        .filter(card -> card.getId().equals(id)).findFirst().orElseGet(() -> null);
  }
  
  /**
   * Performs the given action for each of the cards in this pile.
   *
   * @param action The action to perform.
   */
  @Override
  public void forEach(Consumer<? super Card> action) {
    cards.forEach(action);
  }

  /**
   * Enumeration that holds the possible types of the pile.
   */
  public enum Type {
    /**
     * Standard hand pile for card games.
     */
    HAND,
    
    /**
     * Remaining cards from the deck.
     */
    DECK,
    
    /**
     * Cards put there from the stock.
     */
    WASTE,
    
    /**
     * Extra piles of cards in play.
     */
    PILE
  }

}
