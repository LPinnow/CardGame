package controller;

import model.IdiotGameState;

public interface IEndGameChecker {

	boolean endGameConditionReached();
	void setState(IdiotGameState state);
}
