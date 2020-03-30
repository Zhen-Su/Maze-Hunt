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

import org.junit.Assert;

public class MazeGame extends Game {
	public SpriteBatch batch;
	public AssetManager assets;
	public OrthographicCamera camera;
	public AudioHandler audio;

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;


	@Override
	public void create () {
		assets = new AssetManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		this.setScreen(new SplashScreen(this));

		Assets.load();
		Assets.manager.finishLoading();
		System.out.println(Assets.manager.update());

		audio = new AudioHandler();
		audio.setCurrentScreen("menu");

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
