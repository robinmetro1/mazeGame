package model;

/**
 * 
 * A class to handle all the Settings in the game.
 *
 */
public class Settings {
	
	private static Settings instance = null;
	

	private int walls;

	private int score;


	private int boardWidth;
	private int boardHeight;
	private int tileSize;
	
	
	private Settings() {
		reset();
	}
	

	public static Settings getSingleton() {
		if(instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public void reset() {
		setWalls(10);
		setBoardHeight(9);
		setBoardWidth(9);
		setTileSize(50);
	}
	




	public int getWalls() {
		return walls;
	}


	public void setWalls(int walls) {
		this.walls = walls;
	}


	public int getBoardWidth() {
		return boardWidth;
	}


	public void setBoardWidth(int boardWidth) {
		if(boardWidth % 2 == 0) {
			throw new IllegalArgumentException("The board width must be odd.");
		}
		this.boardWidth = boardWidth;
	}


	public int getBoardHeight() {
		return boardHeight;
	}


	public void setBoardHeight(int boardHeight) {
		if(boardHeight % 2 == 0) {
			throw new IllegalArgumentException("The board height must be odd.");
		}		
		this.boardHeight = boardHeight;
	}


	public int getTileSize() {
		return tileSize;
	}


	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
