package model;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand extends CardCollectionImmutable {

	/**
	 *list of cards in the face up card stack. 
	 */
	final protected List<Card> cards;
	
	public PlayerHand(List<Card> cards) {
		super(cards.size());
		this.cards = cards;
	}
	
	public List<Card> getCards() 
	{
		return new ArrayList<Card>(cards); // return a clone of the list so we don't pass a direct, modifiable reference
	}
}
