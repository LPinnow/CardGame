package controller;

import java.util.List;

import model.Card;
import model.CardPile;
import model.GameCardRank;
import model.GameCardSuit;

/**
 * Class representing the rules of the card game.
 */
public class CardGameRules {

  /**
   * Reference to the list of the standard card piles.
   */
  private CardPile p1_handPile;
  
  /**
   * Reference to the list of the standard card piles.
   */
  private CardPile p2_handPile;
  
  /**
   * Reference to the list of the foundation piles.
   */
  private List<CardPile> p1_foundations;
  
  /**
   * Reference to the list of the foundation piles.
   */
  private List<CardPile> p2_foundations;

  /**
   * Reference to the stock pile.
   */
  private CardPile stock;

  /**
   * Reference to the waste pile.
   */
  private CardPile waste;
  
  /**
   * Number of cards initially dealt to player's hands
   */
  private int handCardNum;
  
  /**
   * Number of cards initially dealt to extra piles (not deck or hand)
   */
  private int pileCardNum;
  
  /**
   * Creates an empty {@link CardGameRules} object.
   */
  public CardGameRules() {
  }

  /**
   * Creates a {@link CardGameRules} object with the given pile references.
   *
   * @param standardPiles Reference to the list of standard card piles.
   * @param foundations   Reference to the list of foundation piles.
   * @param waste         Reference to the waste pile.
   * @param stock         Reference to the stock pile.
   * @param handCardNum 	Reference to number of cards dealt to player's hand.
   * @param pileCardNum		Reference to number of cards dealt to piles.
   */
  public CardGameRules(CardPile p1_handPile, 
		  				CardPile p2_handPile,
		  				List<CardPile> p1_foundations,
		  				List<CardPile> p2_foundations,
		  				CardPile waste, CardPile stock, 
		  				int handCardNum, int pileCardNum) {
    this.p1_handPile = p1_handPile;
    this.p2_handPile = p2_handPile;
    this.p1_foundations = p1_foundations;
    this.p2_foundations = p2_foundations;
    this.waste = waste;
    this.stock = stock;
    this.handCardNum = handCardNum;
    this.pileCardNum = pileCardNum;
  }

  /**
   * Returns the list of standard piles.
   *
   * @return The list of standard piles.
   */
  public CardPile getP1_HandPile() {
    return p1_handPile;
  }
  
  /**
   * Returns the list of standard piles.
   *
   * @return The list of standard piles.
   */
  public CardPile getP2_HandPile() {
    return p2_handPile;
  }
  
  /**
   * Sets the reference to the list of standard piles.
   *
   * @param standardPiles The new reference to the list of standard piles.
   */
  public void setP1_HandPile(CardPile p1_handPile) {
    this.p1_handPile = p1_handPile;
  }
  
  /**
   * Sets the reference to the list of standard piles.
   *
   * @param standardPiles The new reference to the list of standard piles.
   */
  public void setP2_HandPile(CardPile p2_handPile) {
    this.p2_handPile = p2_handPile;
  }
  
  /**
   * Returns the list of player 1 foundation piles.
   *
   * @return The list of player 1 foundation piles.
   */
  public List<CardPile> getP1_Foundations() {
    return p1_foundations;
  }
  
  /**
   * Returns the list of player 2 foundation piles.
   *
   * @return The list of player 2 foundation piles.
   */
  public List<CardPile> getP2_Foundations() {
    return p2_foundations;
  }

  /**
   * Sets the reference to the list of player 1 foundation piles.
   *
   * @param foundations The new reference to the list of player 1 foundation piles.
   */
  public void setP1_Foundations(List<CardPile> p1_foundations) {
    this.p1_foundations = p1_foundations;
  }
  
  /**
   * Sets the reference to the list of player 2 foundation piles.
   *
   * @param foundations The new reference to the list of player 2 foundation piles.
   */
  public void setP2_Foundations(List<CardPile> p2_foundations) {
    this.p2_foundations = p2_foundations;
  }

  /**
   * Returns the stock pile.
   *
   * @return The stock pile.
   */
  public CardPile getStock() {
    return stock;
  }

  /**
   * Sets reference to the stock pile.
   *
   * @param stock The new reference to the stock pile.
   */
  public void setStock(CardPile stock) {
    this.stock = stock;
  }

  /**
   * Returns the waste pile.
   *
   * @return The waste pile.
   */
  public CardPile getWaste() {
    return waste;
  }

  /**
   * Sets reference to the waste pile.
   *
   * @param waste The new reference to the waste pile.
   */
  public void setWaste(CardPile waste) {
    this.waste = waste;
  }

  /**
   * Returns the pile which currently contains the given {@link Card} object.
   *
   * @param card The {@link Card} object to check.
   * @return The {@link CardPile} which contains the card.
   */
  public CardPile lookForPile(Card card) {
	if (p1_handPile.getCards().contains(card)) {
	      return p1_handPile;
	}
	
	if (p2_handPile.getCards().contains(card)) {
	      return p2_handPile;
	}
	
    for (CardPile foundation : p2_foundations) {
      if (foundation.getCards().contains(card)) {
        return foundation;
      }
    }

    if (stock.getCards().contains(card)) {
      return stock;
    }

    if (waste.getCards().contains(card)) {
      return waste;
    }

    return null;
  }

  /**
   * Checks if two cards are of opposite color.
   *
   * @param card1 The first card.
   * @param card2 The second card.
   * @return <code>true</code> if the colors of the two cards are opposite,
   * <code>false</code> otherwise.
   */
  public boolean isOppositeColor(Card card1, Card card2) {
    GameCardSuit thisSuit = (GameCardSuit) card1.getSuit();
    GameCardSuit otherSuit = (GameCardSuit) card2.getSuit();

    switch (thisSuit) {
      case Spades:
        if (otherSuit == GameCardSuit.Hearts || otherSuit == GameCardSuit.Diamonds)
          return true;
        break;

      case Clubs:
        if (otherSuit == GameCardSuit.Hearts || otherSuit == GameCardSuit.Diamonds)
          return true;
        break;

      case Hearts:
        if (otherSuit == GameCardSuit.Clubs || otherSuit == GameCardSuit.Spades)
          return true;
        break;

      case Diamonds:
        if (otherSuit == GameCardSuit.Clubs || otherSuit == GameCardSuit.Spades)
          return true;
        break;
    }

    return false;
  }

  /**
   * Checks if two cards are in the same suit.
   *
   * @param card1 The first card.
   * @param card2 The second card.
   * @return <code>true</code> if the two cards are in the same suit,
   * <code>false</code> otherwise.
   */
  public boolean isSameSuit(Card card1, Card card2) {
    return card1.getSuit() == card2.getSuit();
  }

  /**
   * Checks if the card passed as the first parameter is smaller by one rank
   * than the card passed as the second parameter.
   *
   * @param card1 The first card.
   * @param card2 The second card.
   * @return <code>true</code> if the first card is smaller by one rank than
   * the second card, <code>false</code> otherwise.
   */
  public boolean isSmallerByOne(Card card1, Card card2) {
    return ((GameCardRank) card1.getRank())
        .compareTo((GameCardRank) card2.getRank()) == -1;
  }

  /**
   * Checks if the card passed as the first parameter is larger by one rank
   * than the card passed as the second parameter.
   *
   * @param card1 The first card.
   * @param card2 The second card.
   * @return <code>true</code> if the first card is larger by one rank than
   * the second card, <code>false</code> otherwise.
   */
  public boolean isLargerByOne(Card card1, Card card2) {
    return ((GameCardRank) card1.getRank())
        .compareTo((GameCardRank) card2.getRank()) == 1;
  }

  /**
   * Checks if the card passed as the first parameter is smaller by one rank
   * than the card passed as the second parameter and is opposite color.
   *
   * @param card1 The first card.
   * @param card2 The second card.
   * @return <code>true</code> if the first card is smaller by one rank than
   * the second card and is opposite color, <code>false</code> otherwise.
   */
  public boolean isSmallerByOneAndOppositeColor(Card card1, Card card2) {
    return isSmallerByOne(card1, card2) && isOppositeColor(card1, card2);
  }

  /**
   * Checks if the card passed as the first parameter is smaller by one rank
   * than the card passed as the second parameter and is in the same suit.
   *
   * @param card1 The first card.
   * @param card2 The second card.
   * @return <code>true</code> if the first card is smaller by one rank than
   * the second card and is in the same suit, <code>false</code> otherwise.
   */
  public boolean isLargerByOneAndSameSuit(Card card1, Card card2) {
    return isLargerByOne(card1, card2) && isSameSuit(card1, card2);
  }

  /**
   * Checks if moving the current card to the destination pile is valid.
   *
   * @param card     The card to check.
   * @param destPile The destination pile.
   * @return <code>true</code> if the move is valid,
   * <code>false</code> otherwise.
   */
  public boolean isMoveValid(Card card, CardPile destPile, CardPile sourcePile) {
	 
	  
    if (destPile.getType() == CardPile.Type.HAND) {
      if (destPile.isEmpty())
        return card.getRank() == GameCardRank.King;
      else
        return isSmallerByOneAndOppositeColor(card, destPile.getTopCard()) &&
            !destPile.getTopCard().isFaceDown() &&
            destPile.getTopCard().getRank() != GameCardRank.Two;
    }

    if (destPile.getType() == CardPile.Type.PILE) {
      if (destPile.isEmpty())
        return card.getRank() == GameCardRank.Ace;
      else
        return isLargerByOneAndSameSuit(card, destPile.getTopCard());
    }

    return false;
  }

public int getHandCardNum() {
	return handCardNum;
}

public int getPileCardNum() {
	return pileCardNum;
}

}