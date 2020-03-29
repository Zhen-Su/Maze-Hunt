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
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.CSVStuff;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class HostLobbyScreen implements Screen {


    private BitmapFont bitmapFont;
    private MazeGame game;
    private GameServer gameServer;
    private String hostUsername;
    private MultiPlayerGameScreen gameClient;
    private BitmapFont font ;
    private Texture backGround ,startMazeButtonActive,startMazeButtonInactive;
    public Texture playerRed,playerBlue,playerGreen,playerLilac,playerOrange,playerPink,playerYellow;
    private OrthographicCamera cam;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    private String hostColour;
    private boolean StartPressed = false;

    public HostLobbyScreen(MazeGame game,String username) {
        this.game=game;
        this.hostUsername=username;
        GameServer gameServer=new GameServer();
        this.gameServer = gameServer;
        new Thread(gameServer).start();
        try {
            MultiPlayerGameScreen gameClient = new MultiPlayerGameScreen(game,hostUsername, InetAddress.getLocalHost().getHostAddress(),true);
            this.gameClient=gameClient;
            this.gameClient.setServer(gameServer);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        cam = new OrthographicCamera(WIDTH, HEIGHT);
        cam.setToOrtho(false, WIDTH, HEIGHT);
        
        
        playerRed = Assets.manager.get(Assets.playerRed);
        playerBlue  = Assets.manager.get(Assets.playerBlue);
        playerGreen  = Assets.manager.get(Assets.playerGreen);
        playerLilac  = Assets.manager.get(Assets.playerLilac);
        playerOrange = Assets.manager.get(Assets.playerOrange);
        playerPink = Assets.manager.get(Assets.playerPink);
        playerYellow = Assets.manager.get(Assets.playerYellow);
        
        
        ArrayList<String> output = CSVStuff.readCSVFile("csvFile");
        
        this.hostColour = output.get(1);
        
        startMazeButtonActive = Assets.manager.get(Assets.StartNewMazeButtonPressed,Texture.class);
        startMazeButtonInactive = Assets.manager.get(Assets.StartNewMazeButton,Texture.class);
    }

    @Override
    public void show() {
        backGround = Assets.manager.get(Assets.menuBackground, Texture.class);
        font = Assets.manager.get(Assets.font, BitmapFont.class);
//        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1f);
        
     
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
            game.batch.draw(Assets.manager.get(Assets.waitingForPlayers, Texture.class),0, 800);
//            font.draw(game.batch, "Here is Host Lobby...", 400, 950);
            font.draw(game.batch, "Host Player:   " ,70 , 700);
            
            font.draw(game.batch, hostUsername, 500, 700);
           
            game.batch.draw(getColour(this.hostColour),850, 650, 120,160);
            
            
            font.draw(game.batch, "Ready Players:   ", 70, 600);

            int currY = 500;
            for (Player multiPlayer : gameClient.getPlayers()) {
                font.draw(game.batch, multiPlayer.getName(), 230, currY);
                
                
                // Trying to display what colour each player has chosen next to their name
                game.batch.draw(getColour(multiPlayer.getColour()), 850, currY - 50 , 120,160);
                
            }
                
                currY -= 50;
        }
        
        
        
        if (isHovering(300,150, 400, 100)) {
            game.batch.draw(startMazeButtonActive,300,150, 400, 100);
            if (Gdx.input.justTouched())
                StartPressed = true;
        }
        else {
            game.batch.draw(startMazeButtonInactive,300,150, 400, 100);
        }
        
        game.batch.end();
    }
    
    private boolean isHovering(int X, int  Y, int WIDTH, int HEIGHT) {
        if (Gdx.input.getX() < (X + WIDTH) && Gdx.input.getX() > X && MazeGame.HEIGHT - Gdx.input.getY() > Y && MazeGame.HEIGHT - Gdx.input.getY() < Y + HEIGHT)
            return true;
        return false;
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

    private void handleInput(){
        if(StartPressed) {
            StartGameMessage start = new StartGameMessage(gameClient,true,gameClient.getMultiPlayer().getID());
            gameClient.getNc().send(start);
            gameClient.setImHost(true);
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
