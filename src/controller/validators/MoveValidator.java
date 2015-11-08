
package controller.validators;

import java.util.ArrayList;
import java.util.List;

import model.*;
import model.card.*;
import model.move.Move;
import model.move.PlayMultipleCardsMove;
import model.move.PlayOneCardMove;
import model.move.PlayTopOfDeck;
import model.move.TakePileMove;

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
/*=======
*//**
 * 
 *//*
package controller.validators;

import model.IdiotGameState;
import model.Move;
import model.MoveResult;
import model.PlayMultipleCardsMove;
import model.PlayOneCardMove;
import model.PlayTopOfDeck;
import model.TakePileMove;
import model.card.Card;

*//**
 * MoveValidator class contains validations for all possible move operations.
 *//*
public class MoveValidator implements IMoveValidator {

	protected IdiotGameState state;
	public static final String TABLE_CARDS1="tablecards1";
	public static final String TABLE_CARDS2="tablecards2";
	public static final String TABLE_CARDS3="tablepile3";
	public static final String PLAYER_HAND="playerhand";
	
	public MoveValidator() {
		
	}
	*//**
	 * @param state the state to set
	 *//*
	public void setState(IdiotGameState state) {
		this.state = state;
	}
	
	@Override
	public MoveResult isValidMove(int playerNumber, Move move) {
		
		MoveResult moveresult = new MoveResult(false, null, false, false);
		
		//TODO add logic to validate player number.
		
		if(move instanceof PlayOneCardMove){
			switch (move.getMoveFrom()) {
			case TABLE_CARDS1:
			case TABLE_CARDS2:
			case TABLE_CARDS3:{
					moveresult = moveCardFromTableToDiscardPile(playerNumber,move,move.getMoveFrom());
					break;
			}
						
			case PLAYER_HAND:{
				//TODO:write logic for moving card from player hand to discard pile
			moveresult = moveCardFromPlayerHandToDiscardPile(playerNumber,move);
			}
			
			default:{
				moveresult.setMessage("Requested move is invalid");
				break;
				}
			}
		}
		else if(move instanceof PlayMultipleCardsMove){
			//TODO:add logic for multiple cards move validation
		}
		
		else if(move instanceof TakePileMove){
			//TODO: add logic for take pile move
		}
		
		else if(move instanceof PlayTopOfDeck){
			//TODO: add logic to play top of deck card.
		}
		return moveresult;
	}
	
	
	*//**
	 * Validate move 
	 * @param playerNumber
	 * @param move
	 * @return
	 *//*
	private MoveResult moveCardFromTableToDiscardPile(int playerNumber,Move move,String tablePile){
		switch (tablePile) {
		case TABLE_CARDS1:{
			state.discardedCards.add(state.PlayerPlaces.get(playerNumber-1).tableCards1.remove(0));
			state.PlayerPlaces.get(playerNumber-1).tableCards1.add(state.drawCards.remove(0));
			//state.PlayerPlaces.get(playerNumber-1).tableCards1.get(state.PlayerPlaces.get(playerNumber-1).tableCards1.size()-1).flip();
			break;
			}
		case TABLE_CARDS2:{
			state.discardedCards.add(state.PlayerPlaces.get(playerNumber-1).tableCards2.remove(0));
			state.PlayerPlaces.get(playerNumber-1).tableCards2.add(state.drawCards.remove(0));
			//state.PlayerPlaces.get(playerNumber-1).tableCards2.get(state.PlayerPlaces.get(playerNumber-1).tableCards2.size()-1).flip();
			break;
			}
		case TABLE_CARDS3:{
			state.discardedCards.add(state.PlayerPlaces.get(playerNumber-1).tableCards2.remove(0));
			state.PlayerPlaces.get(playerNumber-1).tableCards2.add(state.drawCards.remove(0));
			//state.PlayerPlaces.get(playerNumber-1).tableCards3.get(state.PlayerPlaces.get(playerNumber-1).tableCards3.size()-1).flip();
			break;
			}

		default:
			break;
		}
		return null;
	}
	
	
	*//**
	 * Validate move 
	 * @param playerNumber
	 * @param move
	 * @return
	 *//*
	private MoveResult moveCardFromPlayerHandToDiscardPile(int playerNumber,Move move){
		//state.PlayerPlaces.get(index)
		for(Card card : state.PlayerPlaces.get(playerNumber-1).hand){
			
			//if playerhand contains the card to be moved, move it to discard pile and draw a card from the deck.
			//else return error message
		}
		return null;
	}

}
>>>>>>> branch 'master' of https://github.com/LPinnow/CardGame.git
*/