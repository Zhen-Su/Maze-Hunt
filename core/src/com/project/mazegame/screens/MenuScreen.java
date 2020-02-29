package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.OrthoCam;

public class MenuScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;
    private Music bgm;

    /*
    LB = Large Button
    MB = Medium Buttons
    SB = Small Buttons
     */
    private static final int LB_WIDTH = 350;
    private static final int LB_HEIGHT = 200;

    private static final int MB_WIDTH = 250;
    private static final int MB_HEIGHT = 100;

    private static final int SB_HEIGHT = 80;
    private static final int SB_WIDTH = 50;

    private static final int CREATE_HEIGHT = 100;
    private static final int CREATE_WIDTH = 100;

    private static final int CREATE_Y = MazeGame.HEIGHT / 2 - 200;
    private static final int PLAY_Y = MazeGame.HEIGHT / 2 + 50;
    private static final int EXIT_Y = MazeGame.HEIGHT / 2 - 300;
    private static final int JOIN_Y = MazeGame.HEIGHT / 2 - 70;
    private static final int AUDIO_WIDTH = 100;
    private static final int AUDIO_HEIGHT = 50;
    private static final int AUDIO_Y = MazeGame.HEIGHT - AUDIO_HEIGHT;


    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture audioOn;
    Texture audioOff;
    Texture joinMazeButtonActive;
    Texture joinMazeButtonInactive;
    Texture backGround;
    Texture createMazeActive;
    Texture createMazeInactive;

    public MenuScreen(MazeGame game) {
        this.game = game;
        playButtonActive = new Texture("UI\\MenuButtons\\playSoloButton.png");
        playButtonInactive = new Texture ("UI\\MenuButtons\\playSoloButtonPressed.png");

        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");

        joinMazeButtonActive = new Texture("UI\\MenuButtons\\FindMazeButton.png");
        joinMazeButtonInactive = new Texture("UI\\MenuButtons\\FindMazeButtonPressed.png");

        createMazeActive = new Texture("UI\\MenuButtons\\StartNewMazeButton.png");
        createMazeInactive = new Texture("UI\\MenuButtons\\StartNewMazeButtonPressed.png");


        audioOn = new Texture("UI\\audioOn.png");
        audioOff = new Texture("UI\\audioOff.png");

        backGround = new Texture("UI\\MenuButtons\\menuBackground.png");

        bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds\\menuBgm.mp3"));
        bgm.setLooping(true);
        bgm.play();
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

        game.batch.draw(backGround,0,0,1000,1000);

        int drawX = xMid("MB");
        if (isHovering(drawX, PLAY_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(playButtonActive, drawX, PLAY_Y,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.isTouched()) {
                bgm.stop();
                game.setScreen(new GameScreen(game));
            }
        } else {
            game.batch.draw(playButtonInactive, drawX, PLAY_Y,MB_WIDTH, MB_HEIGHT);
        }


        if (isHovering(drawX, JOIN_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(joinMazeButtonActive, drawX, JOIN_Y,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.isTouched()) {
                bgm.stop();
                game.setScreen(new MultiPlayerScreen(game));
            }
        } else {
            game.batch.draw(joinMazeButtonInactive, drawX, JOIN_Y,MB_WIDTH, MB_HEIGHT);
        }

        if (isHovering(drawX, CREATE_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(createMazeActive, drawX, CREATE_Y,MB_WIDTH, MB_HEIGHT);
             //bring to maze creation thing
            if(Gdx.input.isTouched())
            {
                bgm.stop();
                game.setScreen(new CreateMazeScreen(game));
            }
        } else {
            game.batch.draw(createMazeInactive, drawX, CREATE_Y,MB_WIDTH, MB_HEIGHT);
        }

        drawX = xMid("SB");
        if (isHovering(drawX, EXIT_Y, SB_WIDTH, SB_HEIGHT)) {
            game.batch.draw(exitButtonActive, drawX, EXIT_Y,SB_WIDTH, SB_HEIGHT);
            if (Gdx.input.isTouched())
                Gdx.app.exit();
        }
        else {
            game.batch.draw(exitButtonInactive, drawX, EXIT_Y,SB_WIDTH, SB_HEIGHT);
        }

        drawX = xMid("AudioButton");
        if (isHovering(drawX, AUDIO_Y, AUDIO_WIDTH, AUDIO_HEIGHT)) {
            if (Gdx.input.isTouched()) {
                if (bgm.isPlaying()) {
                    bgm.setLooping(false);
                    bgm.stop();
                } else {
                    bgm.setLooping(true);
                    bgm.play();
                }
            }
        }

        if (bgm.isPlaying())
            game.batch.draw(audioOff, drawX, AUDIO_Y,AUDIO_WIDTH,AUDIO_HEIGHT);
        else
            game.batch.draw(audioOn, drawX, AUDIO_Y,AUDIO_WIDTH,AUDIO_HEIGHT);

        game.batch.end();
    }

    private boolean isHovering(int X, int  Y, int WIDTH, int HEIGHT) {
        if (Gdx.input.getX() < (X + WIDTH) && Gdx.input.getX() > X && MazeGame.HEIGHT - Gdx.input.getY() > Y && MazeGame.HEIGHT - Gdx.input.getY() < Y + HEIGHT)
            return true;
        return false;
    }

    private int xMid(String buttonSize) {
        switch (buttonSize) {
            case "LB":
                return MazeGame.WIDTH / 2 - LB_WIDTH / 2;
            case "MB":
                return MazeGame.WIDTH / 2 - MB_WIDTH / 2;
            case "SB":
                return MazeGame.WIDTH / 2 - SB_WIDTH / 2;
            case "AudioButton":
                return MazeGame.WIDTH - AUDIO_WIDTH;
            default:
                return -10;
        }
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
