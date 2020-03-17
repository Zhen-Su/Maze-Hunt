package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.project.mazegame.objects.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.*;

import java.awt.Font;
import java.awt.ItemSelectable;
import java.util.ArrayList;
import java.util.HashSet;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;
import static com.project.mazegame.tools.Variables.V_HEIGHT;
import static com.project.mazegame.tools.Variables.V_WIDTH;
import static com.project.mazegame.tools.Variables.*;


public class GameScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;

    private Player player;
    private AIPlayer aiPlayer;// ---------need to be implemented
    private InputHandler inputHandler;
    private float delta;

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;

    private Texture exitButtonActive,exitButtonInactive;
   
    private Texture heartTexture;
    private Texture coinTexture;
    private Texture swordTexture;
    private Texture shieldTexture;
    private Texture compassTexture;
    private Texture healingPotionTexture;
    private Texture damagingPotionTexture;
    private Texture gearEnchantmentTexture;
    private Texture audioButtonActive; //-------need to  implemented
    private Texture audioButtonInactive;
    private Texture overlay;
    private Texture coinPick;
    private BitmapFont font;
    private Texture mapTexture, minimapOutline;
    
    private Texture enchantedGlow;
    
    
    AnimationTool coinAnimation;
    
//    private float timer;
    public static float worldTimer;
    
    Timer time = new Timer();
    
    private Player player2;
    
    private Collect co;
 
    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
    
    public float initialisedShieldTime;
    private float initialisedPotionTime;
    private float initialisedEnchantmentTime;
   
    public static ArrayList<Item> mapItems = new ArrayList<Item>();

    private final int EXIT_Y = VIEWPORT_HEIGHT;
    
    private int tempMapItemssize;
    
    private int numOfAI;
    private String map;
    private String playerSkin;
    private String AIDifficulty;
    
    int overlayWidth;
    int overlayHeight;
    int keyFrame;
   
    Coordinate playerPos;
    
    public GameScreen(MazeGame game) {
        this.game = game;
        
        //CreateMazeScreen c = new CreateMazeScreen(game);
//        System.out.println("here");
        
//        System.out.println("and here");
        
        inputHandler = new InputHandler();
        
        //timer = 0;
        worldTimer = 60;
        
        
        // read csv file
        ArrayList<String> output = CSVStuff.readCSVFile();
        System.out.println(output);
        
        this.map = output.get(0);
        this.playerSkin = output.get(1);
        this.AIDifficulty = output.get(2);
        this.numOfAI = Integer.parseInt(output.get(3));
        
        System.out.println("map is " + this.map);
        
        if(this.map.equals( "map1")) {
        	tileMap = new TmxMapLoader().load("Map1.tmx");
        }
        else if(this.map.equals("map2")) {
        	tileMap = new TmxMapLoader().load("Map2.tmx");
        }
        else {
        	tileMap = new TmxMapLoader().load("Map3.tmx");
        }
        
        System.out.println("map defo is " + this.map);
        
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
        
        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        player = new Player(this.collisionLayer,"james",123 , this.playerSkin);
//        System.out.println("playerskin is " + game.playerSkin);
       
//       player.initialPosition();
//        aiPlayer = new AIPlayer(this.collisionLayer, "Al", 124);
        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, player.position.getX(),player.position.getY());
        
        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
     
        // buttons
        exitButtonActive = new Texture("UI\\MenuButtons\\exit_button_active.png");
        exitButtonInactive = new Texture("UI\\MenuButtons\\exit_button_inactive.png");
        audioButtonActive = new Texture("UI\\MenuButtons\\audioOn.png");
        audioButtonInactive = new Texture("UI\\MenuButtons\\audioOff.png");
        
        heartTexture = new Texture("Collectibles\\heart.png");
        coinTexture = new Texture("Collectibles\\\\coin.png");
        swordTexture = new Texture("Collectibles\\\\sword2.png");
        shieldTexture = new Texture("Collectibles\\\\shield.png");
        healingPotionTexture = new Texture("Collectibles\\\\Potion.png");
        gearEnchantmentTexture = new Texture("Collectibles\\\\Potion2.png");
        compassTexture = new Texture("Collectibles\\\\RolledMap.png");
        damagingPotionTexture = new Texture("Collectibles\\\\Potion3.png");
        overlay = new Texture("UI\\circularOverlay.png");
        coinPick = new Texture("Collectibles\\coinAnimation.png");
        mapTexture = new Texture("Maps\\Map2Icon.png");
        minimapOutline = new Texture("Maps\\minimapOutline.png");
        overlayWidth = overlay.getWidth() +300;
        overlayHeight = overlay.getHeight() +300;
        enchantedGlow = new Texture("Player\\ENCHANTED.png");
       
        
        //coinAnimation = new AnimationTool(50,50,player,coinPick,true);
        //coinAnimation.create();
    }
    
    @Override
    public void show() {
    	 //assuming it's a square map -> only need width of map and width of tile
        generateMapItems((int) collisionLayer.getWidth(), 100 );
        co = new Collect(game, player);
        tempMapItemssize = mapItems.size();
        //start timer
        player.initialPosition();
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        
        playerPos = new Coordinate(player.position.getX(), player.position.getY());
    }
    int iconSize = 30;
    @Override
    public void render(float delta) { //method repeats a lot
    	
    	updateTime(delta);
    	removeShield();
    	removeEnchantment();
    	playerDamaging();
    	
    	if (time.currentTime() == 3) {
    		player.decreaseHealth(10);
    	}
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();
    
        //updates - player position
        inputHandler.update();
        player.update(delta);
        //camera
        cam.update(player.position.getX(),player.position.getY(),game);
        
        //comment out ai player line to run correctly
//       aiPlayer.update(delta);
        
        //draws tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();
      
        game.batch.begin();
        
        //draw collectibles
        drawCollectibles();
      
        //Collectibles pick up
	    if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception	
	        if ((player.position.getX() > co.nearestItem(player).getPosition().getX()) && (player.position.getX() < co.nearestItem(player).getPosition().getX()+100) && 
	            (player.position.getY() > co.nearestItem(player).getPosition().getY()) && (player.position.getY() < co.nearestItem(player).getPosition().getY()+100)){
	        	pickUpItem();
	        	
	        }
		}
	    
	    game.batch.draw(overlay,player.position.getX() - overlayWidth/2,player.position.getY() - overlayHeight/2 , overlayWidth ,overlayHeight);
        
        
        int buffer = 10;
        playerPos.setX(player.position.getX());
        playerPos.setY(player.position.getY());
        drawIcons(iconSize,buffer,playerPos);
        drawExitButton(playerPos);
        
        if(player.items.contains("gearEnchantment"))game.batch.draw(enchantedGlow ,player.position.getX() -enchantedGlow.getWidth()/2 ,player.position.getY() - enchantedGlow.getHeight()/2 , enchantedGlow.getWidth() ,enchantedGlow.getHeight());
        
        
        player.render(game.batch);
        player.attack();
        
        String message = "Time = " + (int) (worldTimer - (time.currentTime())) ;
        
        
        font.draw(game.batch,message, player.position.getX(),player.position.getY() + VIEWPORT_HEIGHT/2 -10);
        
    	//if timer runs out 
    	if((worldTimer - time.currentTime()) < 3) {
    		overlayWidth -= 15;
    		overlayHeight -= 15;
    	  
    	   if((worldTimer - time.currentTime()) < 0) {
    		   this.dispose();
    		   game.setScreen(new EndScreen(this.game));
    		  
    	   }
    	}
         game.batch.end();
    }
    
    
    int coinSize = iconSize*2;
    
    
    private void drawIcons(int iconSize, int buffer, Coordinate position) {
    	//take player x and y into account
    	int playerX = position.getX() - VIEWPORT_WIDTH/2;
    	int playerY = position.getY() - VIEWPORT_HEIGHT/2;
        //draw hearts in top left corner
 
        int xheart = buffer + playerX;
        int yheart = VIEWPORT_HEIGHT - iconSize -buffer + playerY;
        int lives = player.getHealth();
        for(int i = 0; i < lives; i ++) {
        	  game.batch.draw(heartTexture, xheart, yheart,iconSize , iconSize);
        	  xheart += (iconSize + buffer);
        }
        if (player.items.contains("shield")) {
        //draw shield icon
	        float shieldSize = 50;
	        float xShield = VIEWPORT_WIDTH - shieldSize -buffer + playerX;
	        float yShield = VIEWPORT_HEIGHT - (shieldSize *3) + playerY -50;
	        game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);
	        
	        String message = "XP :" + player.getShieldXP() ;
	        font.getData().setScale(0.5f,0.5f);
	        font.draw(game.batch,message, xShield  ,yShield );
        }
        //sword icon
        if ( player.items.contains("sword")) {
	        float swordSize = 50;
	        float xSword = VIEWPORT_WIDTH  - swordSize - buffer + playerX;
	        float ySword = VIEWPORT_HEIGHT - (swordSize *2) + playerY;
	        game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);
	        
	        
	        String message = "XP :" + player.getSwordXP(); 
	        font.getData().setScale(0.5f,0.5f);
	        font.draw(game.batch,message, xSword ,ySword  );
        }
        
       
        
        //draws coin icon
        for ( int i = 0; i < player.coins; i ++ ) {
	        float xCoin = buffer + playerX;
	        float yCoin = VIEWPORT_HEIGHT - ( iconSize*3) -buffer + playerY;
	        if ( coinSize != iconSize*2) coinSize -=5;
	        game.batch.draw(coinTexture, xCoin + (i*10), yCoin, coinSize,coinSize);
        }
        float mapSize = 100;
        float xMap = player.x + VIEWPORT_WIDTH /2 - mapSize - 50;
        float yMap =  player.y - VIEWPORT_HEIGHT/2 + 50;
        
        game.batch.draw(minimapOutline, xMap -8 ,yMap -9,mapSize + 17 , mapSize +17);
        game.batch.draw(mapTexture, xMap,yMap,mapSize , mapSize);
       
        
    }
    
    private void drawExitButton(Coordinate position) {
    	
    	//take player x and y into account
    	int playerX = position.getX() - VIEWPORT_WIDTH/2;
    	int playerY = position.getY() - VIEWPORT_HEIGHT/2;
    	
    	int x = VIEWPORT_WIDTH  - EXIT_WIDTH + playerX;
        int y = VIEWPORT_HEIGHT - EXIT_HEIGHT + playerY;
        
        //origin of cursor is top left hand corner
        //exit button in top right corner
      
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            game.setScreen(new MenuScreen(this.game));
        }
        
        if (Gdx.input.getX()  < V_WIDTH  && Gdx.input.getX()  >  V_WIDTH - EXIT_WIDTH && Gdx.input.getY() < EXIT_HEIGHT && Gdx.input.getY() > 0 ) {
        	game.batch.draw(exitButtonActive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
	        if (Gdx.input.justTouched()) {
	        	this.dispose();
	        	game.setScreen(new MenuScreen(this.game));
	        }
        }
        else game.batch.draw(exitButtonInactive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
    }
    
    private void drawCollectibles() {
    	  for(int i = 0; i < mapItems.size(); i ++) { 
  	    	Texture texture = heartTexture;
  	    	if (mapItems.get(i).getType() == "shield") {
  	    		texture = shieldTexture;
  	    	}
  	    	if (mapItems.get(i).getType() == "coin") {
  	    		texture = coinTexture;
  	    	}
  	    	if (mapItems.get(i).getType() == "sword") {
  	    		texture = swordTexture;
  	    	}
  	    	if (mapItems.get(i).getType() == "healingPotion") {
  	    		texture = healingPotionTexture;
  	    	}
  	    	
  	    	if (mapItems.get(i).getType() == "damagingPotion") {
  	    		texture = damagingPotionTexture;
  	    	}
  	    	if (mapItems.get(i).getType() == "gearEnchantment") {
  	    		texture = gearEnchantmentTexture;
  	    	}
  	    	if (mapItems.get(i).getType() == "compass") {
  	    		texture = compassTexture;
  	    	}
  	    	
  		    int x = mapItems.get(i).getPosition().getX(); 
  		    int y = mapItems.get(i).getPosition().getY();
  		    game.batch.draw(texture,x, y ,100,100);   	
  	    }

    }
    // -------------------------------------------------could move to collect class
    private void pickUpItem() {
    	Item item =  co.nearestItem(player);
    	
    	//System.out.println(player.items);
    	
    	if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")&& !(item.getType() == "healingPotion")&& !(item.getType() == "damagingPotion")) {
    		item = co.pickedUp(co.nearestItem(player));
    		
    		
			if (item.getType() == "shield") {
				item.setInitialisedTime((worldTimer - time.currentTime()));
				initialisedShieldTime = worldTimer - time.currentTime();
				co.shield(item, player);
				if(player.items.contains("gearEnchantment")) {
					initialisedShieldTime += 3;
					System.out.println("increased time");
				}
			}
			if (item.getType() == "sword") {
				co.sword(item, player, player2);
			}
			
			if (item.getType() == "gearEnchantment") {
				co.gearEnchantment(item , player);
				initialisedEnchantmentTime = time.currentTime();
			}
		} else if (item.getType() == "coin") {
			mapItems.remove(item);
			player.coins++;
		}else if (item.getType() == "healingPotion") {
			mapItems.remove(item);
			co.healingPotion (player);
		}else if (item.getType() == "damagingPotion") {
			mapItems.remove(item);
			co.damagingPotion(item, player);
		}
    }
    
    private void animateCoin() {
    	
//    	TextureRegion[] region = coinAnimation.getFrames();
//    	for(int i = 0 ; i < 4 ; i = keyFrame ) {
//    		game.batch.draw(region[keyFrame],player.position.getX() - 50/2,player.position.getY() - 50/2);
//    		
//    	}
//    	
    }
    
    private void removeShield() {
    	if(!player.items.contains("shield")) {
    		return;
    	}
    	if ((worldTimer - time.currentTime()) - initialisedShieldTime  == 10) {
    		player.items.remove("shield");
    	}
    }
    
    private void removeEnchantment() {
    	if(!player.items.contains("gearEnchantment")) {
    		
    		return;
    	}
    	if ((worldTimer - time.currentTime()) - initialisedEnchantmentTime == 10) {
    		player.items.remove("gearEnchantment");
    		System.out.println("removed enchantment");
    	}
    }
    
    private void playerDamaging() {
    	if(!player.items.contains("damagingPotion")) {
    		return;
    	}
    	if ((worldTimer - time.currentTime()) - initialisedPotionTime == 2) {
    		player.loadPlayerTextures();
    		player.items.remove("damagingPotion");
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
    	tileMap.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        player.dispose();
        mapItems.clear();
    }

    @Override
    public void dispose() {
        tileMap.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        player.dispose();
        mapItems.clear();
//        cam.update(V_WIDTH/2, V_HEIGHT/2, game);
        cam.cam.viewportHeight = 1000;
        cam.cam.viewportWidth = 1000;
        cam.update(V_WIDTH/2, V_HEIGHT/2, game);
        cam.cam.update();
        
    }
    
    public void generateMapItems( int widthInTiles, int tileWidth ) {
        HashSet<String> positions = new HashSet<String>();
    	int maxShields = 15;
		int maxCoins = 50;
		int maxSwords = 25;
		int maxCompasses = 15;
		int maxPotions = 50;
		int maxX = widthInTiles;
		int maxY = widthInTiles;
	


		for (int i = 0; i <= maxShields; i++) {
			Coordinate position = new Coordinate(0,0);
			
			
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			
			Item item = new Item("shield", position);

			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}
		}

		for (int i = 0; i < maxCoins; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("coin", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxSwords; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("sword", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxCompasses; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("compass", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxPotions; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			int whatPotion = (int)(Math.random() * 4);
			
			if (whatPotion == 1) {
				Item item = new Item("healingPotion", position);
				if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
					mapItems.add(item);
					positions.add(position.toString());
				}
			} else if (whatPotion == 2) {
				Item item = new Item("damagingPotion", position);
				if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
					mapItems.add(item);
					positions.add(position.toString());
				}
			} else {
				Item item = new Item("gearEnchantment", position);
				if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
					mapItems.add(item);
					positions.add(position.toString());
				}
			}
		}
	}
    
    private float timePassed(float theTime) {
    	return worldTimer - theTime;
    }
    
    private void updateTime(float dt) {
    	float initial  = time.currentTime();
    	time.updateTimer(dt);
    	System.out.println(time.currentTime());
    	if (!(time.currentTime() == initial)) {
    		worldTimer--;
//    		System.out.println("World Timer: " + worldTimer);
    		//timer = 0;
    	}
    }
}
