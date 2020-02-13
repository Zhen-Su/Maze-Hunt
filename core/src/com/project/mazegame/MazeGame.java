package com.project.mazegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.MenuScreen;

public class MazeGame extends Game {
	public SpriteBatch batch;

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;

	public Player player;
	
	
	@Override
	public void create () {
		this.setScreen(new MenuScreen(this));
		batch = new SpriteBatch();

	}

	@Override
	public void render () {
		super.render();
	}

	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
