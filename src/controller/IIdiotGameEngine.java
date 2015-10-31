package controller;

import model.*;

public interface IIdiotGameEngine {

	/**
	 * Invoked to request a new game be started, shuffle deck, and distribute cards. 
	 */
	
	void initializeNewGame(int numberOfPlayers);
	
	/**
	 * Before play begins, called to request a swap of a hand card with a table card
	 */
	
	MoveResult requestHandToTableCardSwap(Player playerRequesting, Card handCard, Card tableCard);
	
	/**
	 * Invoked by the UI when both players have finished swapping hand cards with table cards and are
	 * ready to start the game
	 */
	
	void beginPlay(); 
	
	/**
	 * Once play has started this is used to submit moves from each player
	 * @param move
	 * @param playerRequesting
	 * @return the result of the move request
	 */
	
	MoveResult submitMove(Player playerRequesting, Move move);
	
}