package com.project.mazegame.screens;

<<<<<<< HEAD
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
=======
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

>>>>>>> yueyi2

public class CreateMazeScreen implements Screen {

    private MazeGame game;
    public String username;
    private boolean hasEnterUsername =false;
<<<<<<< HEAD
    
    
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
    
    Texture createYourMaze;
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
        createYourMaze = new Texture("UI\\Titles\\CreateyourMaze.png");
        
        
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
    
    	for( int i = 0; i < difficultyButtons.length ; i ++) {
    		if(difficultyButtons[i].isPressed) {
    			difficulty = difficultyButtons[i].name;
    		
    		}
    	}
    	for( int i = 0; i < playerButtons.length ; i ++) {
    		if(playerButtons[i].isPressed) {
    			player = playerButtons[i].name;
    	
    		}
    	}
    	
    	for( int i = 0; i < mapButtons.length ; i ++) {
    		if(mapButtons[i].isPressed) {
    			map= mapButtons[i].name;
    		
    		}
    	}
    
    	ArrayList<String> input = new ArrayList<>();
    	input.add(map);
    	input.add(player);
    	input.add(difficulty);
    	input.add(numOfAI);
    	
    	CSVStuff.writeCSV(input);
    	
    	
    	
	}

=======
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

>>>>>>> yueyi2

    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
<<<<<<< HEAD
    
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
    	 
    	CreateMaze = new Button(DRAW_X - 50,MAP_Y - 170, 400,size,joinMazeButtonActive,joinMazeButtonInactive , "create" , "create");
    	
    	font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
=======
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
>>>>>>> yueyi2
    }

    @Override
    public void render(float delta) {
<<<<<<< HEAD
    	
    	if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            game.setScreen(new MenuScreen(this.game));
        }
    	
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
    	game.batch.draw(createYourMaze, DRAW_X -300 , MazeGame.HEIGHT -200, 900, 200);
		
        
        
        message = "Select AI" ;
        
        font.draw(game.batch,message, DRAW_X - 300 ,AI_Y + 50);
        
        message = "Difficulty" ;
       
        font.draw(game.batch,message, DRAW_X + 20,AI_Y + 50);
        
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
        				}else {
        					//change all others to false
        					resetButtons(this.type);
        					
//        					change this one to true
        					this.isPressed = true;
        					playerChosen = true;
        				}
    				}
    				if(this.type == "map") {
    					if(!mapChosen) {
        					this.isPressed = true;
        					mapChosen = true;
        				}
    					else {
    						resetButtons(this.type);
    						this.isPressed = true;
        					mapChosen = true;
    					}
    				}
    				if(this.type == "difficulty") {
    					if(!difficultyChosen) {
    						this.isPressed = true;
        					difficultyChosen = true;
        				}else {
        					resetButtons(this.type);
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
    	
    	private void resetButtons(String type) {
    		
    		
    		//loop through buttons changing their isPredd to false
    		if(type.equals("player")) {
	    		for( int i = 0; i < playerButtons.length ; i ++) {
	        		if(playerButtons[i].isPressed) {
	        			playerButtons[i].isPressed  = false;
	        	
	        		}
	        	}
    		}
    		if(type.equals("difficulty")) {
	    		for( int i = 0; i < difficultyButtons.length ; i ++) {
	        		if(difficultyButtons[i].isPressed) {
	        			difficultyButtons[i].isPressed = false;
	        		
	        		}
	        	}
    		}
    		if(type.equals("map")) {
	        	for( int i = 0; i < mapButtons.length ; i ++) {
	        		if(mapButtons[i].isPressed) {
	        			mapButtons[i].isPressed = false;
	        		
	        		}
	        	}
    		}
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
=======
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




>>>>>>> yueyi2
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
<<<<<<< HEAD
    	font.dispose();
=======


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
>>>>>>> yueyi2
    }
}


