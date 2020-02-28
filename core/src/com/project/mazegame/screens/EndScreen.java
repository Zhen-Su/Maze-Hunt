package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.project.mazegame.MazeGame;

public class EndScreen implements Screen {

    private MazeGame game;
    Texture backGround;
    public EndScreen(MazeGame game) {
        this.game = game;
        
        backGround = new Texture("UI\\menuBackground.png");
    }

    @Override
    public void show() {
    	
    }

    @Override
    public void render(float delta) {
    	 Gdx.gl.glClearColor(0, 0, 0, 1);
         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
         
         game.batch.begin();

         game.batch.draw(backGround,0,0,1000,1000);
         
         game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
