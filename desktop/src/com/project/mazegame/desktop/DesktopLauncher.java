package com.project.mazegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.project.mazegame.MazeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60; // when the game is actually running
		config.width = MazeGame.WIDTH;
		config.height = MazeGame.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new MazeGame(), config);
	}
}
