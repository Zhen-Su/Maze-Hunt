package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Messagess.StartGameMessage;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.MultiPlayer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostLobbyScreen implements Screen {


    private BitmapFont bitmapFont;
    private MazeGame game;
    private GameServer gameServer;
    private String hostUsername;
    private MultiPlayerGameScreen gameClient;
    private BitmapFont font;
    private Texture backGround;
    private OrthographicCamera cam;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;


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
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.setToOrtho(false, WIDTH, HEIGHT);
    }

    @Override
    public void show() {
        backGround = new Texture("UI\\menuBackground.png");
        bitmapFont = new BitmapFont(Gdx.files.internal("bitmap.fnt"));
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(1.5f);
    }

    @Override
    public void render(float delta) {
        handleInput();
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        {
            game.batch.draw(backGround, 0, 0, 1000, 1000);
            font.draw(game.batch, "Here is Host Lobby...", 400, 950);
            font.draw(game.batch, "Host Player:   " + hostUsername, 70, 900);
            font.draw(game.batch, "Ready Player:   ", 70, 850);

            int currY = 850;
            for (MultiPlayer multiPlayer : gameClient.getPlayers()) {
                font.draw(game.batch, multiPlayer.getName(), 230, currY);
                currY -= 50;
            }
        }
        game.batch.end();
    }

    private void handleInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            StartGameMessage start = new StartGameMessage(gameClient,true,gameClient.getMultiPlayer().getId());
            gameClient.getNc().send(start);
            //gameClient.generateMapItems(gameClient.getCollisionLayer().getWidth(),100 );
            game.setScreen(gameClient);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            backToMenuScreen();
            disposeServer();
        }
    }

    private void backToMenuScreen(){
        System.out.println("back to menu screen");
        game.setScreen(new MenuScreen(this.game));
    }

    //TODO handle player exit lobby event here
    private void disposeServer(){
        gameServer.dispose(gameClient);
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

    }

}
