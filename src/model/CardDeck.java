package model;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Class representing a deck of French playing cards.
 */
public class CardDeck extends CardCollection {

  /**
   * Creates a standard 52-card deck of French playing cards.
   *
   * @return The GameCardDeck object containing the 52 cards.
   */
  public static CardDeck createGameCardDeck() {

    CardDeck result = new CardDeck();

    for (GameCardSuit suit : GameCardSuit.values()) {
      for (GameCardRank rank : GameCardRank.values()) {
        result.addCard(new GameCard(true, suit, rank));
      }
    }

    return result;
  }

  /**
   * Returns an iterator to iterate through the cards.
   *
   * @return The iterator.
   */
  @Override
  public Iterator<Card> iterator() {
    return cards.iterator();
  }

  /**
   * Performs the given action on all the cards.
   *
   * @param action the specified action.
   */
  @Override
  public void forEach(Consumer<? super Card> action) {
    cards.forEach(action);
  }

  
}
