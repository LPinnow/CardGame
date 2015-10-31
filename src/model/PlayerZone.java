package model;

import java.util.ArrayList;
import java.util.List;

public class PlayerZone {

/*Player number represents the Id associated to the player.
 * */	
public int playerNumber;
public List<Card> hand;
public List<Card> tableCards1;
public List<Card> tableCards2;
public List<Card> tableCards3;

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




}
