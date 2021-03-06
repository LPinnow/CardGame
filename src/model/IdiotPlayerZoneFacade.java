package model;

import java.util.List;

import model.card.Card;
import model.card.PlayerHand;
import model.card.TopCardUpStack;

public class IdiotPlayerZoneFacade {

	final protected PlayerZone wrappedPlayerZone;

	public IdiotPlayerZoneFacade(PlayerZone wrappedPlayerZone) {
		this.wrappedPlayerZone = wrappedPlayerZone;
	}

	public int getPlayerNumber() {
		return wrappedPlayerZone.playerNumber;
	}

	public PlayerHand getHand() {
		return new PlayerHand(wrappedPlayerZone.hand);
	}
	
	//TODO Control view's access to this
	public List<Card> getAllTableCards1() {
		return wrappedPlayerZone.tableCards1;
	}
	
	//TODO Control view's access to this
	public List<Card> getAllTableCards2() {
		return wrappedPlayerZone.tableCards2;
	}
	
	//TODO Control view's access to this
	public List<Card> getAllTableCards3() {
		return wrappedPlayerZone.tableCards3;
	}

	public TopCardUpStack getTableCards1() {

		return getTopCardUpStackFromCardList(wrappedPlayerZone.tableCards1);
	}

	public TopCardUpStack getTableCards2() {

		return getTopCardUpStackFromCardList(wrappedPlayerZone.tableCards2);
	}

	public TopCardUpStack getTableCards3() {
		return getTopCardUpStackFromCardList(wrappedPlayerZone.tableCards3);
	}

	private TopCardUpStack getTopCardUpStackFromCardList(List<Card> tableCards) {
		return new TopCardUpStack(
				tableCards.size() > 0 ? tableCards.get(tableCards.size() - 1)
						: null, tableCards.size());
	}
}
