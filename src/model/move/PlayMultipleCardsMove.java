package model.move;

import java.util.List;

import model.card.Card;

public class PlayMultipleCardsMove extends Move {

	public PlayMultipleCardsMove(String moveFrom, String moveTo, List<Card> cards) {
		super(moveFrom, moveTo);
		// TODO Auto-generated constructor stub
		this.cards = cards;
	}

	public List<Card> cards;
}
