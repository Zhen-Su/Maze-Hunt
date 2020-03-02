package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class CreateMazeScreen implements Screen {

    private MazeGame game;
    private String username;
    private int mapOption = 1; //either 1 2 or three
    private boolean usernameEntered = false;

    private boolean hasEnterUsername =false;
    private static final int CREATE_Y = MazeGame.HEIGHT / 2 - 400;
    private static final int BLANK_1_Y = MazeGame.HEIGHT / 2 - 100;
    private static final int AUDIO_WIDTH = 100;
    private static final int AUDIO_HEIGHT = 50;
    private static final int LB_WIDTH = 350;
    private static final int LB_HEIGHT = 200;

    private static final int MB_WIDTH = 250;
    private static final int MB_HEIGHT = 100;

    private static final int SB_HEIGHT = 80;
    private static final int SB_WIDTH = 50;

    private static final int AUDIO_Y = MazeGame.HEIGHT - AUDIO_HEIGHT;
    Texture OK_BUTTON;

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    public static final int TEXT_FIELD_WIDTH = 300;
    public static final int TEXT_FIELD_HEIGHT = 50;

    Texture backGround;
    Texture joinMazeButtonActive;
    Texture joinMazeButtonInactive;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture audioOn;
    Texture audioOff;
    Texture createMazeActive;
    Texture createMazeInactive;
    Texture usernameInput;
    TextField usernameTextField;

    Stage stage;
    Skin skin;

    private BitmapFont font = new BitmapFont();


    public CreateMazeScreen(MazeGame game) {

        this.game = game;
        OK_BUTTON = new Texture("UI\\MenuButtons\\FindMazeButton.png");
        backGround = new Texture("UI\\MenuButtons\\menuBackground.png");
        joinMazeButtonActive = new Texture("UI\\MenuButtons\\FindMazeButton.png");
        joinMazeButtonInactive = new Texture("UI\\MenuButtons\\FindMazeButtonPressed.png");
        usernameInput = new Texture("button.png");

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("arcade/skin/arcade-ui.json"));

//        usernameTextField = new TextField("", font);
//        usernameTextField.setPosition(24,73);
//        usernameTextField.setSize(88, 14);
//        stage.addActor(usernameTextField);            // <-- Actor now on stage
        Gdx.input.setInputProcessor(stage);

        createMazeActive = new Texture("UI\\MenuButtons\\StartNewMazeButton.png");
        createMazeInactive = new Texture("UI\\MenuButtons\\StartNewMazeButtonPressed.png");

    }




    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.325f, 0.343f, 0.604f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(backGround,0,0,1000,1000);

        int drawX = xMid("MB");

        if (isHovering(drawX, BLANK_1_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(usernameInput, drawX, BLANK_1_Y,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.isTouched()) {
               // prompts user to input name
               // enter user input
                // store user input as username string
            }
        } else {
            game.batch.draw(usernameInput, drawX, BLANK_1_Y,MB_WIDTH, MB_HEIGHT);
        }

        // selection of maps
        // if selected, background -> light color

        if (isHovering(drawX, CREATE_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(createMazeActive, drawX, CREATE_Y,MB_WIDTH, MB_HEIGHT);
            //bring to maze creation thing
            if(Gdx.input.isTouched())
            {
                if (hasEnterUsername) {
                    clickConfirm();
                } else {

                }
            }
        } else {
            game.batch.draw(createMazeInactive, drawX, CREATE_Y,MB_WIDTH, MB_HEIGHT);
        }



        game.batch.end();
    }

    private void clickConfirm() {
        if(usernameEntered){ // problem here , render is called again and again, so multiple threads are made
            new Thread(new GameServer()).start();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new LobbyScreen(game,username));
                }
            });
        } else {
            // prompt username to continue
        }
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

    private boolean isHovering(int X, int  Y, int WIDTH, int HEIGHT) {
        if (Gdx.input.getX() < (X + WIDTH) && Gdx.input.getX() > X && MazeGame.HEIGHT - Gdx.input.getY() > Y && MazeGame.HEIGHT - Gdx.input.getY() < Y + HEIGHT)
            return true;
        return false;
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
        this.dispose();
    }

    @Override
    public void dispose() {

    }
}


