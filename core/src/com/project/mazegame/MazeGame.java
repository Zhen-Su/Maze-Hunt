package com.project.mazegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project.mazegame.screens.MenuScreen;
import com.project.mazegame.screens.SplashScreen;

public class MazeGame extends Game {
	public SpriteBatch batch;
	public AssetManager assets;
	public OrthographicCamera camera;


	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	
	
	@Override
	public void create () {
		assets = new AssetManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		this.setScreen(new SplashScreen(this));

		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		super.render();
	}


	@Override
	public void dispose () {
		batch.dispose();
		this.getScreen().dispose();
	}
}
