package controller.validators;

import model.*;
import model.card.Card;

public class TableSwapValidator implements ITableSwapValidator {

	protected IdiotGameState state;
	
	public TableSwapValidator() {
	}
	
	public void setState(IdiotGameState state) {
		this.state = state;
	}
	
	public TableSwapValidationResult isValidSwap(int playerNumber, Card handCard, Card tableCard) {
		
		TableSwapValidationResult result = new TableSwapValidationResult();
		
		if (!playerNumberIsValid(playerNumber, result)) return result;
		
		int tableStackContainingCard = tableCardStackContainingTableCard(tableCard, playerNumber, result);
		
		if (tableStackContainingCard == 0) return result;
		
		if (!cardIsPresentInPlayersHand(handCard, playerNumber, result)) return result;
		
		result.Success = true;
		return result; 
		
	}
	
	private boolean playerNumberIsValid(int playerNumber, TableSwapValidationResult result) {
		
		if (playerNumber < 1 || playerNumber > state.PlayerPlaces.size()) {
			result.ErrorMessage = "Invalid Player Number: " + playerNumber;
			result.Success = false;
			return false;
		} 
		
		return true;
	}
	
	private int tableCardStackContainingTableCard(Card tableCard, int playerNumber,  TableSwapValidationResult result) {
		
		int tableCardStackFoundIn = state.PlayerPlaces.get(playerNumber-1).getMatchingFaceUpTableCard(tableCard);
		
		if (tableCardStackFoundIn == 0) {
			result.ErrorMessage = "Table card requested for swap not among Player " + playerNumber + "'s face up cards";
			result.Success = false;
		}
		else {
			result.targetTableStack = tableCardStackFoundIn;
		}
		return tableCardStackFoundIn;
	}
	
	private boolean cardIsPresentInPlayersHand(Card handCard, int playerNumber,  TableSwapValidationResult result) {
		
		if (!state.PlayerPlaces.get(playerNumber-1).hand.contains(handCard)) {
			result.ErrorMessage = "Hand card requested for swap not in Player " + playerNumber + "'s hand";
			result.Success = false;
			return false;
		} 
		
		return true;
	}

}
