package com.project.mazegame.screens;

//import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
//import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;
import static com.project.mazegame.tools.Variables.V_HEIGHT;
import static com.project.mazegame.tools.Variables.V_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.OrthoCam;


public class MenuScreen implements Screen {

    private MazeGame game;

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

    private static final int LEADERBOARD_HEIGHT = 100;
    private static final int LEADERBOARD_WIDTH = 100;

    private static final int LEADERBOARD_Y = MazeGame.HEIGHT / 2 - 330;
    private static final int CREATE_Y = MazeGame.HEIGHT / 2 - 200;
    private static final int PLAY_Y = MazeGame.HEIGHT / 2 + 50;
    private static final int EXIT_Y = MazeGame.HEIGHT / 2 - 400;
    private static final int JOIN_Y = MazeGame.HEIGHT / 2 - 70;
    private static final int AUDIO_WIDTH = 100;
    private static final int AUDIO_HEIGHT = 80;
    private static final int AUDIO_Y = 0;
    private static final int AUDIO_SFX_Y = AUDIO_Y + 90;



    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture audioOn;
    Texture audioOff;
    Texture audioSFXOn;
    Texture audioSFXOff;
    Texture joinMazeButtonActive;
    Texture joinMazeButtonInactive;
    Texture backGround;
    Texture createMazeActive;
    Texture createMazeInactive;
    Texture mazeGame;
    Texture leaderBoardButton ,leaderBoardButtonPressed;


    Sprite playButtonActiveSprite;

    public MenuScreen(MazeGame game) {
        this.game = game;

        playButtonActive = Assets.manager.get(Assets.playSoloButton,Texture.class);
        playButtonInactive = Assets.manager.get(Assets.playSoloButtonPressed,Texture.class);
        exitButtonActive = Assets.manager.get(Assets.exit_button_active,Texture.class);
        exitButtonInactive = Assets.manager.get(Assets.exit_button_inactive,Texture.class);
        joinMazeButtonActive = Assets.manager.get(Assets.FindMazeButton,Texture.class);
        joinMazeButtonInactive = Assets.manager.get(Assets.FindMazeButtonPressed,Texture.class);
        createMazeActive = Assets.manager.get(Assets.StartNewMazeButton,Texture.class);
        createMazeInactive = Assets.manager.get(Assets.StartNewMazeButtonPressed,Texture.class);
        mazeGame = Assets.manager.get(Assets.MazeHunt,Texture.class);
        audioOn = Assets.manager.get(Assets.audioOn,Texture.class);
        audioOff = Assets.manager.get(Assets.audioOff,Texture.class);
        audioSFXOn = Assets.manager.get(Assets.audioSFXOn, Texture.class);
        audioSFXOff = Assets.manager.get(Assets.audioSFXOff, Texture.class);
        backGround = Assets.manager.get(Assets.menuBackground,Texture.class);
        leaderBoardButton = Assets.manager.get(Assets.LeaderboardButton,Texture.class);
        leaderBoardButtonPressed = Assets.manager.get(Assets.LeaderboardButtonPressed,Texture.class);

        if (game.audio.isMusicOn()) {
            game.audio.setMusicOff();
            game.audio.setCurrentScreen("menu");
            game.audio.setMusicOn();
        } else {
            game.audio.setMusicOff();
        }

    }

    @Override
    public void show() {
//    	cam = new OrthoCam(game,false, V_WIDTH, V_HEIGHT, V_WIDTH/2,V_HEIGHT/2);  
    }

    private void playSound(boolean player){
        if(player) {
            game.audio.choose();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.325f, 0.343f, 0.604f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //cam.update(500, 500, game);
        //rendering
        game.batch.begin();

        game.batch.draw(backGround,0,0,1000,1000);
        game.batch.draw(mazeGame,25, 650, 950, 325);
        int drawX = xMid("MB");
        if (isHovering(drawX, PLAY_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(playButtonActive, drawX, PLAY_Y,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.isTouched()) {
                game.setScreen(new CreateMazeScreen(game , false));
                playSound(true);
            }
        } else {

            game.batch.draw(playButtonInactive, drawX, PLAY_Y,MB_WIDTH, MB_HEIGHT);
        }


        if (isHovering(drawX, JOIN_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(joinMazeButtonActive, drawX, JOIN_Y,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.justTouched()) {
                this.dispose();
                game.setScreen(new JoinMazeScreen(game));
                playSound(true);
            }
        } else {
            game.batch.draw(joinMazeButtonInactive, drawX, JOIN_Y,MB_WIDTH, MB_HEIGHT);
        }

        drawX = xMid("MB");
        if (isHovering(drawX, LEADERBOARD_Y  , MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(leaderBoardButton, drawX, LEADERBOARD_Y ,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.justTouched())
                try{
                    game.setScreen(new LeaderboardScreen(game));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        else {
            game.batch.draw(leaderBoardButtonPressed, drawX, LEADERBOARD_Y,MB_WIDTH, MB_HEIGHT);
        }

        if (isHovering(drawX, CREATE_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(createMazeActive, drawX, CREATE_Y,MB_WIDTH, MB_HEIGHT);
            if(Gdx.input.justTouched())
            {
                game.setScreen(new CreateMazeScreen(game , true));
                playSound(true);
            }
        } else {
            game.batch.draw(createMazeInactive, drawX, CREATE_Y,MB_WIDTH, MB_HEIGHT);
        }

        drawX = xMid("SB");
        if (isHovering(drawX, EXIT_Y, SB_WIDTH, SB_HEIGHT)) {
            game.audio.choose();
            game.batch.draw(exitButtonActive, drawX, EXIT_Y,SB_WIDTH, SB_HEIGHT);
            if (Gdx.input.justTouched()) {
                Gdx.app.exit();
                playSound(true);
            }
        }
        else {
            game.audio.buttonPlayed = false;
            game.batch.draw(exitButtonInactive, drawX, EXIT_Y,SB_WIDTH, SB_HEIGHT);
        }

        drawX = xMid("AudioButton") - MazeGame.WIDTH + AUDIO_WIDTH;
        if (isHovering(drawX, AUDIO_Y, AUDIO_WIDTH, AUDIO_HEIGHT)) {
            if (Gdx.input.justTouched()) {
                if (game.audio.isMusicOn()) {
                    game.audio.setMusicOff();
                } else {
                    game.audio.setMusicOn();
                }
            }
        }

        drawX = xMid("AudioButton") - MazeGame.WIDTH + AUDIO_WIDTH;
        if (isHovering(drawX, AUDIO_SFX_Y, AUDIO_WIDTH, AUDIO_HEIGHT)) {
            if (Gdx.input.justTouched()) {
                if (game.audio.isSFXOn()) {
                    game.audio.setSFXOff();
                } else {
                    game.audio.setSFXOn();
                }
            }
        }

        if (game.audio.isMusicOn())
            game.batch.draw(audioOff, drawX, AUDIO_Y,AUDIO_WIDTH,AUDIO_HEIGHT);
        else
            game.batch.draw(audioOn, drawX, AUDIO_Y,AUDIO_WIDTH,AUDIO_HEIGHT);

        if (game.audio.isSFXOn())
            game.batch.draw(audioSFXOff, drawX, AUDIO_SFX_Y,AUDIO_WIDTH,AUDIO_HEIGHT);
        else
            game.batch.draw(audioSFXOn, drawX, AUDIO_SFX_Y,AUDIO_WIDTH,AUDIO_HEIGHT);

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
        playButtonActive.dispose();
        playButtonInactive.dispose();
        audioOn.dispose();
        audioOff.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        joinMazeButtonActive.dispose();
        joinMazeButtonInactive.dispose();

    }
}
