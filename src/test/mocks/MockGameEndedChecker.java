package test.mocks;

import controller.IEndGameChecker;
import model.IdiotGameState;

public class MockGameEndedChecker implements IEndGameChecker {

	@Override
	public boolean endGameConditionReached() {
		return false;
	}

	@Override
	public void setState(IdiotGameState state) {

	}

}
