package com.example.vers7.components;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.example.vers7.MainGame.TILE_SIZE;


/**
 * Represents a Pawn within the main game screen.
 */

public class PawnComponent extends StackPane {

	private PawnType type;
	private double mouseX, mouseY;
	private double oldX, oldY;
	
	public PawnType getType() {
		return type;
	}
	
	public double getOldX() {
		return oldX;
	}
	
	public double getOldY() {
		return oldY;
	}
	
	public PawnComponent(PawnType type, int x, int y, String name, String colour) {
		this.type = type;
		
		move(x, y);
		        
        Circle ellipse = new Circle(TILE_SIZE * 0.3215);
        ellipse.setFill(Color.web(colour));
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);
        
        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(ellipse);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
	}
	
	public void move(int x, int y) {
		oldX = x * TILE_SIZE;
		oldY = y * TILE_SIZE;
		relocate(oldX, oldY);
	}
	
	public void reverseMove() {
		relocate(oldX, oldY);
	}
	
	public enum PawnType {
	    RED,  BLUE;
	}

}
