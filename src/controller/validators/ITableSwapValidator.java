package controller.validators;

import model.*;
import model.card.Card;

public interface ITableSwapValidator {

	TableSwapValidationResult isValidSwap(int playerNumber, Card handCard, Card tableCard);
	void setState(IdiotGameState state);
}
