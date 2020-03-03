package com.project.mazegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.project.mazegame.MazeGame;


public class JoinMazeScreen implements Screen {


    MazeGame game;

    public JoinMazeScreen(MazeGame game) {
        this.game = game;

    }
    private static final String TAG = JoinMazeScreen.class.getSimpleName();

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    public static final int TEXT_FIELD_WIDTH = 300;
    public static final int TEXT_FIELD_HEIGHT = 50;

    private Stage stage;

    private Texture bgTexture;

    private Texture cursorTexture;

    private BitmapFont bitmapFont;

    private TextField usernameTextField;

    private TextField ipAddressTextField;

    Texture backGround;

    @Override
    public void show() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));

        Gdx.input.setInputProcessor(stage);


        bgTexture = createBackgroundTexture();
        cursorTexture = createCursorTexture();

        bitmapFont = new BitmapFont();

        bitmapFont.getData().setScale(2.0F);


        TextField.TextFieldStyle style = new TextField.TextFieldStyle();

        style.background = new TextureRegionDrawable(new TextureRegion(bgTexture));

        style.cursor = new TextureRegionDrawable(new TextureRegion(cursorTexture));

        style.font = bitmapFont;

        style.fontColor = new Color(1, 1, 1, 1);

        usernameTextField = new TextField("", style);
        ipAddressTextField = new TextField("", style);

        usernameTextField.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        ipAddressTextField.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        usernameTextField.setPosition(90, 500);
        ipAddressTextField.setPosition(90, 430);

        usernameTextField.setAlignment(Align.center);
        ipAddressTextField.setAlignment(Align.center);


        stage.addActor(usernameTextField);
        stage.addActor(ipAddressTextField);
    }

    private Texture createBackgroundTexture() {
        Pixmap pixmap = new Pixmap(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }


    private Texture createCursorTexture() {
        Pixmap pixmap = new Pixmap(1, TEXT_FIELD_HEIGHT - 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render(float delta) {


        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            Gdx.app.log(TAG, "username = " + usernameTextField.getText());
            Gdx.app.log(TAG, "ipaddress = " + ipAddressTextField.getText());
            //game.setScreen(new MultiPlayerGameScreen(game,usernameTextField.getText(),ipAddressTextField.getText()));
            game.setScreen(new OtherLobbyScreen(game,usernameTextField.getText(),ipAddressTextField.getText()));
        }
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
        {
            backToMenuScreen();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        if (cursorTexture != null) {
            cursorTexture.dispose();
        }
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

    private void backToMenuScreen(){
        System.out.println("back to");
        // cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
        //this.dispose();
        game.setScreen(new MenuScreen(this.game));
        System.out.println("shouldn't see");
    }


}