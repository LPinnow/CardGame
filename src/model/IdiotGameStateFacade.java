package model;

import java.util.ArrayList;
import java.util.List;

import model.card.CardDeck;
import model.card.FaceDownCardStack;
import model.card.FaceUpCardStack;

public class IdiotGameStateFacade {

	final protected IdiotGameState wrappedState;
	
	public IdiotGameStateFacade(IdiotGameState wrappedState) {
		this.wrappedState = wrappedState;
	}
	
	public IdiotGameState.GamePhases CurrentGamePhase() {
		return wrappedState.CurrentGamePhase;
	}
	
	public int CurrentPlayerTurn() {
		return wrappedState.currentPlayerTurn;
	}
	
	public FaceDownCardStack GetDeck() {
		return new FaceDownCardStack(wrappedState.drawCards.size());
	}
	
	public FaceUpCardStack GetPile() {
		return new FaceUpCardStack(wrappedState.pile);
	}
	
	public CardDeck GetFullDeck() {
		return wrappedState.fullDeck;
	}
	
	public List<IdiotPlayerZoneFacade> getPlayerPlaces() {
		List<IdiotPlayerZoneFacade> playerPlaces = new ArrayList<IdiotPlayerZoneFacade>();
		wrappedState.PlayerPlaces.forEach(place -> playerPlaces.add(new IdiotPlayerZoneFacade(place)));
		return playerPlaces;
	}

}
