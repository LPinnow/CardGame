package controller;

import java.util.ArrayList;

import model.*;
import controller.validators.*;

public class IdiotGameEngine implements IIdiotGameEngine {

	protected IdiotGameState state = new IdiotGameState();
	protected ITableSwapValidator tableSwapValidator;
	
	public IdiotGameEngine(ITableSwapValidator tableSwapValidator) {
		this.tableSwapValidator = tableSwapValidator;
	}
	
	@Override
	public IdiotGameStateFacade getCurrentGameState() {
		return new IdiotGameStateFacade(state);
	}
	
	@Override
	public void initializeNewGame(int numberOfPlayers) {
		
		state = new IdiotGameState(numberOfPlayers);
		
		state.CurrentGamePhase = IdiotGameState.GamePhases.ResettingGame;
		state.discardedCards = new ArrayList<Card>();
		
		CardDeck deck = CardDeck.createGameCardDeck();
		
		deck.shuffle();

		state.drawCards = deck.getCards();
		
		state.pile = new ArrayList<Card>();
		state.discardedCards = new ArrayList<Card>();
		
		for (PlayerZone playerPlace : state.PlayerPlaces ) {
			playerPlace = new PlayerZone(playerPlace.playerNumber);
		}
		
		for (PlayerZone playerPlace: state.PlayerPlaces ) {
			playerPlace.tableCards1.add(state.drawCards.remove(0));
			playerPlace.tableCards1.add(state.drawCards.remove(0));
			playerPlace.tableCards2.add(state.drawCards.remove(0));
			playerPlace.tableCards2.add(state.drawCards.remove(0));
			playerPlace.tableCards3.add(state.drawCards.remove(0));
			playerPlace.tableCards3.add(state.drawCards.remove(0));
			playerPlace.hand.add(state.drawCards.remove(0));
			playerPlace.hand.add(state.drawCards.remove(0));
			playerPlace.hand.add(state.drawCards.remove(0));
		}
		
		state.CurrentGamePhase = IdiotGameState.GamePhases.CardSwapping;
	}

	@Override
	public MoveResult requestHandToTableCardSwap(Player playerRequesting, Card handCard, Card tableCard) {
		
		if (state.CurrentGamePhase != IdiotGameState.GamePhases.CardSwapping) {
			return new MoveResult() {{ success = false; message = "Game is currently not in state to swap cards"; }};
		}
		
		TableSwapValidationResult validationResult = tableSwapValidator.isValidSwap(playerRequesting.getNum(), handCard, tableCard);
		
		if (validationResult.Success) {
			
			updateStateForTableSwap(playerRequesting, handCard, tableCard, validationResult.targetTableStack);
			
			return new MoveResult() {{ success = true; }};
		} 
		else {
			return new MoveResult() {{ success = false; message = validationResult.ErrorMessage; }};
		}
	}

	private synchronized void updateStateForTableSwap(Player playerRequesting, Card handCard, Card tableCard, int targetTableStack) {
		
		int playerPlaceIndex = playerRequesting.getNum()-1;
		
		state.PlayerPlaces.get(playerRequesting.getNum()-1).hand.remove(handCard);
		
		if (targetTableStack == 1) {
			state.PlayerPlaces.get(playerPlaceIndex).tableCards1.remove(tableCard);
			state.PlayerPlaces.get(playerPlaceIndex).tableCards1.add(handCard);
		}
		if (targetTableStack == 2) {
			state.PlayerPlaces.get(playerPlaceIndex).tableCards2.remove(tableCard);
			state.PlayerPlaces.get(playerPlaceIndex).tableCards2.add(handCard);
		}
		if (targetTableStack == 3) {
			state.PlayerPlaces.get(playerPlaceIndex).tableCards3.remove(tableCard);
			state.PlayerPlaces.get(playerPlaceIndex).tableCards3.add(handCard);
		}
		
		state.PlayerPlaces.get(playerRequesting.getNum()-1).hand.add(tableCard);
	}
	
	@Override
	public void beginPlay() {
		state.CurrentGamePhase = IdiotGameState.GamePhases.GamePlay;
		state.currentPlayerTurn = 1;
	}

	@Override
	public MoveResult submitMove(Player playerRequesting, Move move) {
		
		if (state.CurrentGamePhase != IdiotGameState.GamePhases.GamePlay) {
			return new MoveResult() {{ success = false; message = "Game play has not yet started"; }};
		}
		
		//TODO: Finish implementing method
		return null;
	}
	

}
