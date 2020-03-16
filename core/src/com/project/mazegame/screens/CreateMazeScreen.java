package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.tools.CSVStuff;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.project.mazegame.tools.Variables.*;

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
    Texture diffButton1,diffButton1Selected,diffButton2,diffButton2Selected,diffButton3,diffButton3Selected;
    Texture backGround;
    Texture Map1,Map2,Map3, Map1Selected,Map2Selected,Map3Selected ;
    
    Texture player1, player2, player3, player4, player5, player6, playerSelected , player7;
    
    TextureRegion txture;
    TextureRegion[][] playerRegion;
    Button player1Button,player2Button,player3Button,player4Button,player5Button,player6Button,player7Button;
    Button[] playerButtons ;
   
    Button AIButton,AIDifficulty1Button,AIDifficulty2Button,AIDifficulty3Button;
    Button[] difficultyButtons;

    Button Map1Button,Map2Button,Map3Button;
    Button[] mapButtons;
    Button CreateMaze;
    BitmapFont font;
    boolean playerChosen;
    boolean mapChosen;
    boolean difficultyChosen;
    

    public CreateMazeScreen(MazeGame game) {
        this.game = game;
       // this.playerEnterUsername();
        
        joinMazeButtonInactive = new Texture("UI\\MenuButtons\\button.png");
        joinMazeButtonActive = new Texture("UI\\MenuButtons\\buttonPressed.png");
        backGround = new Texture("UI\\Backgrounds\\menuBackground.png");
        playerSelected = new Texture("Player\\playerSelected.png");
        player1 = new Texture("Player\\playerRed.png");
        player2 = new Texture("Player\\playerBlue.png");
        player3 = new Texture("Player\\playerGreen.png");
        player4 = new Texture("Player\\playerLilac.png");
        player5 = new Texture("Player\\playerOrange.png");
        player6 = new Texture("Player\\playerPink.png");
        player7 = new Texture("Player\\playerYellow.png");
        diffButton1 = new Texture("UI\\MenuButtons\\diff1.png");
        diffButton1Selected = new Texture("UI\\MenuButtons\\diff1Selected.png");
        diffButton2 = new Texture("UI\\MenuButtons\\diff2.png");
        diffButton2Selected = new Texture("UI\\MenuButtons\\diff2Selected.png");
        diffButton3 = new Texture("UI\\MenuButtons\\diff3.png");
        diffButton3Selected = new Texture("UI\\MenuButtons\\diff3Selected.png");
        //playerRegion = TextureRegion.split(new Texture("Player\\walkDownYellow.png"),playerSelected.getWidth()-2,playerSelected.getHeight()-2);
       // txture =  playerRegion[0][0];
       
        
        
        Map1 = new Texture("Maps\\Map1Icon.png");
        Map2 = new Texture("Maps\\Map2Icon.png");
        Map3 = new Texture("Maps\\Map3Icon.png");
        Map1Selected = new Texture("Maps\\Map1IconSelected.png");
        Map2Selected = new Texture("Maps\\Map2IconSelected.png");
        Map3Selected = new Texture("Maps\\Map3IconSelected.png");
        
         playerChosen = false;
        mapChosen = false;
        difficultyChosen = false;
        
        
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
    
    public void setPreferences() {
    	
    	//String[] preferences = new String[3] ;
    	
    	
    	String difficulty= new String();
    	String player= new String();
    	String map = new String();
    	String numOfAI = new String();
    	
    	difficulty = "3";
    	player = "red";
    	map = "map 1";
    	numOfAI = "3";
    	
    	//loop though buttons to see which is chosen
    	System.out.println("---diff");
    	for( int i = 0; i < difficultyButtons.length ; i ++) {
//    		System.out.println(i);
//    		System.out.println(difficultyButtons.length);
//    		System.out.println(difficultyButtons[0]);
//    		System.out.println(difficultyButtons[1]);
//    		System.out.println(difficultyButtons[2]);
//    		System.out.println(difficultyButtons[i].isPressed);
    		if(difficultyButtons[i].isPressed) {
    			difficulty = difficultyButtons[i].name;
    			System.out.println(difficulty);
    		}
    	}System.out.println("----player");
    	for( int i = 0; i < playerButtons.length ; i ++) {
    		if(playerButtons[i].isPressed) {
    			player = playerButtons[i].name;
    			System.out.println(player);
    		}
    	}
    	System.out.println("---map");
    	for( int i = 0; i < mapButtons.length ; i ++) {
    		if(mapButtons[i].isPressed) {
    			map= mapButtons[i].name;
    			System.out.println(map);
    		}
    	}
    
//    	numOfAI = "6";
    	
    	ArrayList<String> input = new ArrayList<>();
    	input.add(map);
    	input.add(player);
    	input.add(difficulty);
    	input.add(numOfAI);
    	
    	CSVStuff.writeCSV(input);
    	
    	
    	
	}


    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
    
    	int buf = 110;

    	int drawX = DRAW_X;
    	AIButton = new Button(drawX - 100,AI_Y, size,size, joinMazeButtonActive, joinMazeButtonInactive , "AI" , "numAI");
    	drawX += buf *2;
    	AIDifficulty1Button = new Button(drawX  ,AI_Y, size,size, diffButton1Selected, diffButton1 , "difficulty" , "difficulty 1");
    	drawX += buf;
    	AIDifficulty2Button = new Button(drawX  ,AI_Y, size,size, diffButton2Selected, diffButton2,"difficulty" , "difficulty 2");
    	drawX += buf;
    	AIDifficulty3Button = new Button(drawX ,AI_Y, size,size, diffButton3Selected, diffButton3,"difficulty" , "difficulty 3");
    	
    	drawX = DRAW_X;
    	player1Button = new Button(drawX,PLAYER_Y, size,size, playerSelected, player1 ,"player" , "red");
    	drawX += buf;
    	player2Button = new Button(drawX,PLAYER_Y, size,size, playerSelected, player2 ,"player", "blue");
    	drawX += buf;
    	player3Button = new Button(drawX,PLAYER_Y, size,size, playerSelected, player3 ,"player", "green");
    	drawX = DRAW_X;
    	player4Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player4 ,"player", "lilac");
    	drawX += buf;
    	player5Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player5 ,"player", "orange");
    	drawX += buf;
    	player6Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player6 ,"player", "pink");
    	drawX += buf;
    	player7Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player7,"player", "yellow");
    	
    	drawX = DRAW_X;
    	Map1Button = new Button(drawX,MAP_Y, size,size, Map1Selected, Map1 , "map" , "map1" );
    	drawX += buf;
    	Map2Button = new Button(drawX,MAP_Y, size,size, Map2Selected, Map2 , "map", "map2" );
    	drawX += buf;
    	Map3Button = new Button(drawX,MAP_Y, size,size, Map3Selected, Map3 , "map", "map3" );
    	
    	playerButtons = new Button[] {player1Button,player2Button,player3Button,player4Button,player5Button,player6Button,player7Button};
    	difficultyButtons = new Button[] {AIDifficulty1Button,AIDifficulty2Button,AIDifficulty3Button};
    	mapButtons = new Button[] {Map1Button,Map2Button,Map3Button};
    	 
    	CreateMaze = new Button(DRAW_X ,MAP_Y - 110, size * 2,size,joinMazeButtonActive,joinMazeButtonInactive , "create" , "create");
    	
    	font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
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
    	AIDifficulty1Button.draw();
    	AIDifficulty2Button.draw();
    	AIDifficulty3Button.draw();
    	
    	Map1Button.draw();
    	Map2Button.draw();
    	Map3Button.draw();
    	
    	
    	
    	String message = "Create New Maze" ;
        
        font.draw(game.batch,message, DRAW_X  , MazeGame.HEIGHT -100);
          
        
        message = "Select AI" ;
        
        font.draw(game.batch,message, DRAW_X - 300 ,AI_Y + 50);
        
        message = "Difficulty" ;
       
        font.draw(game.batch,message, DRAW_X ,AI_Y + 50);
        
        message = "Select Player" ;
        
        font.draw(game.batch,message, DRAW_X - 300 ,PLAYER_Y + 50);
        
        message = "Select Map" ;
       
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
    	boolean isPressed = false;
    	String type;
    	String name;
    	private Button (int x,int y, int w, int h , Texture active, Texture inactive , String type,String name) {
    		this.x = x;
    		this.y = y;
    		this.width = w;
    		this.height = h;
    		this.active = active;
    		this.inactive = inactive;
    		this.type = type;
    		this.name = name;
    		
    		texture = this.inactive;
    		
    		activeWidth = this.width + 10;
    		activeHeight = this.height + 10;
    		
    	}
    	
    	
    	
    	public void draw() {
    		
    		//if(this.isPressed) System.out.println("pressed ");
    		
    		if (isButtonHovering()) {
    			
    			
    			if (Gdx.input.justTouched() && !isPressed) {
    				
    				if(this.type == "create") {
    					
    					if(playerChosen && difficultyChosen && mapChosen) {
    						setPreferences();
    						game.setScreen(new GameScreen(game));
    					}
    				}
    				
    				if(this.type == "player") {
    					if(!playerChosen) {
        					this.isPressed = true;
        					playerChosen = true;
        				}
    				}
    				if(this.type == "map") {
    					if(!mapChosen) {
        					this.isPressed = true;
        					mapChosen = true;
        				}
    				}
    				if(this.type == "difficulty") {
    					if(!difficultyChosen) {
        					this.isPressed = true;
        					difficultyChosen = true;
        				}
    				}
    				
 
    				
    				
    				
    			}else if (Gdx.input.justTouched() && isPressed) {
    				this.isPressed = false;
    				
    				if(this.type == "player") {
    					playerChosen = false;
    				}
    				if(this.type == "map") {
    					mapChosen = false;
    				}
    				if(this.type == "difficulty") {
    					difficultyChosen = false;
    				}
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


