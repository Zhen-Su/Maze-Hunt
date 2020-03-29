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
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.CSVStuff;

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
    private Texture backGround ,startMazeButtonActive ,startMazeButtonInactive;
    private String hostPlayerName;
    private List<Player> players;
    private boolean hasReady = true;

    String username;
    String ip;
    
    public Texture playerRed,playerBlue,playerGreen,playerLilac,playerOrange,playerPink,playerYellow;
    private boolean ReadyPressed = false;
    
    private Texture playButtonPressed,playButton;

    public OtherLobbyScreen(MazeGame game,String username, String ip) {
        this.game = game;
        this.username = username;
        this.ip = ip;
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.setToOrtho(false, WIDTH, HEIGHT);
        
        playerRed = Assets.manager.get(Assets.playerRed);
        playerBlue  = Assets.manager.get(Assets.playerBlue);
        playerGreen  = Assets.manager.get(Assets.playerGreen);
        playerLilac  = Assets.manager.get(Assets.playerLilac);
        playerOrange = Assets.manager.get(Assets.playerOrange);
        playerPink = Assets.manager.get(Assets.playerPink);
        playerYellow = Assets.manager.get(Assets.playerYellow);
        playButton = Assets.manager.get(Assets.playButton,Texture.class);
        playButtonPressed = Assets.manager.get(Assets.playButtonPressed,Texture.class);
    
        
    }

    public MultiPlayerGameScreen getGameClient() { return gameClient; }

    public void setGameClient(MultiPlayerGameScreen gameClient) { this.gameClient = gameClient; }

    public String getHostPlayerName() { return hostPlayerName; }

    public void setHostPlayerName(String hostPlayerName) { this.hostPlayerName = hostPlayerName; }

    public List<Player> getPlayers() { return players; }

    public void setPlayers(List<Player> players) { this.players = players; }

    @Override
    public void show() {
    	backGround = Assets.manager.get(Assets.menuBackground, Texture.class);
    	font = Assets.manager.get(Assets.font, BitmapFont.class);
        
    	 font.setColor(Color.WHITE);
         font.getData().setScale(1f);
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
            
            game.batch.draw(Assets.manager.get(Assets.waitingForPlayers, Texture.class),0, 800);
//            font.draw(game.batch, "Here is Lobby...", 400, 950);
            font.draw(game.batch, "Ready Player:   ", 70, 750);

            handleInput();

            if (gameClient != null) {
                //draw the ready players on the screen
                int currY = 800;
                font.draw(game.batch, username, 230, 850);
                
                for (Player multiPlayer : players) {
                    font.draw(game.batch, multiPlayer.getName(), 230, currY);
                    
                    // Trying to display what colour each player has chosen next to their name
                    game.batch.draw(getColour(multiPlayer.getColour()), 850, currY - 50 , 120,160);
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
        
        if (isHovering(300,150, 400, 100)) {
            game.batch.draw(playButtonPressed,300,150, 400, 100);
            if (Gdx.input.justTouched())
            	ReadyPressed = true;
        }
        else {
            game.batch.draw(playButton,300,150, 400, 100);
        }
        
        game.batch.end();
    }
    
    private boolean isHovering(int X, int  Y, int WIDTH, int HEIGHT) {
        if (Gdx.input.getX() < (X + WIDTH) && Gdx.input.getX() > X && MazeGame.HEIGHT - Gdx.input.getY() > Y && MazeGame.HEIGHT - Gdx.input.getY() < Y + HEIGHT)
            return true;
        return false;
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
        if(ReadyPressed)
        {
            if(hasReady) {
                MultiPlayerGameScreen gameClient = new MultiPlayerGameScreen(game, username, ip,false);
                setGameClient(gameClient);
                //TODO Thread problem!!!!
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //TODO this need make more general, if second create maze, then host player is not the first in List
                //TODO if host player not ready, then will show an EXCEPTION!!!!!!
                setHostPlayerName(gameClient.getPlayers().get(0).getName());
                setPlayers(gameClient.getPlayers());
                hasReady = false;
                waitingStatus = true;
            }
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            backToMenuScreen();
            PlayerExitMessage message = new PlayerExitMessage(gameClient,gameClient.getMultiPlayer().getID());
            gameClient.getNc().send(message);
        }
    }

    private void backToMenuScreen(){
        System.out.println("back to");
        game.setScreen(new MenuScreen(this.game));
        System.out.println("shouldn't see");
    }
    
    private Texture getColour(String colour) {
  	  switch (colour) {
        case "blue":
        	return playerBlue;
            
        case "green":
      	  return playerGreen;
          
        case "pink":
      	  return playerPink;
          
        case "orange":
      	  return playerOrange;
            
        case "lilac":
      	  return playerLilac;
          
        case "yellow":
      	  return playerYellow;
       
        default:
      	  return playerRed;
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
        this.dispose();
    }

    @Override
    public void dispose() {
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
    }
}

