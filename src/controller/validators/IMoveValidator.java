package controller.validators;

import model.IdiotGameConfiguration;
import model.IdiotGameState;
import model.move.Move;

public interface IMoveValidator {
	ValidationResult IsValidMove(Move move);
	void setState(IdiotGameState state);
	void setConfig(IdiotGameConfiguration config);
}
