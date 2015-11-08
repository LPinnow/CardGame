package controller;

import model.IdiotGameConfiguration;
import model.IdiotGameState;
import model.move.Move;
import model.move.MoveResult;

public interface IMoveExecutor {
	
	MoveResult executeMove(Move move);
	void setState(IdiotGameState state);
	void setConfig(IdiotGameConfiguration config);
	
}
