package controller;

import model.IdiotGameState;

public class EndGameChecker implements IEndGameChecker {

protected IdiotGameState state;
	
	public EndGameChecker(IdiotGameState state) {
		this.state = state;
	}

	public boolean endGameConditionReached() {
		
		if (deckIsEmpty() && currentPlayersHandIsEmpty() && currentPlayerHasPlayedAllTableCards()) return true;
		else return false;	
	}
	
	private boolean deckIsEmpty() {
		return (state.drawCards.size() == 0);
	}
	
	private boolean currentPlayersHandIsEmpty() {
		return (state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.size() == 0);
	}
	
	private boolean currentPlayerHasPlayedAllTableCards() {
		return (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards1.size() == 0) &&
			   (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards2.size() == 0) &&
		       (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards3.size() == 0);
	}    
}
