package model;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<IdiotPlayerZoneFacade> getPlayerPlaces() {
		List<IdiotPlayerZoneFacade> playerPlaces = new ArrayList<IdiotPlayerZoneFacade>();
		wrappedState.PlayerPlaces.forEach(place -> playerPlaces.add(new IdiotPlayerZoneFacade(place)));
		return playerPlaces;
	}
}
