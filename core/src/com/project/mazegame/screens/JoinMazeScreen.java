package com.project.mazegame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.project.mazegame.MazeGame;
import com.project.mazegame.screens.CreateMazeScreen.Button;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.CSVStuff;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JoinMazeScreen implements Screen {


    MazeGame game;
    public Texture joinMaze , enterBox;
    Texture player1, player2, player3, player4, player5, player6, playerSelected , player7;
    private Button player1Button,player2Button,player3Button,player4Button,player5Button,player6Button,player7Button;
    private Button[] playerButtons ;
    Button CreateMaze;
    Texture joinMazeButtonActive;
    Texture joinMazeButtonInactive;
    
    public JoinMazeScreen(MazeGame game) {
        this.game = game;
        
        joinMaze = Assets.manager.get(Assets.JoinMaze,Texture.class);
        enterBox = Assets.manager.get(Assets.enterBox,Texture.class);
        
        joinMazeButtonInactive = new Texture("UI\\MenuButtons\\FindMazeButton.png");
        joinMazeButtonActive = new Texture("UI\\MenuButtons\\FindMazeButtonPressed.png") ;
        playerSelected = new Texture("Player\\playerSelected.png");
        player1 = new Texture("Player\\playerRed.png");
        player2 = new Texture("Player\\playerBlue.png");
        player3 = new Texture("Player\\playerGreen.png");
        player4 = new Texture("Player\\playerLilac.png");
        player5 = new Texture("Player\\playerOrange.png");
        player6 = new Texture("Player\\playerPink.png");
        player7 = new Texture("Player\\playerYellow.png");
        
        
    }
    private static final String TAG = JoinMazeScreen.class.getSimpleName();

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    public static final int TEXT_FIELD_WIDTH = 300;
    public static final int TEXT_FIELD_HEIGHT = 70;

    private Stage stage;
    private Texture bgTexture;
    private Texture cursorTexture;
    private BitmapFont bitmapFont;
    private TextField usernameTextField;
    private TextField ipAddressTextField;
    private String ipAdrress;
    private String username;
    private String colour;
    
    private static int DRAW_X = 300;
    private static int MAP_Y = 200;
    
    private static int PLAYER_Y = 340;
    private static int PLAYER2_Y = 240;
    
    boolean playerChosen;
    
    
    
    Texture backGround;
    BitmapFont font;
    private static final String ipRegex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

    public void setIpAdrress(String ipAdrress) { this.ipAdrress = ipAdrress; }

    public void setUsername(String username) { this.username = username; }
    
    public void setColour (String colour) { this.colour = colour;}
    	
    

    
    @Override
    public void show() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));

        Gdx.input.setInputProcessor(stage);

        backGround = new Texture("UI\\Backgrounds\\menuBackground.png");

        bgTexture = createBackgroundTexture();
        cursorTexture = createCursorTexture();

        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(1.5F);

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.background = new TextureRegionDrawable(new TextureRegion(enterBox));
        style.cursor = new TextureRegionDrawable(new TextureRegion(cursorTexture));
        style.font = bitmapFont;
        style.fontColor = new Color(1, 1, 1, 1);

        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("bitmap.fnt"));
        font = new BitmapFont((Gdx.files.internal("myFont.fnt")));
        
        font.setColor(Color.WHITE);
        font.getData().setScale(1f);

        usernameTextField = new TextField("Player2", style);
        ipAddressTextField = new TextField("127.0.0.1", style);

        usernameTextField.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        ipAddressTextField.setSize(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

        usernameTextField.setPosition(90, 500);
        ipAddressTextField.setPosition(90, 400);

        usernameTextField.setAlignment(Align.center);
        ipAddressTextField.setAlignment(Align.center);

        stage.addActor(usernameTextField);
        stage.addActor(ipAddressTextField);
        
        int drawX, buf , size;
        drawX = DRAW_X - 30;
    	buf = 150;
    	size = 100;
    	player1Button = new Button(drawX,PLAYER_Y, size,size, playerSelected, player1 ,"player" , "red");
    	drawX += buf;
    	player2Button = new Button(drawX,PLAYER_Y, size,size, playerSelected, player2 ,"player", "blue");
    	drawX += buf;
    	player3Button = new Button(drawX,PLAYER_Y, size,size, playerSelected, player3 ,"player", "green");
    	drawX = DRAW_X - 100;
    	buf = 150;
    	player4Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player4 ,"player", "lilac");
    	drawX += buf;
    	player5Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player5 ,"player", "orange");
    	drawX += buf;
    	player6Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player6 ,"player", "pink");
    	drawX += buf;
    	player7Button = new Button(drawX,PLAYER2_Y, size,size, playerSelected, player7,"player", "yellow");
    	CreateMaze = new Button(DRAW_X,MAP_Y - 100, 400,size,joinMazeButtonActive,joinMazeButtonInactive , "join" , "join");
    	playerButtons = new Button[] {player1Button,player2Button,player3Button,player4Button,player5Button,player6Button,player7Button};
    }
    

    
    public class Button {
    	int x, y,width,height , activeWidth,activeHeight;
    	
    	Texture active,inactive,texture;
    	boolean isPressed = false;
    	String type;
    	String name;
    	public Button (int x,int y, int w, int h , Texture active, Texture inactive , String type,String name) {
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
    				
    				if(this.type == "join") {
    					
    					if(playerChosen) {
    					    Gdx.app.log(TAG, "username = " + usernameTextField.getText());
    			            Gdx.app.log(TAG, "ipaddress = " + ipAddressTextField.getText());
    			            setIpAdrress(ipAddressTextField.getText());
    			            setUsername(usernameTextField.getText());
    			            
    			        	for( int i = 0; i < playerButtons.length ; i ++) {
    			        		if(playerButtons[i].isPressed) {
    			        			setColour(playerButtons[i].name);
    			        	
    			        		}
    			        	}
    					
    						
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
    			}else if (Gdx.input.justTouched() && isPressed) {
    				this.isPressed = false;
    				
    				if(this.type == "player") {
    					playerChosen = false;
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
    	public void resetButtons(String type) {
    		if(type.equals("player")) {
	    		for( int i = 0; i < playerButtons.length ; i ++) {
	        		if(playerButtons[i].isPressed) {
	        			playerButtons[i].isPressed  = false;
	        	
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

    private Texture createBackgroundTexture() {
        Pixmap pixmap = new Pixmap(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 1, 0, 1);
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        {
            game.batch.draw(backGround, 0, 0, 1000, 1000);
            
            game.batch.draw(joinMaze, 150, 800, 700, 200);
            
            String message = "Enter name";
        	font.draw(game.batch, message, MazeGame.WIDTH/2 -100, 750 );
        	message = "Enter IP address";
         	font.draw(game.batch, message, MazeGame.WIDTH/2 -150, 620);
         	message = "Choose your player";
         	font.draw(game.batch, message, MazeGame.WIDTH/2 -150, 470);
            handleInput();
            checkIp();
            stage.act();
            
        	
            player1Button.draw();
        	player2Button.draw();
        	player3Button.draw();
        	player4Button.draw();
        	player5Button.draw();
        	player6Button.draw();
        	player7Button.draw();
        	
        	CreateMaze.draw();
        }
        game.batch.end();
        stage.draw();
    }
    



    private void handleInput(){
        
            //game.setScreen(new MultiPlayerGameScreen(game,usernameTextField.getText(),ipAddressTextField.getText()));
        
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            backToMenuScreen();
        }
    }

    private void checkIp(){
        if(ipAdrress!=null) {
            Pattern pattern = Pattern.compile(ipRegex);
            Matcher matcher = pattern.matcher(ipAdrress);
            boolean matchIp = matcher.matches();
            if (matchIp) {
                game.setScreen(new OtherLobbyScreen(game, username, ipAdrress , colour));
            } else {
            	
                font.draw(game.batch, "Ip not correct,Please try again...", 320, 500);
            }
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

    }

    @Override
    public void dispose() {
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        if (cursorTexture != null) {
            cursorTexture.dispose();
        }
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

    private void backToMenuScreen(){
        System.out.println("back to");
        // cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
        //this.dispose();
        game.setScreen(new MenuScreen(this.game));
        System.out.println("shouldn't see");
    }


}
