package model;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class PlayerZone {

	/*Player number represents the Id associated to the player.
	 * */	
	public int playerNumber;
	public List<Card> hand;
	public List<Card> tableCards1;
	public List<Card> tableCards2;
	public List<Card> tableCards3;
	
	public String idTableCards1;
	public String idTableCards2;
	public String idTableCards3;
	public String idHand;
	
	public PlayerZone(int playerNumber) {
		this.playerNumber = playerNumber;
		hand = new ArrayList<Card>();
		tableCards1= new ArrayList<Card>();
		tableCards2= new ArrayList<Card>();
		tableCards3= new ArrayList<Card>();
	}
	
	/**
	 * @param playerNumber
	 * @param hand
	 * @param tableCards1
	 * @param tableCards2
	 * @param tableCards3
	 */
	
	public PlayerZone(int playerNumber, List<Card> hand, List<Card> tableCards1,
			List<Card> tableCards2, List<Card> tableCards3) {
		this.playerNumber = playerNumber;
		this.hand = hand;
		this.tableCards1 = tableCards1;
		this.tableCards2 = tableCards2;
		this.tableCards3 = tableCards3;
	}
	
	/***
	 * Returns which table card stack has a face up value matching the card passed.  If none do then 
	 * zero is returned.
	 * 
	 * @param card
	 * @return
	 */
	
	public int getMatchingFaceUpTableCard(Card card) {

		if (tableCards1.size() > 0 && tableCards1.get(tableCards1.size()-1).equals(card)) return 1;
		if (tableCards2.size() > 0 && tableCards2.get(tableCards2.size()-1).equals(card)) return 2;
		if (tableCards3.size() > 0 && tableCards3.get(tableCards3.size()-1).equals(card)) return 3;
		
		return 0;
	}
	
	/**
	 * Sets the ids of the piles in the zone.
	 * Ids must match corresponding CardPileView ids.
	 */
	public void initializePileIDs(){
		this.idHand = "p" + this.playerNumber + "Hand";
		this.idTableCards1 = "p" + this.playerNumber + "Foundation1";
		this.idTableCards2 = "p" + this.playerNumber + "Foundation2";
		this.idTableCards3 = "p" + this.playerNumber + "Foundation3";
	}
}
