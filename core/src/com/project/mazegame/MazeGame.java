package com.project.mazegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project.mazegame.screens.MenuScreen;
import com.project.mazegame.screens.SplashScreen;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.AudioHandler;


public class MazeGame extends Game {
	public SpriteBatch batch;
	public AssetManager assets;
	public static AssetManager manager;
	public OrthographicCamera camera;
	public AudioHandler audio;




	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;


	@Override
	public void create () {
		assets = new AssetManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		//Only for test
		Assets.load();
		Assets.manager.finishLoading();
		audio = new AudioHandler();
//		this.setScreen(new MenuScreen(this));
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
