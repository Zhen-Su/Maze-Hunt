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

    private static final int EXIT_WIDTH = 100;
    private static final int EXIT_HEIGHT = 50;
    private static final int PLAY_WIDTH = 200;
    private static final int PLAY_HEIGHT = 100;
    private static final int PLAY_Y = MazeGame.HEIGHT / 2 + 50;
    private static final int EXIT_Y = MazeGame.HEIGHT / 2 - 50;
    private static final int AUDIO_WIDTH = 100;
    private static final int AUDIO_HEIGHT = 50;
    private static final int AUDIO_Y = MazeGame.HEIGHT - EXIT_HEIGHT;


    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture audioOn;
    Texture audioOff;
    Texture joinMazeButtonActive;
    Texture joinMazeButtonInactive;
    Texture backGround;

    public MenuScreen(MazeGame game) {
        this.game = game;
        playButtonActive = new Texture("D:\\UNI\\YearTwo\\Term2\\TeamProject\\anotherworld\\android\\assets\\UI\\MenuButtons\\playSoloButton.png");
        playButtonInactive = new Texture ("D:\\UNI\\YearTwo\\Term2\\TeamProject\\anotherworld\\android\\assets\\UI\\MenuButtons\\playSoloButtonPressed.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        backGround = new Texture("D:\\UNI\\YearTwo\\Term2\\TeamProject\\anotherworld\\android\\assets\\UI\\MenuButtons\\menuBackground.png");
        audioOn = new Texture("D:\\UNI\\YearTwo\\Term2\\TeamProject\\anotherworld\\android\\assets\\UI\\audioOn.png");
        audioOff = new Texture("D:\\UNI\\YearTwo\\Term2\\TeamProject\\anotherworld\\android\\assets\\UI\\audioOff.png");
        bgm = Gdx.audio.newMusic(Gdx.files.internal("D:\\UNI\\YearTwo\\Term2\\TeamProject\\anotherworld\\android\\assets/sounds/menuBgm.mp3"));
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

        game.batch.draw(backGround,0,0);

        int x1 = MazeGame.WIDTH / 2 - PLAY_WIDTH / 2;
        if (Gdx.input.getX() < (x1 + PLAY_WIDTH) && Gdx.input.getX() > x1 && MazeGame.HEIGHT - Gdx.input.getY() > PLAY_Y && MazeGame.HEIGHT - Gdx.input.getY() < PLAY_Y + PLAY_HEIGHT) {
            game.batch.draw(playButtonActive, x1, PLAY_Y,PLAY_WIDTH,PLAY_HEIGHT);
            if (Gdx.input.isTouched()) {
                bgm.stop();
                game.setScreen(new GameScreen(game));
            }
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

        int x3 = MazeGame.WIDTH - AUDIO_WIDTH;
        if (Gdx.input.getX() < (x3 + AUDIO_WIDTH) && Gdx.input.getX() > x3 && MazeGame.HEIGHT - Gdx.input.getY() > AUDIO_Y && MazeGame.HEIGHT - Gdx.input.getY() < AUDIO_Y + AUDIO_HEIGHT)
            if (Gdx.input.isTouched()){
                if (bgm.isPlaying()) {
                    bgm.setLooping(false);
                    bgm.stop();
                } else {
                    bgm.setLooping(true);
                    bgm.play();
                }
        }

        if (bgm.isPlaying())
            game.batch.draw(audioOff, x3, AUDIO_Y,AUDIO_WIDTH,AUDIO_HEIGHT);
        else
            game.batch.draw(audioOn, x3, AUDIO_Y,AUDIO_WIDTH,AUDIO_HEIGHT);

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
