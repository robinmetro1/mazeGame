package com.example.vers7.components;

import com.example.vers7.MainGame;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * Represents a tile component.
 *
 */
public class TileComponent extends Rectangle {
        
	public TileComponent(int x, int y) {
		setWidth(MainGame.TILE_SIZE);
		setHeight(MainGame.TILE_SIZE);
		relocate(x * MainGame.TILE_SIZE, y * MainGame.TILE_SIZE);
		setFill(Color.valueOf("#FFFFFF"));
	}
	
}
