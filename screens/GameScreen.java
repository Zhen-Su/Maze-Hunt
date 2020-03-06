package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.project.mazegame.objects.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.*;

import java.util.ArrayList;
import java.util.HashSet;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;

import com.project.mazegame.tools.InputHandler;
import com.project.mazegame.tools.OrthoCam;



public class GameScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;

    private AIPlayer player;
//    private AIPlayer aiPlayer;// ---------need to be implemented
    private InputHandler inputHandler;
    private float delta;

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;

    private Texture exitButtonActive;
    private Texture exitButtonInactive;
    private Texture heartTexture;
    private Texture coinTexture;
    private Texture swordTexture;
    private Texture shieldTexture;
    private Texture compassTexture;
    private Texture healingPotionTexture;
    private Texture damagingPotionTexture;
    private Texture audioButtonActive; //-------need to  implemented
    private Texture audioButtonInactive;
    
    private Player player2;
	private Player player3;
    private Collect co;
    private Collect co1;
 
    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
   
    public static ArrayList<Item> mapItems = new ArrayList<Item>();

    private final int EXIT_Y = VIEWPORT_HEIGHT;
    
    private int tempMapItemssize;
    
    public GameScreen(MazeGame game) {
        this.game = game;
        inputHandler = new InputHandler();

        tileMap = new TmxMapLoader().load("prototypeMap.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
        
        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        player = new AIPlayer(this.collisionLayer,"james",123, co);
		player2 = new Player(this.collisionLayer, "Albert", 124, co);
        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, VIEWPORT_WIDTH/2,VIEWPORT_HEIGHT/2);
        
        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
     
        // buttons
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        audioButtonActive = new Texture("audioOn.png");
        audioButtonInactive = new Texture("audioOff.png");
        
        heartTexture = new Texture("heart.png");
        coinTexture = new Texture("coin.png");
        swordTexture = new Texture("sword.png");
        shieldTexture = new Texture("shield.png");
        healingPotionTexture = new Texture("Potion.png");
        compassTexture = new Texture("RolledMap.png");
        damagingPotionTexture = new Texture("Potion.png");
        
        
    }
    
    @Override
    public void show() {
    	 //assuming it's a square map -> only need width of map and width of tile
        generateMapItems((int) collisionLayer.getWidth(), 100 );
        co = new Collect(game, player);
        co1 = new Collect(game, player2);
        tempMapItemssize = mapItems.size();
    }
    
    @Override
    public void render(float delta) { //method repeats a lot
    	
    	//only draw mapItems if one gets picked up
    	if (!(mapItems.size() == tempMapItemssize)) {
    		tempMapItemssize = mapItems.size();
    	}
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();
    
        //updates - player position
        inputHandler.update();
        player.update(delta, 2, co);
        player2.update(delta, 1, co1);
        
        //comment out ai player line to run correctly
//       aiPlayer.update(delta);
        
        //draws tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();
        
        
        game.batch.begin();
      
        //draw collectibles
        drawCollectibles();
      
        System.out.println("here");
        System.out.println(player.position.getX() + " , " + co.nearestItem(player).getPosition().getX());
		System.out.println(player2.position.getX() + " , " + co1.nearestItem(player2).getPosition().getX());
        //Collectibles pick up
	    if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception	
	        if ((player.position.getX() > co.nearestItem(player).getPosition().getX()) && (player.position.getX() < co.nearestItem(player).getPosition().getX()+100) && 
	            (player.position.getY() > co.nearestItem(player).getPosition().getY()) && (player.position.getY() < co.nearestItem(player).getPosition().getY()+100)){
	        	System.out.println("over item");
	        	pickUpItem();
	        	
	        }
		}
		if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception
			if ((player2.position.getX() > co1.nearestItem(player2).getPosition().getX()) && (player2.position.getX() < co1.nearestItem(player2).getPosition().getX()+100) &&
					(player2.position.getY() > co1.nearestItem(player2).getPosition().getY()) && (player2.position.getY() < co1.nearestItem(player2).getPosition().getY()+100)){
				System.out.println("over item");
				pickUpItem();

			}
		}
	    
	    drawExitButton();
        
        int iconSize = 30;
        int buffer = 10;
        
        drawIcons(iconSize,buffer);
        
        player.render(game.batch);
        player2.render(game.batch);

        game.batch.end();
        
        
        //camera
        
        //follow player
        
        cam.update(500,500);
        
    }
    
    private void drawIcons(int iconSize, int buffer) {

        //draw hearts in top left corner
 
        int xheart = buffer;
        int yheart = VIEWPORT_HEIGHT - iconSize -buffer;
        int lives = player2.getHealth();
        for(int i = 0; i < lives; i ++) {
        	  game.batch.draw(heartTexture, xheart, yheart,iconSize , iconSize);
        	  xheart += (iconSize + buffer);
        }
        if (player.items.contains("shield")) {
        //draw shield icon
	        float shieldSize = 50;
	        float xShield = VIEWPORT_WIDTH - shieldSize -buffer;
	        float yShield = VIEWPORT_HEIGHT - (shieldSize *3) ;
	        game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);
        }
        //sword icon
        if ( player2.items.contains("sword")) {
	        float swordSize = 50;
	        float xSword = VIEWPORT_WIDTH  - swordSize - buffer;
	        float ySword = VIEWPORT_HEIGHT - (swordSize *2) ;
	        game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);
        }
        //draws coin icon
        for ( int i = 0; i < player2.coins; i ++ ) {
	        float xCoin = buffer;
	        float yCoin = VIEWPORT_HEIGHT - ( iconSize*3) -buffer;
	        game.batch.draw(coinTexture, xCoin + (i*10), yCoin,iconSize*2 , iconSize*2);
        }
    }
    
    private void drawExitButton() {
    	float x = VIEWPORT_WIDTH  - EXIT_WIDTH;
        float y = VIEWPORT_HEIGHT - EXIT_HEIGHT;
        
        //exit button in top right corner
        game.batch.draw(exitButtonActive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            //cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
            this.dispose();
            game.setScreen(new MenuScreen(this.game));
        }
        if (Gdx.input.getX() < (x + EXIT_WIDTH) && Gdx.input.getX() > x && MazeGame.HEIGHT - Gdx.input.getY() > EXIT_Y && MazeGame.HEIGHT - Gdx.input.getY() < EXIT_Y + EXIT_HEIGHT) {
           game.batch.draw(exitButtonActive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
           if (Gdx.input.isTouched())
                Gdx.app.exit();
        }
       else {
            game.batch.draw(exitButtonInactive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
        }
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
  	    		texture = healingPotionTexture;
  	    	}
  	    	if (mapItems.get(i).getType() == "compass") {
  	    		texture = compassTexture;
  	    	}
  	    	
  		    int x = mapItems.get(i).getPosition().getX(); 
  		    int y = mapItems.get(i).getPosition().getY();
  		    game.batch.draw(texture,x, y ,100,100);   	
  	    }

    }
    
    private void pickUpItem() {
    	Item item =  co.nearestItem(player);
    	
    	if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")) {
    		item = co.pickedUp(co.nearestItem(player));
    		
			if (item.getType() == "shield") {
				co.shield(item, player);
			}
			if (item.getType() == "sword") {
				co.sword(item, player, player3);
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
		}
		Item item1 =  co1.nearestItem(player2);

		if (!(player2.items.contains(item1.getType())) && !(item1.getType() == "coin")) {
			item1 = co1.pickedUp(co1.nearestItem(player2));

			if (item1.getType() == "shield") {
				co1.shield(item1, player2);
			}
			if (item1.getType() == "sword") {
				co1.sword(item1, player2, player3);
			}
			if (item1.getType() == "healingPotion") {
				co1.healingPotion (player2);
			}
			if (item1.getType() == "damagingPotion") {
				co1.damagingPotion(item1, player2);
			}
			if (item1.getType() == "gearEnchantment") {
				co1.gearEnchantment(item1 , player2);
			}
		} else if (item1.getType() == "coin") {
			mapItems.remove(item1);
			player2.coins++;
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
        player2.dispose();
        mapItems.clear();
    }

    @Override
    public void dispose() {
        tileMap.dispose();
        exitButtonActive.dispose();
        exitButtonInactive.dispose();
        player.dispose();
        player2.dispose();
        mapItems.clear();
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
			Coordinate position = new Coordinate(0,0);
			
			
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			
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
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);			
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("coin", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
//				System.out.println(position);
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
		for (int i = 0; i <= maxShields; i++) {
			Coordinate position = new Coordinate(0,0);


			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);

			Item item = new Item("shield", position);
			System.out.println(position);
			if(!(positions.contains(position.toString())) && !(player2.isCellBlocked((float)position.getX(), (float)position.getY()))) {
//				System.out.println(position);
				mapItems.add(item);
				System.out.println("adding to positions");
				positions.add(position.toString());
			}
		}

		for (int i = 0; i < maxCoins; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("coin", position);
			if(!(positions.contains(position.toString())) && !(player.isCellBlocked((float)position.getX(), (float)position.getY()))) {
//				System.out.println(position);
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxSwords; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("sword", position);
			if(!(positions.contains(position.toString())) && !(player2.isCellBlocked((float)position.getX(), (float)position.getY()))) {
				mapItems.add(item);
				positions.add(position.toString());
			}

		}

		for (int i = 0; i < maxCompasses; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("compass", position);
			if(!(positions.contains(position.toString())) && !(player2.isCellBlocked((float)position.getX(), (float)position.getY()))) {
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
				if(!(positions.contains(position.toString())) && !(player2.isCellBlocked((float)position.getX(), (float)position.getY()))) {
					mapItems.add(item);
					positions.add(position.toString());
				}
			} else if (whatPotion == 2) {
				Item item = new Item("damagingPotion", position);
				if(!(positions.contains(position.toString())) && !(player2.isCellBlocked((float)position.getX(), (float)position.getY()))) {
					mapItems.add(item);
					positions.add(position.toString());
				}
			} else {
				Item item = new Item("gearEnchantment", position);
				if(!(positions.contains(position.toString())) && !(player2.isCellBlocked((float)position.getX(), (float)position.getY()))) {
					mapItems.add(item);
					positions.add(position.toString());
				}
			}
		}
		System.out.println(positions);
	}
}
