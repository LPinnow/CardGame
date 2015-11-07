package controller.validators;

import java.util.ArrayList;
import java.util.List;

import model.*;
import model.card.*;

public class MoveValidator implements IMoveValidator {

	protected IdiotGameState state; 
	protected IdiotGameConfiguration config;
	
	final private ValidationResult successfulResult = new ValidationResult() {{ Success = true; }};
	
	//TODO: clean up this method (break up in to parts and remove redundancies)
	
	@Override
	public ValidationResult IsValidMove(Move move) {
		
		if (move instanceof TakePileMove) {
			if (state.pile.size() > 0) return successfulResult;
			else return new ValidationResult() {{ Success = false; ErrorMessage = "Cannot take pile because it is empty";}};
		}
		
		if (move instanceof PlayTopOfDeck) {
			if (state.drawCards.size() > 0) return successfulResult;
			else return new ValidationResult() {{ Success = false; ErrorMessage = "Cannot play top of deck because it is empty.";}};
		}
		
		if (move instanceof PlayOneCardMove) {
			PlayOneCardMove cardPlay = (PlayOneCardMove)move;
			
			ValidationResult resultOfCheckForCardOwnership = playerHasCardPlayed(cardPlay.card);
			
			if (! resultOfCheckForCardOwnership.Success) return resultOfCheckForCardOwnership;
			
			GameCardRank rank = cardPlay.card.getRank();
			
			if (rank == config.burnCard || rank == config.restartCard || rank == config.reverseCard) return successfulResult;
			
			if (state.pile.size() == 0) return successfulResult;
			
			GameCardRank topPileCardRank = state.pile.get(state.pile.size()-1).getRank();
			
			if (topPileCardRank == config.reverseCard) {
				if (cardPlay.card.getRank().ordinal() > topPileCardRank.ordinal()) return new ValidationResult() {{ Success = false; ErrorMessage = "Top pile card is the reverse card and card played is greater than the reverse card value.";}};
			}
			else {
				if (cardPlay.card.getRank().ordinal() < topPileCardRank.ordinal()) return new ValidationResult() {{ Success = false; ErrorMessage = "Card played is less than top of pile card's value.";}};
			}
			
			return successfulResult;
		}
		
		if (move instanceof PlayMultipleCardsMove) {
			PlayMultipleCardsMove cardsPlay = (PlayMultipleCardsMove)move;
			
			for (Card card : cardsPlay.cards) {
				ValidationResult resultOfCheckForCardOwnership = playerHasCardPlayed(card);
				if (! resultOfCheckForCardOwnership.Success) return resultOfCheckForCardOwnership;
			}
			
			GameCardRank firstCardRank = cardsPlay.cards.get(0).getRank();
			
			for (Card card: cardsPlay.cards) {
				if (card.getRank() != firstCardRank) return new ValidationResult() {{ Success = false; ErrorMessage = "All cards of the multiple ones selected for play are not of the same rank.";}};
			}
			
			if (firstCardRank == config.burnCard || firstCardRank == config.restartCard || firstCardRank == config.reverseCard) return successfulResult;
			
			if (state.pile.size() == 0) return successfulResult;
			
			GameCardRank topPileCardRank = state.pile.get(state.pile.size()-1).getRank();
			
			if (topPileCardRank == config.reverseCard) {
				if (firstCardRank.ordinal() > topPileCardRank.ordinal()) return new ValidationResult() {{ Success = false; ErrorMessage = "Top pile card is the reverse card and card played is greater than the reverse card value.";}};
			}
			else {
				if (firstCardRank.ordinal() < topPileCardRank.ordinal()) return new ValidationResult() {{ Success = false; ErrorMessage = "Card played is less than top of pile card's value.";}};
			}
			
			return successfulResult;
		}
		
		throw new IllegalStateException("Move received for validation is not a known type");
	}
	
	private ValidationResult playerHasCardPlayed(Card card) {
		
		List<Card> currentPlayerHand = state.PlayerPlaces.get(state.currentPlayerTurn - 1).hand;
		List<Card> currentPlayerTableCards1 = state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards1;
		List<Card> currentPlayerTableCards2 = state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards2;
		List<Card> currentPlayerTableCards3 = state.PlayerPlaces.get(state.currentPlayerTurn - 1).tableCards3;
		
		List<Card> faceUpTableCards = new ArrayList<Card>();
		if (currentPlayerTableCards1.size() > 0) faceUpTableCards.add(currentPlayerTableCards1.get(currentPlayerTableCards1.size() - 1));
		if (currentPlayerTableCards2.size() > 0) faceUpTableCards.add(currentPlayerTableCards2.get(currentPlayerTableCards2.size() - 1));
		if (currentPlayerTableCards3.size() > 0) faceUpTableCards.add(currentPlayerTableCards3.get(currentPlayerTableCards3.size() - 1));
		 
		if (currentPlayerHand.size() > 0) {
			if (! currentPlayerHand.contains(card)) return new ValidationResult() {{ Success = false; ErrorMessage = "Card played is not in current player's hand or is not a valid card to play.";}}; 
		}
		else if (! faceUpTableCards.contains(card)) return new ValidationResult() {{ Success = false; ErrorMessage = "Card played is among current player's face up table cards.";}}; 

		return successfulResult; 
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
