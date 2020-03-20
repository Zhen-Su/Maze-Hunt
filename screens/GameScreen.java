package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.project.mazegame.networking.Messagess.AINewMessage;
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



public class GameScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;

    private Player player;
    private AIPlayer aiPlayer;// ---------need to be implemented
	private ArrayList<AIPlayer> aiPlayers;
    private InputHandler inputHandler;
    private AIPlayer aiPlayerAttack;
    private Player playerAttack;
    private float delta;
    private int numOfAI;
    private int posAP;
    private int posAAI;

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;
    private TiledMapTileLayer collisionLayer1;

    private Texture exitButtonActive;
    private Texture exitButtonInactive;
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

    private int updateCount;
    
    
    AnimationTool coinAnimation;
    
    private float timer;
    public float worldTimer;
    
    private Player player2;
    
    private Collect co;
    private Collect co1;
    private ArrayList<Collect> aicos;
 
    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
    
    private float initialisedShieldTime;
    private float initialisedPotionTime;
   
    public static ArrayList<Item> mapItems = new ArrayList<Item>();

    private final int EXIT_Y = VIEWPORT_HEIGHT;
    
    private int tempMapItemssize;
    
    int overlayWidth;
    int overlayHeight;
    int keyFrame;
   
    
    
    public GameScreen(MazeGame game) {
        this.game = game;
        this.updateCount = 0;
        
        aiPlayerAttack = null;
        playerAttack = null;
      
        inputHandler = new InputHandler();
        
        timer = 0;
        worldTimer = 120;

        tileMap = new TmxMapLoader().load("Map1.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
        
        this.collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
		this.collisionLayer1 = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
        player = new Player(this.collisionLayer,"james",123);
        aiPlayer = new AIPlayer(this.collisionLayer1, "Albert", 124);
        aiPlayers = aiPlayer.AITakingOver(3);
        aicos = new ArrayList<Collect>();


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
        
        overlayWidth = overlay.getWidth() +300;
        overlayHeight = overlay.getHeight() +300;
        
        //coinAnimation = new AnimationTool(50,50,player,coinPick,true);
        //coinAnimation.create();
    }
    
    @Override
    public void show() {
		generateMapItems((int) collisionLayer.getWidth(), 100 );
		co = new Collect(game, player);
		for (int i = 0; i < aiPlayers.size(); i++) {
			aicos.add(new Collect(game, aiPlayers.get(i)));
		}
		tempMapItemssize = mapItems.size();
		//start timer
		player.initialPosition();

		for (int i = 0; i < aiPlayers.size(); i++) {

            aiPlayers.get(i).initialPosition();

		}

    }
    int iconSize = 30;
    @Override
    public void render(float delta) { //method repeats a lot
    	
    	updateTime(delta);
    	removeShield();
    	playerDamaging();
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();
    
        //updates - player position

        inputHandler.update();

		ArrayList<Item> empty = new ArrayList<>();

        player.update(delta, 0, empty, 0);


        for (int i = 0; i < aiPlayers.size(); i++) {
				aiPlayers.get(i).update(delta, 1, mapItems, worldTimer);
//

		}


        //camera
        cam.update(player.position.getX(),player.position.getY(),game);

        
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

		// will need to add an ai there

		aiMultiPickUp();

	    game.batch.draw(overlay,player.position.getX() - overlayWidth/2,player.position.getY() - overlayHeight/2 , overlayWidth ,overlayHeight);
//        game.batch.draw(overlay, aiPlayer.position.getX() - overlayWidth/2, aiPlayer.position.getY() - overlayHeight/2, overlayWidth, overlayHeight);
        
        int buffer = 10;
        Coordinate playerPos = new Coordinate(player.position.getX(), player.position.getY());
        drawIcons(iconSize,buffer,playerPos);
        drawExitButton(playerPos);
        // first check is for players
		// first call players
		ArrayList<Player> emptyattack = new ArrayList<>();
		if(isPlayerOnSameP(player, emptyattack, aiPlayers)) {
			if(isHuman1()) {
				player.attackP(emptyattack.get(posAP), worldTimer);
				emptyattack.get(posAP).x = emptyattack.get(posAP).moveTo.getX();
				emptyattack.get(posAP).y = emptyattack.get(posAP).moveTo.getY();
			} else {
				AIPlayer playerinsert = aiPlayers.remove(posAAI);
				player.attackAI(playerinsert, worldTimer);
				playerinsert.x = playerinsert.moveTo.getX();
				playerinsert.y = playerinsert.moveTo.getY();
				aiPlayers.add(posAAI, playerinsert);
			}
		}
		// go through list one by one remove the player from that list
		ArrayList<Player> forAI = new ArrayList<>();
		forAI.add(player);
		for (int i = 0; i < aiPlayers.size(); i++) {
			AIPlayer playerTurn = aiPlayers.remove(i);
			if (isPlayerOnSameAI(playerTurn, forAI, aiPlayers)) {
				if(isHuman1()) {
					System.out.println("An ai is about to attack me");
					playerTurn.attackP(forAI.get(posAP), worldTimer);
					forAI.get(posAP).x = forAI.get(posAP).moveTo.getX();
					forAI.get(posAP).y = forAI.get(posAP).moveTo.getY();
				} else {
					player.attackAI(aiPlayers.get(posAAI), worldTimer);
					aiPlayers.get(posAAI).x = aiPlayers.get(posAAI).moveTo.getX();
					aiPlayers.get(posAAI).y = aiPlayers.get(posAAI).moveTo.getY();
				}
			}
			aiPlayers.add(i, playerTurn);
		}
        /*
		for (int i = 0; i< aiPlayers.size(); i++) {
			aiPlayers.get(i).x = aiPlayers.get(i).moveTo.getX();
        	aiPlayers.get(i).y = aiPlayers.get(i).moveTo.getY();
		}
		*/

        player.render(game.batch);
//        aiPlayer.render(game.batch);

        for (int i = 0; i < aiPlayers.size(); i++) {
//        	aiPlayers.get(i).x = aiPlayers.get(i).moveTo.getX();
//        	aiPlayers.get(i).y = aiPlayers.get(i).moveTo.getY();
        	aiPlayers.get(i).render(game.batch);
		}
        
        
        String message = "Time = " + worldTimer ;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
        font.draw(game.batch,message, player.position.getX(),player.position.getY() + VIEWPORT_HEIGHT/2 -10);
        
        game.batch.end();
        
    	//if timer runs out 
    	if(worldTimer < 3) {
    		overlayWidth -= 15;
    		overlayHeight -= 15;
    	  
    	   if(worldTimer < 0) {
    		   this.dispose();
    		   game.setScreen(new EndScreen(this.game));
    		  
    	   }
    	}
        
    }
    private boolean isHuman1() {
    	if (aiPlayerAttack != null && playerAttack == null) {
    		return false;
		} else if (aiPlayerAttack == null && playerAttack != null) {
    		return true;
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("This should not be happening");
			}
		}
    	return true;
	}

    private boolean isPlayerOnSameAI(AIPlayer current, ArrayList<Player> playersA, ArrayList<AIPlayer> aiPlayersA) {
    	// first thing cycle through list checking and comparing coordinates
		// if one person on same square that is the player attack return true and set attackplayer
		this.aiPlayerAttack = null;
		this.playerAttack = null;
		Coordinate currentC = new Coordinate(current.x, current.y);
		if (!playersA.isEmpty()) {
			for (int i = 0; i < playersA.size(); i++) {
				Coordinate currentA = new Coordinate(playersA.get(i).x, playersA.get(i).y);
				if (sameSpace(currentC, currentA)) {
					this.playerAttack = playersA.get(i);
					this.aiPlayerAttack = null;
					this.posAP = i;
					return true;
				}
			}
		}
		if (!aiPlayersA.isEmpty()) {
			for (int i = 0; i < aiPlayersA.size(); i++) {
				Coordinate currentA = new Coordinate(aiPlayersA.get(i).x, aiPlayersA.get(i).y);
				if (sameSpace(currentC, currentA)) {
					this.playerAttack = null;
					this.aiPlayerAttack = aiPlayersA.get(i);
					this.posAAI = i;
					return true;
				}
			}
		}
		return false;

	}
	private boolean isPlayerOnSameP(Player current, ArrayList<Player> playersA, ArrayList<AIPlayer> aiPlayersA) {
		// first thing cycle through list checking and comparing coordinates
		// if one person on same square that is the player attack return true and set attackplayer
		this.aiPlayerAttack = null;
		this.playerAttack = null;
		Coordinate currentC = new Coordinate(current.x, current.y);
		if (!playersA.isEmpty()) {
			for (int i = 0; i < playersA.size(); i++) {
				Coordinate currentA = new Coordinate(playersA.get(i).x, playersA.get(i).y);
				if (sameSpace(currentC, currentA)) {
					this.playerAttack = playersA.get(i);
					this.aiPlayerAttack = null;
					this.posAP = i;
					return true;
				}
			}
		}
		if (!aiPlayersA.isEmpty()) {
			for (int i = 0; i < aiPlayersA.size(); i++) {
				Coordinate currentA = new Coordinate(aiPlayersA.get(i).x, aiPlayersA.get(i).y);
				if (sameSpace(currentC, currentA)) {
					this.playerAttack = null;
					this.aiPlayerAttack = aiPlayersA.get(i);
					this.posAAI = i;
					return true;
				}
			}
		}
		return false;

	}
	private boolean sameSpace(Coordinate investigation, Coordinate check) {
    	// get  coordinates then abs and check difference
		int xCorI = investigation.getX();
		int xCorC = check.getX();
		int yCorI = investigation.getY();
		int yCorC = check.getY();
		int xdist = Math.abs(xCorI - xCorC);
		int ydist = Math.abs(yCorI - yCorC);
		return (xdist <= 100 && ydist <= 100);
	}

    private void aiMultiPickUp() {
    	for (int i  = 0; i < aiPlayers.size(); i++) {
			if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception
				if ((aiPlayers.get(i).position.getX() > aicos.get(i).nearestItem(aiPlayers.get(i)).getPosition().getX()) && (aiPlayers.get(i).position.getX() < aicos.get(i).nearestItem(aiPlayers.get(i)).getPosition().getX() + 100) &&
						(aiPlayers.get(i).position.getY() > aicos.get(i).nearestItem(aiPlayers.get(i)).getPosition().getY()) && (aiPlayers.get(i).position.getY() < aicos.get(i).nearestItem(aiPlayers.get(i)).getPosition().getY() + 100)) {

					aiPickUp(aiPlayers.get(i), aicos.get(i));

				}
			}
		}
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
	        float yShield = VIEWPORT_HEIGHT - (shieldSize *3) + playerY;
	        game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);
	        
	        String message = "XP :" + player.getShieldXP() ;
	        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
	        font.draw(game.batch,message, xShield ,yShield -50);
        }
        //sword icon
        if ( player.items.contains("sword")) {
	        float swordSize = 50;
	        float xSword = VIEWPORT_WIDTH  - swordSize - buffer + playerX;
	        float ySword = VIEWPORT_HEIGHT - (swordSize *2) + playerY;
	        game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);
	        
	        
	        String message = "XP :" + player.getswordXP(); ;
	        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);
	        font.draw(game.batch,message, xSword ,ySword -50);
        }
        
       
        
        //draws coin icon
        for ( int i = 0; i < player.coins; i ++ ) {
	        float xCoin = buffer + playerX;
	        float yCoin = VIEWPORT_HEIGHT - ( iconSize*3) -buffer + playerY;
	        if ( coinSize != iconSize*2) coinSize -=5;
	        game.batch.draw(coinTexture, xCoin + (i*10), yCoin, coinSize,coinSize);
        }
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
    	
    	if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")) {
    		item = co.pickedUp(co.nearestItem(player));
    		
    		
			if (item.getType() == "shield") {
				item.setInitialisedTime(worldTimer);
				initialisedShieldTime = worldTimer;
				co.shield(item, player);
			}
			if (item.getType() == "sword") {
				co.sword(item, player, player2);
			}
			if (item.getType() == "healingPotion") {
				player.loadPlayerTextures();
				co.healingPotion (player);
			}
			if (item.getType() == "damagingPotion") {
				item.setInitialisedTime(worldTimer);
				initialisedPotionTime = worldTimer;
				co.damagingPotion(item, player);
				
				//System.out.println("posion");
			}
			if (item.getType() == "gearEnchantment") {
				co.gearEnchantment(item , player);
			}
		} else if (item.getType() == "coin") {
			mapItems.remove(item);
			player.coins++;
			//coinAnimation.render();
			
			//animateCoin();
			//coinSize = 100;
	
		
		}
    }

    private void aiPickUp(AIPlayer player, Collect aico) {
    	Item item = aico.nearestItem(player);
    	if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")) {
    		item = aico.pickedUp(aico.nearestItem(player));

    		if (item.getType() == "shield") {
    			item.setInitialisedTime(worldTimer);
    			initialisedShieldTime = worldTimer;
    			aico.shield(item, player);
			}
    		if (item.getType() == "sword") {
    			aico.sword(item, player, player2);
			}
    		if (item.getType() == "healingPotion") {
    			player.loadPlayerTextures();
    			aico.healingPotion(player);
			}
    		if (item.getType() == "damagingPotion") {
    			item.setInitialisedTime(worldTimer);
    			initialisedPotionTime = worldTimer;
    			aico.damagingPotion(item, player);
			}
    		if (item.getType() == "gearEnchantment") {
    			aico.gearEnchantment(item, player);
			}

		} else if (item.getType() == "coin") {
    		mapItems.remove(item);
    		player.coins++;
		}

	}
    
    private void animateCoin() {
    	
    	TextureRegion[] region = coinAnimation.getFrames();
    	for(int i = 0 ; i < 4 ; i = keyFrame ) {
    		game.batch.draw(region[keyFrame],player.position.getX() - 50/2,player.position.getY() - 50/2);
    		
    	}
    	
    }
    
    private void removeShield() {
    	if(!player.items.contains("shield")) {
    		return;
    	}
    	if (initialisedShieldTime - worldTimer == 10) {
    		player.items.remove("shield");
    	}
    }
    
    private void playerDamaging() {
    	if(!player.items.contains("damagingPotion")) {
    		return;
    	}
    	if (initialisedPotionTime - worldTimer == 2) {
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
        aiPlayer.dispose();
        mapItems.clear();
//        cam.update(V_WIDTH/2, V_HEIGHT/2, game);
        cam.cam.viewportHeight = 1000;
        cam.cam.viewportWidth = 1000;
        cam.update(V_WIDTH/2, V_HEIGHT/2, game);
        cam.cam.update();
        
    }
    
    public void generateMapItems( int widthInTiles, int tileWidth ) {
        HashSet<String> positions = new HashSet<String>();
    	int maxShields = 30;
		int maxCoins = 100;
		int maxSwords = 50;
		int maxCompasses = 30;
		int maxPotions = 100;
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
    	timer += dt;
    	if (timer >= 1) {
    		worldTimer--;
//    		System.out.println("World Timer: " + worldTimer);
    		timer = 0;
    	}
    }
}
