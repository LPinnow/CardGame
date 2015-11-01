package controller.validators;

import model.*;

public interface ITableSwapValidator {

	TableSwapValidationResult isValidSwap(int playerNumber, Card handCard, Card tableCard);
}
