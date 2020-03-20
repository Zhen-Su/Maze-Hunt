
package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Messagess.PlayerExitMessage;
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
 * This lobby used by other player who click “join maze"
 */
public class OtherLobbyScreen implements Screen {
    private MazeGame game;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;

    public boolean waitingStatus=false ;


    private OrthographicCamera cam;
    private BitmapFont bitmapFont;
    private BitmapFont font;
    private MultiPlayerGameScreen gameClient;
    private Texture backGround;
    private String hostPlayerName;
    private List<MultiPlayer> players;
    private boolean hasReady = true;

    String username;
    String ip;


    public OtherLobbyScreen(MazeGame game,String username, String ip) {
        this.game = game;
        this.username = username;
        this.ip = ip;
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.setToOrtho(false, WIDTH, HEIGHT);
    }

    public MultiPlayerGameScreen getGameClient() { return gameClient; }

    public void setGameClient(MultiPlayerGameScreen gameClient) { this.gameClient = gameClient; }

    public String getHostPlayerName() { return hostPlayerName; }

    public void setHostPlayerName(String hostPlayerName) { this.hostPlayerName = hostPlayerName; }

    public List<MultiPlayer> getPlayers() { return players; }

    public void setPlayers(List<MultiPlayer> players) { this.players = players; }

    @Override
    public void show() {
        backGround = new Texture("UI\\Backgrounds\\menuBackground.png");
        bitmapFont = new BitmapFont(Gdx.files.internal("myFont.fnt"));
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(1.5f);
    }

    @Override
    public void render(float delta) {
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        {
            game.batch.draw(backGround, 0, 0, 1000, 1000);
            font.draw(game.batch, "Here is Lobby...", 400, 950);
            font.draw(game.batch, "Ready Player:   ", 70, 850);

            handleInput();

            if (gameClient != null) {
                //draw the ready players on the screen
                int currY = 800;
                font.draw(game.batch, username, 230, 850);
                for (MultiPlayer multiPlayer : players) {
                    font.draw(game.batch, multiPlayer.getName(), 230, currY);
                    currY -= 50;
                }
                //check the host player if quit the game.
                checkHostLeave();
            }

            //check whether stare game...
            if(waitingStatus && gameClient.isHostStartGame())
            {
                game.setScreen(gameClient);
            }
        }
        game.batch.end();
    }

    private void checkHostLeave(){
        if(players.isEmpty()) {
            font.setColor(Color.YELLOW);
            font.draw(game.batch, "The host player has quit! Please back to Menu Screen...", 240, 500);
        }
        if (!players.isEmpty() && players.get(0).getName() != hostPlayerName) {
            font.draw(game.batch, "The host player has quit! Please back to Menu Screen...", 240, 500);
        }
    }

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            if(hasReady) {
                MultiPlayerGameScreen gameClient = new MultiPlayerGameScreen(game, username, ip);
                setGameClient(gameClient);
                setHostPlayerName(gameClient.getPlayers().get(0).getName());
                setPlayers(gameClient.getPlayers());
                hasReady = false;
                waitingStatus = true;
            }
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            backToMenuScreen();
            PlayerExitMessage message = new PlayerExitMessage(gameClient,gameClient.getMultiPlayer().getId());
            gameClient.getNc().send(message);
        }
    }

    private void backToMenuScreen(){
        System.out.println("back to");
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
    }
}


