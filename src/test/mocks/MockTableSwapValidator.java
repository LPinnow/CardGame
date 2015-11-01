package test.mocks;

import controller.validators.*;
import model.card.Card;

public class MockTableSwapValidator implements ITableSwapValidator {

	public TableSwapValidationResult resultToReturn;
	
	public MockTableSwapValidator(TableSwapValidationResult resultToReturn) {
		this.resultToReturn = resultToReturn;
	}
	
	@Override
	public TableSwapValidationResult isValidSwap(int playerNumber, Card handCard, Card tableCard) {
		return resultToReturn;
	}
	
}
