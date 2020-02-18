package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.OrthoCam;

public class MenuScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;

    private static final int EXIT_WIDTH = 100;
    private static final int EXIT_HEIGHT = 50;
    private static final int PLAY_WIDTH = 200;
    private static final int PLAY_HEIGHT = 100;
    private static final int PLAY_Y = MazeGame.HEIGHT / 2 + 50;
    private static final int EXIT_Y = MazeGame.HEIGHT / 2 - 50;


    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;

    public MenuScreen(MazeGame game) {
        this.game = game;
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture ("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.325f, 0.343f, 0.604f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //rendering
        game.batch.begin();


        int x1 = MazeGame.WIDTH / 2 - PLAY_WIDTH / 2;
        if (Gdx.input.getX() < (x1 + PLAY_WIDTH) && Gdx.input.getX() > x1 && MazeGame.HEIGHT - Gdx.input.getY() > PLAY_Y && MazeGame.HEIGHT - Gdx.input.getY() < PLAY_Y + PLAY_HEIGHT) {
            game.batch.draw(playButtonActive, x1, PLAY_Y,PLAY_WIDTH,PLAY_HEIGHT);
            if (Gdx.input.isTouched())
                game.setScreen(new GameScreen(game));
        } else {
            game.batch.draw(playButtonInactive, x1, PLAY_Y,PLAY_WIDTH,PLAY_HEIGHT);
        }

        int x2 = MazeGame.WIDTH / 2 - EXIT_WIDTH / 2;
        if (Gdx.input.getX() < (x2 + EXIT_WIDTH) && Gdx.input.getX() > x2 && MazeGame.HEIGHT - Gdx.input.getY() > EXIT_Y && MazeGame.HEIGHT - Gdx.input.getY() < EXIT_Y + EXIT_HEIGHT) {
            game.batch.draw(exitButtonActive, x2, EXIT_Y,EXIT_WIDTH,EXIT_HEIGHT);
            if (Gdx.input.isTouched())
                Gdx.app.exit();
        }
        else {
            game.batch.draw(exitButtonInactive, x2, EXIT_Y,EXIT_WIDTH,EXIT_HEIGHT);
        }

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
