package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.maps.MapLayer;
//import com.badlogic.gdx.maps.MapObject;
///import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
//import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.*;

import java.awt.ItemSelectable;
import java.lang.Integer;
//import com.project.mazegame.tools.Variables;





import java.util.ArrayList;
import java.util.HashSet;

import com.project.mazegame.tools.Variables;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;



	
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.InputHandler;
import com.project.mazegame.tools.OrthoCam;

import static com.project.mazegame.tools.Variables.SCROLLTRACKER_X;
import static com.project.mazegame.tools.Variables.SCROLLTRACKER_Y;
import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;

public class GameScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;

    private Player player;
    private InputHandler inputHandler;
    private float delta;

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;
    //private MapLayer objectLayer;


    private Texture exitButtonActive;
    private Texture exitButtonInactive;
    private Texture heartTexture;
    private Texture coinTexture;
    private Texture swordTexture;
    private Texture shieldTexture;
    private Texture compassTexture;
    private Texture healingPotionTexture;
    private Texture audioButtonActive;
    private Texture audioButtonInactive;
    
   // private Player player1;
    private Player player2;
    
    private Collect co;
    
   // private Collect collect = new Collect();;
    

    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
   
    public static ArrayList<Item> mapItems = new ArrayList<Item>();
       private Texture audioButtonActive;
    private Texture audioButtonInactive;


    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
    private final int EXIT_Y = VIEWPORT_HEIGHT;

   
    public GameScreen(MazeGame game) {
        this.game = game;
        inputHandler = new InputHandler();

        tileMap = new TmxMapLoader().load("prototypeMap.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
        
        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        player = new Player(this.collisionLayer,"james",123);
        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, player.x, player.y);
        


        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
        //coinLayer = (TiledMapTileLayer) tileMap.getLayers().get("coinLayer");
        //objects


        //System.out.println("Tile's width " + collisionLayer.getWidth());
        



        // buttons
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        audioButtonActive = new Texture("audioOn.png");
        audioButtonInactive = new Texture("audioOff.png");
        
        
        heartTexture = new Texture("heart.png");
        coinTexture = new Texture("coin.png");
        swordTexture = new Texture("SWORDluv.png");
        shieldTexture = new Texture("shield.png");
        healingPotionTexture = new Texture("Potion.png");
        compassTexture = new Texture("RolledMap.png");
        
       
        
        
    }
    int size;
    @Override
    public void show() {
    	 //assuming it's a square map -> only need width of map and width of tile
        System.out.println("generating from gamescreen");
        generateMapItems((int) collisionLayer.getWidth(), 100 );
        co = new Collect(game, player);
        System.out.println(mapItems);
        //System.out.println(positions);
        size = mapItems.size();
        System.out.println("size" + size);
        
    }
 
    
    
    @Override
    public void render(float delta) {
    	if (!(mapItems.size() == size)) {
	    	size = mapItems.size();
	        System.out.println("size" + size);
        
    	}
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();
    
        //updates - player position
        inputHandler.update();
        player.update(delta);

        //draws tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();
        
        game.batch.begin();
        
        
        //rendering
        //draw coins
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
	    		texture = healingPotionTexture;
	    	}
	    	if (mapItems.get(i).getType() == "gearEnchantment") {
	    		texture = healingPotionTexture;
	    	}
	    	if (mapItems.get(i).getType() == "compass") {
	    		texture = compassTexture;
	    	}
	    	
		    int x = mapItems.get(i).getPosition().getX(); 
		    int y = mapItems.get(i).getPosition().getY();
		   
		    game.batch.draw(texture,x   - CAMERA_X, y  - CAMERA_Y  ,100,100);   	
	    	
	    }

      
        //Collectibles pick up
	    if (!(mapItems.size() == 0)) {
	    	
		    //System.out.println(mapItems);
	        
	       
	      
	       
	        if ((player.position.getX() > co.nearestItem(player).getPosition().getX()-50) && (player.position.getX() < co.nearestItem(player).getPosition().getX()+50) && 
	            (player.position.getY() > co.nearestItem(player).getPosition().getY()-50) && (player.position.getY() < co.nearestItem(player).getPosition().getY()+50)){
	        	System.out.println("nearest " + co.nearestItem(player).getPosition().getX() + " , " + co.nearestItem(player).getPosition().getY() );
		        System.out.println("player " + player.position.getX() + " , " + player.position.getY());
	        	  
	        	System.out.println("over item");
	        	Item item =  co.nearestItem(player);
	        	
	        	if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")) {
	        		item = co.pickedUp(co.nearestItem(player));
	        		
					if (item.getType() == "shield") {
						System.out.println("hit shiield");
						co.shield(item, player);
					}
					if (item.getType() == "sword") {
						co.sword(item, player, player2);
					}
					
					if (item.getType() == "healingPotion") {
						co.healingPotion (player);
					}
					if (item.getType() == "damagingPotion") {
						co.damagingPotion(item, player);
					}
					if (item.getType() == "gearEnchantment") {
						co.gearEnchantment(item , player);
					}
				} else if (item.getType() == "coin") {
					mapItems.remove(item);
					player.coins++;
					System.out.println("coin count:" + player.coins);
						}
	        	}
		}
      
        float x = player.x + VIEWPORT_WIDTH / 2 - EXIT_WIDTH;
        float y = player.y + VIEWPORT_HEIGHT / 2 - EXIT_HEIGHT;
        
        //exit button in top right corner
        game.batch.draw(exitButtonActive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//            cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
//            this.dispose();
//            game.setScreen(new MenuScreen(this.game));
        }
//        if (Gdx.input.getX() < (x + EXIT_WIDTH) && Gdx.input.getX() > x && MazeGame.HEIGHT - Gdx.input.getY() > EXIT_Y && MazeGame.HEIGHT - Gdx.input.getY() < EXIT_Y + EXIT_HEIGHT) {
//            game.batch.draw(exitButtonActive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
//            if (Gdx.input.isTouched())
//                Gdx.app.exit();
//        }
//        else {
//            game.batch.draw(exitButtonInactive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
//        }
        

        //draw hearts in top left corner
        float iconSize = 30;
        float buffer = 10;
        float xheart = player.x - VIEWPORT_WIDTH /2 +buffer;
        float yheart = player.y + VIEWPORT_HEIGHT/2 - iconSize -buffer;
        int lives = player.getLives();
        for(int i = 0; i < lives; i ++) {
        	  game.batch.draw(heartTexture, xheart, yheart,iconSize , iconSize);
        	  xheart += (iconSize + buffer);
        }
        if (player.items.contains("shield")) {
        //draw shield icon
	        float shieldSize = 50;
	        float xShield =  player.x + VIEWPORT_WIDTH /2 - shieldSize -buffer;
	        float yShield = player.y + VIEWPORT_HEIGHT/2 - (shieldSize *3) ;
	        game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);
        }
        //sword icon
        if ( player.items.contains("sword")) {
	        float swordSize = 50;
	        float xSword =  player.x + VIEWPORT_WIDTH /2 - swordSize - buffer;
	        float ySword = player.y + VIEWPORT_HEIGHT/2 - (swordSize *2) ;
	        game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);
        }
        //draws coin icon
        for ( int i = 0; i < player.coins; i ++ ) {
	        float xCoin = player.x - (VIEWPORT_WIDTH /2) ;
	        float yCoin = player.y + VIEWPORT_HEIGHT/2 - ( iconSize*3) -buffer;
	        game.batch.draw(coinTexture, xCoin + (i*10), yCoin,iconSize*2 , iconSize*2);
        }
        
        //audio icon
        float xAudio = player.x + VIEWPORT_WIDTH /2 -(iconSize*3);
        float yAudio = player.y + VIEWPORT_HEIGHT/2 - iconSize - buffer ;
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(audioButtonActive));
        ImageButton audioButton= new ImageButton(drawable);
        audioButton.setX(xAudio);
        audioButton.setY(yAudio);
        audioButton.setWidth(iconSize);
        audioButton.setHeight(iconSize);
        //audioButton.draw
        if (audioButton.isOver()) {
        	drawable = new TextureRegionDrawable(new TextureRegion(audioButtonInactive));
        	audioButton.setBackground(drawable);
        }

        player.render(game.batch);
        game.batch.end();
        //camera
        cam.update(player.x, player.y);
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
        tileMap.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        player.dispose();
    }
    
    public void generateMapItems( int widthInTiles, int tileWidth ) {
        HashSet<String> positions = new HashSet<String>();
    	System.out.println("generating");
    	int maxShields = 3;
		int maxCoins = 10;
		int maxSwords = 5;
		int maxCompasses = 3;
		int maxPotions = 10;
		int maxX = widthInTiles;
		int maxY = widthInTiles;
	


		for (int i = 0; i <= maxShields; i++) {
			ItemCell position = new ItemCell();
			
			
			position.setX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.setY((int)((Math.random() * (maxY )))* tileWidth);
			
			Item item = new Item("shield", position);
			System.out.println(position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
//				System.out.println(position);
				mapItems.add(item);
				System.out.println("adding to positions");
				positions.add(position.toString());
			}
		}

		for (int i = 0; i < maxCoins; i++) {
			ItemCell position = new ItemCell();
			position.setX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.setY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("coin", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
//				System.out.println(position);
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxSwords; i++) {
			ItemCell position = new ItemCell();
			position.setX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.setY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("sword", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxCompasses; i++) {
			ItemCell position = new ItemCell();
			position.setX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.setY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("compass", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxPotions; i++) {
			ItemCell position = new ItemCell();
			position.setX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.setY((int)((Math.random() * (maxY )))* tileWidth);
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
		System.out.println(positions);
	}
}
