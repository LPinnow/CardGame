/**
 * 
 */
package controller.validators;
import model.IdiotGameState;
import model.Move;
import model.MoveResult;

/**
 * @author Divya			
 *
 */	
public interface IMoveValidator {

	MoveResult isValidMove(int playerNumber, Move move);
	void setState(IdiotGameState state);
}

