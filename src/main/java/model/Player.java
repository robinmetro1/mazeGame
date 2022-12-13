package model;

/**
 * Represents a Player in the game.
 */
public abstract class Player {
	private String name;

	private String username;

	private String password;

	private int walls;
	private Statistics stats;
	private String pawnColour;
	 private int score;
	
	/**
	 * Creates a new Player by initialising its name and pawn
	 * Generates UUID 
	 * @param name
	 */
	public Player(String name, String pawnColour){
		//Generate UUID
		
		//Initialise values.
		this.name = name;
		walls = Settings.getSingleton().getWalls();
		this.stats = new Statistics();
		this.pawnColour = pawnColour;

	}
	public Player(){

	}


	/**
	 * Gets the name of the player.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the amount of walls that the player has.
	 * @return the number of walls
	 */
	public int getWalls() {
		return walls;
	}
	
	/**
	 * Decrements the number of walls that the player has by one.
	 * @throws IllegalStateException if the number of walls is below 0
	 */
	public void decrementWalls() {
		if(walls == 0) {
			throw new IllegalStateException("The number of walls cannot be decremented below 0.");
		}
		System.out.println(name + ": " + walls + " walls left ");
		walls--;
	}



	
	/**
	 * Get this player's {@link Statistics}
	 * @return the statistics
	 */
	public Statistics getStatistics() {
		return stats;
	}
	
	/**
	 * Gets this player's Pawn {@Colour}
	 * @return the colour
	 */
	public String getPawnColour() {
		return pawnColour;
	}

	public void updateScore(int s){
		score+=s;

	}


	public int getScore() {
		return score;
	}


	public String getUsername() {
		return username;

	}
	public int setScore(int score) {
		return score;
	}
}
