package controller;

import java.util.List;

import model.*;
import model.card.Card;
import model.move.Move;
import model.move.MoveResult;

public interface IIdiotGameEngine {

	/***
	 * Returns an immutable copy of the current game state with the values of face down cards hidden
	 * @return
	 */
	
	IdiotGameStateFacade getCurrentGameState();
	
	/**
	 * Invoked to request a new game be started, shuffle deck, and distribute cards. 
	 */
	
	void initializeNewGame(int numberOfPlayers, IRuleConfigurationLoader configLoader);
	
	/**
	 * Before play begins, called to request a swap of a hand card with a table card
	 */
	
	MoveResult requestHandToTableCardSwap(int playerRequesting, Card handCard, Card tableCard);
	
	/**
	 * Report to the game engine that player one is done swapping
	 */
	
	void playerOneDoneSwapping();
	
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
	
	MoveResult submitMove(int playerReuqesting, Move move);
	
	List<Card> getPileById(String shortID);
}
