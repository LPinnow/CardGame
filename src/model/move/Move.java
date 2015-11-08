package model.move;

public abstract class Move {
	
 protected String moveFrom;
 protected String moveTo;
  
 public Move(String moveFrom, String moveTo){ 
	 this.moveFrom = moveFrom;
	 this.moveTo  = moveTo;
 }

/**
 * @return the present card location
 */
public String getMoveFrom() {
	return moveFrom;
}

/**
 * @return the destination location
 */
public String getMoveTo() {
	return moveTo;
}

	
}
