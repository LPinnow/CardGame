package controller;

import java.util.ArrayList;
import java.util.List;

import model.*;
import model.card.Card;
import model.card.CardDeck;
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
	public void initializeNewGame(GameBoard gameBoard, int numberOfPlayers) {
		
		state = new IdiotGameState(numberOfPlayers);
		
		state.CurrentGamePhase = IdiotGameState.GamePhases.ResettingGame;
		state.discardedCards = new ArrayList<Card>();
		
		CardDeck deck = CardDeck.createGameCardDeck();
		
		// create original deck of cards to be used by the InputManager
		state.fullDeck = deck;
		
		deck.shuffle();

		state.drawCards = deck.getCards();
		state.idDrawCards = "drawCards";
		
		state.pile = new ArrayList<Card>();
		state.idPile = "pile";
		
		state.discardedCards = new ArrayList<Card>();
		
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
			
			//flip the top card in each pile and in the hands
			playerPlace.tableCards1.get(playerPlace.tableCards1.size()-1).flip();
			playerPlace.tableCards2.get(playerPlace.tableCards2.size()-1).flip();
			playerPlace.tableCards3.get(playerPlace.tableCards3.size()-1).flip();
			
			for (Card card : playerPlace.hand){
				card.flip();
			}
			
			//set pile ids for InputManager. Ids must match corresponding CardPileView ids.
			playerPlace.initializePileIDs();
			
			//draw the cards in the PlayerZone
			gameBoard.drawPlayerPlace(playerPlace);
		}
		
		//draw the deck
		gameBoard.drawDeck(state.drawCards);
		
		state.CurrentGamePhase = IdiotGameState.GamePhases.CardSwapping;
	}

	@Override
	public MoveResult requestHandToTableCardSwap(Player playerRequesting, Card handCard, Card tableCard) {
		
		if (state.CurrentGamePhase != IdiotGameState.GamePhases.CardSwapping) {
			return new MoveResult() {{ success = false; message = "Swapping table cards is not valid in this state of the game."; }};
		}

		TableSwapValidationResult validationResult = tableSwapValidator.isValidSwap(playerRequesting.getNum(), handCard, tableCard);
		
		if (validationResult.Success) {
			
			//System.out.println(state.toString());
			
			updateStateForTableSwap(playerRequesting, handCard, tableCard, validationResult.targetTableStack);
			
			//System.out.println(state.toString());
			
			return new MoveResult() {{ success = true; }};
		} 
		else {
			return new MoveResult() {{ success = false; message = validationResult.ErrorMessage; }};
		}
	}

	private synchronized void updateStateForTableSwap(Player playerRequesting, Card handCard, Card tableCard, int targetTableStack) {
		
		int playerPlaceIndex = playerRequesting.getNum()-1;
		
		state.PlayerPlaces.get(playerPlaceIndex).hand.remove(handCard);
		
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
		
		state.PlayerPlaces.get(playerPlaceIndex).hand.add(tableCard);
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
	
	/**
	 * Looks up a List<Card> by its id
	 * Returns the List<Card> with a matching id
	 * 
	 * @param shortID
	 * @return
	 */
	@Override
	public List<Card> getPileById(String shortID) {
		
		if(state.idDrawCards.equalsIgnoreCase(shortID)){
			return state.drawCards;
		} else if (state.idPile.equalsIgnoreCase(shortID)){
			return state.pile;
		}
		
		for (PlayerZone playerPlace: state.PlayerPlaces ) {
			if(playerPlace.idHand.equals(shortID))
				return playerPlace.hand;
			else if (playerPlace.idTableCards1.equals(shortID))
				return playerPlace.tableCards1;
			else if (playerPlace.idTableCards2.equals(shortID))
				return playerPlace.tableCards2;
			else if (playerPlace.idTableCards3.equals(shortID))
				return playerPlace.tableCards3;
		}
		
		return null;
	}
}
