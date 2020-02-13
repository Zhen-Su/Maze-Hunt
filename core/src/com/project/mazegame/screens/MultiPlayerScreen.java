package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.InputHandler;
import com.project.mazegame.tools.OrthoCam;

public class MultiPlayerScreen implements Screen{

    private MazeGame game;
    private OrthoCam cam;
    private Music bgm;
    private InputHandler textListener;
    private String ip;
    private String username;

    public MultiPlayerScreen(MazeGame game) {
        this.game = game;
        textListener = new InputHandler();

        Gdx.input.getTextInput(textListener, "ENTER IP", "", "Your IP Please");
        ip = textListener.returnText();
        Gdx.input.getTextInput(textListener, "ENTER USERNAME", "", "Your username Please");
        username = textListener.returnText();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.dispose();
        game.setScreen(new MenuScreen(game));
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
