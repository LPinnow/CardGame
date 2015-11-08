package test.mocks;

import controller.validators.IMoveValidator;
import controller.validators.ValidationResult;
import model.IdiotGameConfiguration;
import model.IdiotGameState;
import model.move.Move;

public class MockMoveValidator implements IMoveValidator {

	@Override
	public ValidationResult IsValidMove(Move move) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public void setState(IdiotGameState state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setConfig(IdiotGameConfiguration config) {
		// TODO Auto-generated method stub

	}

}
