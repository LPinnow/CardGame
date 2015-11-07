package controller;

import model.IdiotGameConfiguration;
import model.IdiotGameState;
import model.Move;
import model.MoveResult;

public interface IMoveExecutor {
	
	MoveResult executeMove(Move move);
	void setState(IdiotGameState state);
	void setConfig(IdiotGameConfiguration config);
	
}
