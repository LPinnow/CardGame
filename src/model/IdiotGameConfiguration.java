package model;

import model.Card.Ranks;

public class IdiotGameConfiguration {

/**
 * Playing card with this rank will be used as the restart card for Idiot game
 */
public Ranks restartCard;

/**
 * Playing card with this rank will be used as the burn card for Idiot game
 */
public Ranks burnCard;

/**
 * Playing card with this rank will be used as the reverse card for Idiot game
 */
public Ranks reverseCard;

/**
 * @param restartCard
 * @param burnCard
 * @param reverseCard
 */
public IdiotGameConfiguration(Ranks restartCard, Ranks burnCard,
		Ranks reverseCard) {
	
	/**Initialize the game configuration variables with constructor.
	 * */
	this.restartCard = restartCard;
	this.burnCard = burnCard;
	this.reverseCard = reverseCard;
}


	
}
