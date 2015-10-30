package model;

import java.util.List;

public class IdiotGameState {

	/**
	 * All possible states of idiot game.
	 */
	public enum GamePhases{ready,running,complete}
	
	/**
	 * Current phase of the game
	 */
	public GamePhases currentGamePhase;
	
	/**
	 *Indicates whether it is the current player turn.
	 */
	public int currentPlayerTurn;
	
	/**
	 *Contains the pile of all the discarded cards 
	 */
	public List<Card> discardedCards;
	public List<Card> drawCards;
	public List<Card> pile;
}

