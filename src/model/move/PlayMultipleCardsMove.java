package model.move;

import java.util.List;

import model.card.Card;

public class PlayMultipleCardsMove extends Move {

	public PlayMultipleCardsMove(String moveFrom, String moveTo) {
		super(moveFrom, moveTo);
		// TODO Auto-generated constructor stub
	}

	public List<Card> cards;
}
