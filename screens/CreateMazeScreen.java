package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class CreateMazeScreen implements Screen {

    private MazeGame game;
    public String username;
    private boolean hasEnterUsername =false;
    
    
    private static final int LB_WIDTH = 350;
    private static final int LB_HEIGHT = 200;

    private static final int MB_WIDTH = 250;
    private static final int MB_HEIGHT = 100;

    private static final int SB_HEIGHT = 80;
    private static final int SB_WIDTH = 50;

    private static final int CREATE_HEIGHT = 100;
    private static final int CREATE_WIDTH = 100;
    
    private int size = 100;

    private static final int CREATE_Y = MazeGame.HEIGHT / 2 - 200;
    private static final int PLAY_Y = MazeGame.HEIGHT / 2 + 50;
    private static final int EXIT_Y = MazeGame.HEIGHT / 2 - 300;
    private static  final int JOIN_Y = MazeGame.HEIGHT / 2 - 70;
    private static final int AUDIO_WIDTH = 100;
    private static final int AUDIO_HEIGHT = 50;
    private static final int AUDIO_Y = MazeGame.HEIGHT - AUDIO_HEIGHT;
    
    
    private static int AI_Y =  MazeGame.HEIGHT -300;
    private static int DRAW_X = 350;
    private static int PLAYER_Y = AI_Y - 150;
    private static int PLAYER2_Y = PLAYER_Y - 150;
    private static int MAP_Y = PLAYER2_Y -  150;
    
    
    Texture joinMazeButtonActive;
    Texture joinMazeButtonInactive;
    Texture backGround;
    Texture Map1,Map2,Map3, Map1Selected,Map2Selected,Map3Selected ;
    
    Texture player1, player2, player3, player4, player5, player6, player7,player1Selected, player2Selected;
    
    Button player1Button,player2Button,player3Button,player4Button,player5Button,player6Button,player7Button;
    Button AIButton,AIDifficultyButton,AIDifficulty2Button,AIDifficulty3Button;
    Button Map1Button,Map2Button,Map3Button;
    Button CreateMaze;
    BitmapFont font;

    public CreateMazeScreen(MazeGame game) {
        this.game = game;
       // this.playerEnterUsername();
        
        joinMazeButtonInactive = new Texture("UI\\MenuButtons\\FindMazeButton.png");
        joinMazeButtonActive = new Texture("UI\\MenuButtons\\FindMazeButtonPressed.png");
        backGround = new Texture("UI\\Backgrounds\\menuBackground.png");
        
        player1 = new Texture("Player\\PlayerRedFrontCrop.png");
        player1Selected = new Texture("Player\\Player1Selected.png");
        player2Selected = new Texture("Player\\PlayerBlueFrontSelected.png");
        player2 = new Texture("Player\\PlayerBlueFront.png");
        player3 = new Texture("Player\\PlayerRedFrontCrop.png");
        player4 = new Texture("Player\\PlayerRedFrontCrop.png");
        player5 = new Texture("Player\\PlayerRedFrontCrop.png");
        player6 = new Texture("Player\\PlayerRedFrontCrop.png");
        player7 = new Texture("Player\\PlayerRedFrontCrop.png");
        Map1 = new Texture("Maps\\Map1Icon.png");
        Map2 = new Texture("Maps\\Map2Icon.png");
        Map3 = new Texture("Maps\\Map3Icon.png");
        Map1Selected = new Texture("Maps\\Map1IconSelected.png");
        Map2Selected = new Texture("Maps\\Map2IconSelected.png");
        Map3Selected = new Texture("Maps\\Map3IconSelected.png");
        
    }

    private void playerEnterUsername (){
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                Gdx.app.log("Username:",text);
                username=text;
                hasEnterUsername=true;
            }
            @Override
            public void canceled() {
                Gdx.app.log("MutiPlayerScreen: ","Player cancel,GOODBYE!");

            }
        },"ENTER USERNAME", "", "Your username Please");
    }


    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
    
    	int buf = 110;

    	int drawX = DRAW_X;
    	AIButton = new Button(drawX,AI_Y, size,size, player1, joinMazeButtonInactive );
    	AIDifficultyButton = new Button(drawX  ,AI_Y, size,size, player1, joinMazeButtonInactive );
    	AIDifficulty2Button = new Button(drawX +110 ,AI_Y, size,size, player1, joinMazeButtonInactive );
    	AIDifficulty3Button = new Button(drawX + 110 ,AI_Y, size,size, player1, joinMazeButtonInactive );
    	
    	drawX = DRAW_X;
    	player1Button = new Button(drawX,PLAYER_Y, size,size, player1Selected, player1 );
    	drawX += buf;
    	player2Button = new Button(drawX,PLAYER_Y, size,size, player2Selected, player2 );
    	drawX += buf;
    	player3Button = new Button(drawX,PLAYER_Y, size,size, player1Selected, player3 );
    	drawX = DRAW_X;
    	player4Button = new Button(drawX,PLAYER2_Y, size,size, player1Selected, player4 );
    	drawX += buf;
    	player5Button = new Button(drawX,PLAYER2_Y, size,size, player1Selected, player5 );
    	drawX += buf;
    	player6Button = new Button(drawX,PLAYER2_Y, size,size, player1Selected, player6 );
    	drawX += buf;
    	player7Button = new Button(drawX,PLAYER2_Y, size,size, player1Selected, player7 );
    	
    	drawX = DRAW_X;
    	Map1Button = new Button(drawX,MAP_Y, size,size, Map1Selected, Map1 );
    	drawX += buf;
    	Map2Button = new Button(drawX,MAP_Y, size,size, Map2Selected, Map2 );
    	drawX += buf;
    	Map3Button = new Button(drawX,MAP_Y, size,size, Map3Selected, Map3 );
    	
    	
    	CreateMaze = new Button(DRAW_X ,MAP_Y - 110, size,size,joinMazeButtonActive,joinMazeButtonInactive);
    }

    @Override
    public void render(float delta) {
    	
    	game.batch.begin();
    	game.batch.draw(backGround,0,0,1000,1000);

    	player1Button.draw();
    	player2Button.draw();
    	player3Button.draw();
    	player4Button.draw();
    	player5Button.draw();
    	player6Button.draw();
    	player7Button.draw();
    	 
    	AIButton.draw();
    	AIDifficultyButton.draw();
    	
    	Map1Button.draw();
    	Map2Button.draw();
    	Map3Button.draw();
    	
    	String message = "Create New Maze" ;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        font.draw(game.batch,message, DRAW_X  , MazeGame.HEIGHT -100);
          
        
        message = "Select AI" ;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        font.draw(game.batch,message, DRAW_X - 300 ,AI_Y + 50);
        
        message = "Difficulty" ;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        font.draw(game.batch,message, DRAW_X ,AI_Y + 50);
        
        message = "Select Player" ;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        font.draw(game.batch,message, DRAW_X - 300 ,PLAYER_Y + 50);
        
        message = "Select Map" ;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        font.draw(game.batch,message, DRAW_X - 300 ,MAP_Y + 50);
    	 
        CreateMaze.draw();
        /*if(hasEnterUsername){
            try {
                Gdx.app.log("Server","I'm a server!");
                Gdx.app.log("Server IP:", InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            new Thread(new GameServer()).start();

            game.setScreen(new MultiPlayerGameScreen(game,username,"127.0.0.1"));

        }*/
    	 
    	 game.batch.end();
    }
 
    
    private class Button {
    	int x, y,width,height , activeWidth,activeHeight;
    	
    	Texture active,inactive,texture;
    	boolean isPressed;
    	private Button (int x,int y, int w, int h , Texture active, Texture inactive ) {
    		this.x = x;
    		this.y = y;
    		this.width = w;
    		this.height = h;
    		this.active = active;
    		this.inactive = inactive;
    		
    		texture = this.inactive;
    		
    		activeWidth = this.width + 10;
    		activeHeight = this.height + 10;
    		
    	}
    	
    	public void draw() {
    		
    		//if(this.isPressed) System.out.println("pressed ");
    		
    		if (isButtonHovering()) {
    			
    			
    			if (Gdx.input.justTouched() && !isPressed) {
    				this.isPressed = true;
    			}else if (Gdx.input.justTouched() && isPressed) {
    				this.isPressed = false;
    			}
    		}
    		

//    		if(this.isPressed) System.out.println("pressed ");
    		
    		if(isPressed) 
    			this.texture = this.active;
    		else if (isButtonHovering()) {
    			this.texture = this.active;
    		}else {
    			this.texture = this.inactive;
    		}
    			
    		game.batch.draw(this.texture, this.x, this.y,this.width,this.height);
    		
    	}
    	
    	public boolean isButtonHovering() {
    		
    		return isHovering(this.x,this.y,this.width,this.height);
    	}
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
        this.dispose();
    }

    @Override
    public void dispose() {
    	font.dispose();
    }
}


