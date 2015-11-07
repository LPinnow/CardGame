package model;

import model.card.Card;

public class PlayOneCardMove extends Move {

	public Card card;
	public PlayOneCardMove(String moveFrom, String moveTo,Card card) {
		super(moveFrom, moveTo);
		this.card = card;
		// TODO Auto-generated constructor stub
	}
	
}
