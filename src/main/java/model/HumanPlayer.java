package model;

/**
 * This class creates a new Human Player object.
 */


public class HumanPlayer extends Player {
	private String username;
	private String password;

	public HumanPlayer(String name, String pawnColour,String username, String password) {
		super(name, pawnColour);
		this.username =username;
		this.password = password;
	}

	 public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
