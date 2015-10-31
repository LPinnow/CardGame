package controller;

import java.util.ArrayList;
import java.util.List;

import model.*;

public class IdiotGameEngine implements IIdiotGameEngine {

	public IdiotGameState state = new IdiotGameState();
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beginPlay() {
		// TODO Auto-generated method stub

	}

	@Override
	public MoveResult submitMove(Player playerRequesting, Move move) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
