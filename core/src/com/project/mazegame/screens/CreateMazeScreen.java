package com.project.mazegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.project.mazegame.MazeGame;


public class CreateMazeScreen implements Screen {

    private MazeGame game;
    public String username;
    private boolean hasEnterUsername =false;
    private HostLobbyScreen hostlobby;

    private static final String TAG = JoinMazeScreen.class.getSimpleName();


    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    public static final int TEXT_FIELD_WIDTH = 300;
    public static final int TEXT_FIELD_HEIGHT = 50;

    private Stage stage;

    private Texture bgTexture;

    private Texture cursorTexture;

    private BitmapFont bitmapFont;

    private BitmapFont font;

    private TextField usernameTextField;

    private TextField numField;


    Texture backGround = new Texture("UI\\Backgrounds\\menuBackground.png");

    public CreateMazeScreen(MazeGame game) {
        this.game = game;
    }


    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
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

        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("bitmap.fnt"));
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(2f);

        usernameTextField = new TextField("Player1", style);
        numField = new TextField("Num of AI Players", style);

        usernameTextField.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        numField.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        usernameTextField.setPosition(90, 500);
        numField.setPosition(90, 430);

        usernameTextField.setAlignment(Align.center);
        numField.setAlignment(Align.center);

        stage.addActor(usernameTextField);
        stage.addActor(numField);
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(backGround,0,0,1000,1000);

        font.draw(game.batch, "Press enter to enter Host lobby ", 350, 480);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            Gdx.app.log(TAG, "username = " + usernameTextField.getText());
            Gdx.app.log(TAG, "number of AI players = " + numField.getText());
            game.setScreen(new HostLobbyScreen(game,usernameTextField.getText()));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            backToMenuScreen();
        }

        stage.act();
        game.batch.end();
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
        this.dispose();
    }

    @Override
    public void dispose() {


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

    private void backToMenuScreen(){
        System.out.println("back to");
        // cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
        //this.dispose();
        game.setScreen(new MenuScreen(this.game));
        System.out.println("shouldn't see");
    }
}


