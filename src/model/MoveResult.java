package model;

public class MoveResult {

	public boolean success;
	public String message;
	public boolean stateChanged;
	public boolean gameEnded;
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the stateChanged
	 */
	public boolean isStateChanged() {
		return stateChanged;
	}
	/**
	 * @param stateChanged the stateChanged to set
	 */
	public void setStateChanged(boolean stateChanged) {
		this.stateChanged = stateChanged;
	}
	/**
	 * @return the gameEnded
	 */
	public boolean isGameEnded() {
		return gameEnded;
	}
	/**
	 * @param gameEnded the gameEnded to set
	 */
	public void setGameEnded(boolean gameEnded) {
		this.gameEnded = gameEnded;
	}
	
	/**
	 * default constructor
	 */
	public MoveResult(){
		
	}
	
	/**
	 * @param success
	 * @param message
	 * @param stateChanged
	 * @param gameEnded
	 */
	public MoveResult(boolean success, String message, boolean stateChanged, boolean gameEnded) {
		this.success = success;
		this.message = message;
		this.stateChanged = stateChanged;
		this.gameEnded = gameEnded;
	}
	
}
