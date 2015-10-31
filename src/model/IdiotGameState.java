package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class IdiotGameState {
	
	/**
	 * All possible states of idiot game.
	 */
	public enum GamePhases{Uninitialized,ResettingGame,CardSwapping,GamePlay,GameCompleted}
	
	public IdiotGameState() {} // use for unit testing
	
	public IdiotGameState(int numberOfPlayers) {
		
		//TODO: Validate numberOfPlayers passed is within valid bounds
		
		PlayerPlaces = new ArrayList<PlayerZone>(numberOfPlayers);
		IntStream.range(1, numberOfPlayers+1).forEach(idx -> PlayerPlaces.add(new PlayerZone(idx)));
	}

	/**
	 * Current phase of the game
	 */
	public GamePhases CurrentGamePhase = GamePhases.Uninitialized;
	
	/**
	 *Indicates the index of the current player's turn.
	 */
	public int currentPlayerTurn = 1;
	
	/**
	 *Contains the pile of all the discarded cards 
	 */
	
	public List<Card> discardedCards = new ArrayList<Card>();
	public List<Card> drawCards = new ArrayList<Card>();
	public List<Card> pile = new ArrayList<Card>();
	
	/**
	 * The collections of cards settings for each player
	 */
	
	public List<PlayerZone> PlayerPlaces = new ArrayList<PlayerZone>();
	
	@Override 
	public String toString() { // Useful for debugging
		StringBuffer buf = new StringBuffer();
		buf.append("Deck: [");
		cardListToString(drawCards, buf);
		buf.append("]\r\nDiscardedCards: [");
		cardListToString(discardedCards, buf);
		buf.append("]\r\nPile: [");
		cardListToString(pile, buf);
		buf.append("]\r\n");
		
		for (PlayerZone player : PlayerPlaces) {
			
			buf.append("\r\nPlayer : " + player.playerNumber);
			buf.append("\r\nHand: [" );
			cardListToString(player.hand, buf);
			buf.append("]\r\nTableCards: [" );
			cardListToString(player.tableCards1, buf);
			buf.append("] [");
			cardListToString(player.tableCards2, buf);
			buf.append("] [");
			cardListToString(player.tableCards3, buf);
			buf.append("]\r\n");
		}
		
		return buf.toString();
	}
	
	private void cardListToString(List<Card> cardList, StringBuffer buf) {
		IntStream.range(0, cardList.size()).forEach(i -> buf.append(cardList.get(i).getId() + ((i == cardList.size()-1)?"":",")));
	}

}

