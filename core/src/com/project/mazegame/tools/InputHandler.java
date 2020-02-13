package com.project.mazegame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.project.mazegame.MazeGame;

import static com.project.mazegame.tools.Variables.*;

public class InputHandler implements Input.TextInputListener {
	private String txt;
  	public InputHandler() {

  	}
  	public void update(){
		// reset all variables
		UP_TOUCHED = false;
		DOWN_TOUCHED = false;
		LEFT_TOUCHED = false;
		RIGHT_TOUCHED = false;

		// set boolean to true if key is touched
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			LEFT_TOUCHED = true;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			DOWN_TOUCHED = true;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			RIGHT_TOUCHED = true;
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			UP_TOUCHED = true;
		}
//		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
//			game.setScreen(new MenuScreen(this.game));
//		}
	}

	@Override
	public void input(String text) {
  		txt = text;
	}

	public String returnText() {
  		return txt;
	}

	@Override
	public void canceled() {

	}
}
