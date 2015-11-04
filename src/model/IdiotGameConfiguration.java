package model;

import model.card.GameCardRank;

public class IdiotGameConfiguration {

/**
 * Playing card with this rank will be used as the restart card for Idiot game
 */
public GameCardRank restartCard;

/**
 * Playing card with this rank will be used as the burn card for Idiot game
 */
public GameCardRank burnCard;

/**
 * Playing card with this rank will be used as the reverse card for Idiot game
 */
public GameCardRank reverseCard;

/**
 * @param restartCard
 * @param burnCard
 * @param reverseCard
 */
public IdiotGameConfiguration(GameCardRank restartCard, GameCardRank burnCard,
		GameCardRank reverseCard) {
	
	/**Initialize the game configuration variables with constructor.
	 * */
	this.restartCard = restartCard;
	this.burnCard = burnCard;
	this.reverseCard = reverseCard;
}

public IdiotGameConfiguration() {
}

@Override
public String toString(){
	return "Rules: Burn card(" + burnCard.toString() + "), " +
					"Restart card(" + restartCard.toString() + "), " +
					"Reverse card(" + reverseCard.toString() + ")";
}

	
}
