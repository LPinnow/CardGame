/**
 * 
 */
package controller.validators;

import model.IdiotGameState;
import model.Move;
import model.MoveResult;
import model.PlayMultipleCardsMove;
import model.PlayOneCardMove;
import model.PlayTopOfDeck;
import model.TakePileMove;
import model.card.Card;

/**
 * MoveValidator class contains validations for all possible move operations.
 */
public class MoveValidator implements IMoveValidator {

	protected IdiotGameState state;
	public static final String TABLE_CARDS1="tablecards1";
	public static final String TABLE_CARDS2="tablecards2";
	public static final String TABLE_CARDS3="tablepile3";
	public static final String PLAYER_HAND="playerhand";
	
	public MoveValidator() {
		
	}
	/**
	 * @param state the state to set
	 */
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
	
	
	/**
	 * Validate move 
	 * @param playerNumber
	 * @param move
	 * @return
	 */
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
	
	
	/**
	 * Validate move 
	 * @param playerNumber
	 * @param move
	 * @return
	 */
	private MoveResult moveCardFromPlayerHandToDiscardPile(int playerNumber,Move move){
		//state.PlayerPlaces.get(index)
		for(Card card : state.PlayerPlaces.get(playerNumber-1).hand){
			
			//if playerhand contains the card to be moved, move it to discard pile and draw a card from the deck.
			//else return error message
		}
		return null;
	}

}
