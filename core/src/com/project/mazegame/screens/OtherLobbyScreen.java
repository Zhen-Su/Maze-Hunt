package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.MultiPlayer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * This looby used by other player who click “join maze"
 */
public class OtherLobbyScreen implements Screen {
    private MazeGame game;
    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;
    Stage stage;

    private Label label;
    private BitmapFont bitmapFont;

    String username;
    String ip;

    public OtherLobbyScreen(MazeGame game,String username, String ip) {
        this.game = game;
        this.username = username;
        this.ip = ip;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));

        bitmapFont = new BitmapFont(Gdx.files.internal("bitmap.fnt"));

        Label.LabelStyle style = new Label.LabelStyle();

        style.font = bitmapFont;

        style.fontColor = new Color(1, 0, 0, 1);

        label = new Label("Waiting in Lobby...", style);

        label.setPosition(100, 500);

        label.setFontScale(1.5f);

        stage.addActor(label);

        Label label1 = new Label("Press Enter to ready ...", style);

        label1.setPosition(100, 400);

        label1.setFontScale(1f);

        stage.addActor(label1);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            MultiPlayerGameScreen gameClient = new MultiPlayerGameScreen(game,username,ip);

        }else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            backToMenuScreen();
        }
        stage.act();
        stage.draw();
    }

    private void backToMenuScreen(){
        System.out.println("back to");
        // cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
        //this.dispose();
        game.setScreen(new MenuScreen(this.game));
        System.out.println("shouldn't see");
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
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}

