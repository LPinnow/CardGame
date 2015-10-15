package cardGameModel;

/**
 * Class representing a player
 *
 */
public class Player {
	/**
	 * Player number
	 */
	int num;
	
	/**
	 * String representing a player's name
	 */
	String name;
	
	/**
	 * Represents if player is ready
	 * true: player is ready
	 * false: player is not ready
	 */
	boolean ready;
	
	/**
	 * Represents if player has control of current turn
	 * true: It is the player's turn
	 * false: It is not the player's turn
	 */
	boolean activeTurn;
	
	/**
	 * 
	 * @param num
	 * @param name
	 * @param ready
	 */
	public Player(int num, String name, boolean ready, boolean activeTurn){
		this.num = num;
		this.name = name;
		this.ready = ready;
		this.activeTurn = activeTurn;
	}

	/**
	 * @return the player's number
	 */
	public int getNum() {
		return num;
	}

	/**
	 * 
	 * @param num the number to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @param ready 
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public boolean isActive() {
		return activeTurn;
	}
	
	public void setActiveTurn(boolean activeTurn){
		this.activeTurn = activeTurn;
	}

}
