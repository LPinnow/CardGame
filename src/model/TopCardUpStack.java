package model;

public class TopCardUpStack extends CardCollectionImmutable {

	/**
	 *Stack with the topcard facing up. 
	 **/
	final protected Card topCard;
	
	public TopCardUpStack(Card topCard, int sizeIncludingTopCard) {
		super(sizeIncludingTopCard);
		this.topCard = topCard;
	}
	
	public Card getTopCard() 
	{
		return (Card)topCard.clone();
	}
}
