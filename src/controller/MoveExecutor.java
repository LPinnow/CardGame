package controller;

import java.util.ArrayList;

import controller.validators.ValidationResult;
import model.IdiotGameConfiguration;
import model.IdiotGameState;
import model.Move;
import model.MoveResult;
import model.PlayMultipleCardsMove;
import model.PlayOneCardMove;
import model.PlayTopOfDeck;
import model.TakePileMove;
import model.card.Card;
import model.card.GameCardRank;

public class MoveExecutor implements IMoveExecutor {

	protected IEndGameChecker endGameChecker;
	protected IdiotGameState state; 
	protected IdiotGameConfiguration config;
	
	public MoveExecutor(IEndGameChecker endGameChecker) {
		this.endGameChecker = endGameChecker;
	}
	
	@Override
	public MoveResult executeMove(Move move) {

		if (move instanceof TakePileMove) {
			state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.addAll(state.pile);
			state.pile = new ArrayList<Card>();
		}
	
		if (move instanceof PlayTopOfDeck) {
			
			Card cardFromTopOfDeck = state.drawCards.get(state.drawCards.size() - 1);
			
			GameCardRank topPileCardRank = state.pile.get(state.pile.size()-1).getRank();
			
			if (cardCanBePlayedOnPile(cardFromTopOfDeck.getRank(), topPileCardRank)) {
				state.pile.add(cardFromTopOfDeck);
				state.drawCards.remove(cardFromTopOfDeck);
				handleIfCardPlayedOnPileWasBurnCard();
				handleIfTopFourPileCardsAreSameRank();
			}
			else {
				state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.add(cardFromTopOfDeck);
				state.drawCards.remove(cardFromTopOfDeck);
				state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.addAll(state.pile);
				state.pile = new ArrayList<Card>();
			}
		}
		
		if (move instanceof PlayOneCardMove) {
			PlayOneCardMove cardPlay = (PlayOneCardMove)move;
			
			if (! cardCanBePlayedOnPile(cardPlay.card.getRank(), state.pile.get(state.pile.size()-1).getRank()))
					throw new IllegalStateException("Validator should have prevented an invalid pile card play from occurring.");
			
			state.pile.add(cardPlay.card); // (move should be validated by move validator before this point)
			if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.contains(cardPlay.card))
				state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.remove(cardPlay.card);
			if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards1.contains(cardPlay.card))
				state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards1.remove(cardPlay.card);
			if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards2.contains(cardPlay.card))
				state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards2.remove(cardPlay.card);		
			if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards3.contains(cardPlay.card))
				state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards3.remove(cardPlay.card);	
			
			handleIfCardPlayedOnPileWasBurnCard();
			handleIfTopFourPileCardsAreSameRank();
		}
		
		if (move instanceof PlayMultipleCardsMove) {
			PlayMultipleCardsMove cardsPlay = (PlayMultipleCardsMove)move;
			
			if (! cardCanBePlayedOnPile(cardsPlay.cards.get(0).getRank(), state.pile.get(state.pile.size()-1).getRank()))
				throw new IllegalStateException("Validator should have prevented an invalid pile card play from occurring.");
			
			state.pile.addAll(cardsPlay.cards); // (move should be validated by move validator before this point)
			for (Card card: cardsPlay.cards) {
				if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.contains(card))
					state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand.remove(card);
				if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards1.contains(card))
					state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards1.remove(card);
				if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards2.contains(card))
					state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards2.remove(card);		
				if (state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards3.contains(card))
					state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards3.remove(card);	
			}
			
			handleIfCardPlayedOnPileWasBurnCard();
			handleIfTopFourPileCardsAreSameRank();
		}
			
		if (endGameChecker.endGameConditionReached()) return new MoveResult() {{ success = true; gameEnded = true; message = "Player " + state.currentPlayerTurn + " has won"; }};
		else advancePlayerTurn();
		
		return new MoveResult() {{ success = true; }};
	}
	
	private boolean cardCanBePlayedOnPile(GameCardRank candidateCardRank, GameCardRank topOfPileCardRank) {
		
		if (candidateCardRank == config.reverseCard || candidateCardRank == config.burnCard || candidateCardRank == config.restartCard) return true;
		
		if (topOfPileCardRank == config.reverseCard) {
			if (candidateCardRank.ordinal() > topOfPileCardRank.ordinal()) return false;
		}
		else {
			if (candidateCardRank.ordinal() < topOfPileCardRank.ordinal()) return false;
		}
		return true;
	}
	
	private void handleIfCardPlayedOnPileWasBurnCard() {
		if (state.pile.get(state.pile.size() - 1).getRank() == config.burnCard) {
			state.discardedCards.addAll(state.pile);
			state.pile = new ArrayList<Card>();
		}
	}
	
	private void handleIfTopFourPileCardsAreSameRank() {
		if (state.pile.size() >= 4) {
			GameCardRank topCardRank = state.pile.get(state.pile.size() - 1).getRank();
			boolean lastFourCardsAreSameRank = (topCardRank == state.pile.get(state.pile.size() - 2).getRank()) && 
					                           (topCardRank == state.pile.get(state.pile.size() - 3).getRank()) &&  
					                           (topCardRank == state.pile.get(state.pile.size() - 4).getRank());
			
			if (lastFourCardsAreSameRank) {
				state.discardedCards.addAll(state.pile);
				state.pile = new ArrayList<Card>();
			}
		}
	
	}
	
	private void advancePlayerTurn() {
		if (state.currentPlayerTurn == 2) state.currentPlayerTurn = 1;
		if (state.currentPlayerTurn == 1) state.currentPlayerTurn = 2;
	}

	@Override
	public void setState(IdiotGameState state) {
		this.state = state;
	}

	@Override
	public void setConfig(IdiotGameConfiguration config) {
		this.config = config;

	}
}
