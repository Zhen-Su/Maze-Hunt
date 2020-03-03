package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.MultiPlayer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostLobbyScreen implements Screen {

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    private Stage stage;
    private Label label;
    private Label label1;
    private BitmapFont bitmapFont;
    private MazeGame game;
    private GameServer gameServer;
    private String hostUsername;
    private MultiPlayerGameScreen gameClient;
    private Label.LabelStyle style;

    public HostLobbyScreen(MazeGame game,String username) {
        this.game=game;
        this.hostUsername=username;
        GameServer gameServer=new GameServer();
        this.gameServer = gameServer;
        new Thread(gameServer).start();
        try {
            MultiPlayerGameScreen gameClient = new MultiPlayerGameScreen(game,hostUsername, InetAddress.getLocalHost().getHostAddress());
            this.gameClient=gameClient;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void show() {

        stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));

        bitmapFont = new BitmapFont(Gdx.files.internal("bitmap.fnt"));

        style = new Label.LabelStyle();

        style.font = bitmapFont;

        style.fontColor = new Color(1, 0, 0, 1);

        label = new Label("Here is Host Lobby...", style);
        label.setPosition(20, 750);
        label.setFontScale(1.5f);
        stage.addActor(label);

        label1 = new Label(hostUsername , style);
        label1.setPosition(20, 650);
        label1.setFontScale(1f);
        stage.addActor(label1);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            //game.setScreen(new MultiPlayerGameScreen(game,username,ip));

        }else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            backToMenuScreen();
            disposeServer();
        }

        if(gameClient.getPlayers().size()==1){
            MultiPlayer multiPlayer = gameClient.getPlayers().get(0);
            Label label2 = new Label(multiPlayer.getName(), style);
            label2.setPosition(20, 550);
            label2.setFontScale(1f);
            stage.addActor(label2);
        }
        if(gameClient.getPlayers().size()==2){
            MultiPlayer multiPlayer = gameClient.getPlayers().get(1);
            Label label3 = new Label(multiPlayer.getName(), style);
            label3.setPosition(20, 450);
            label3.setFontScale(1f);
            stage.addActor(label3);
        }

       // System.out.println("Player number: "+gameClient.getPlayers().size());

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

    //TODO handle player exit lobby event here
    private void disposeServer(){
        gameServer.dispose();
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
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

}