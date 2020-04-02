package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.project.mazegame.objects.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.IntIntMap;
import com.project.mazegame.MazeGame;
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
	private Player player;
	private AIPlayer aiPlayer;// ---------need to be implemented

	public ArrayList<AIPlayer> aiPlayers;
	
	private int posAP;
	private int posAAI;

	private AIPlayer aiPlayerAttack;
	private Player playerAttack;
	private InputHandler inputHandler;
	private float delta;

	private TiledMap tileMap;//
	private OrthogonalTiledMapRenderer tileMapRenderer;//
	private TiledMapTileLayer collisionLayer,collisionLayer1;

	private Texture exitButtonActive,exitButtonInactive;

	private Texture heartTexture;
	private Texture coinTexture;
	private Texture swordTexture;
	private Texture shieldTexture;
	private Texture minimapTexture;
	private Texture healingPotionTexture;
	private Texture damagingPotionTexture;
	private Texture gearEnchantmentTexture;
	private Texture audioButtonActive; //-------need to  implemented
	private Texture audioButtonInactive;
	private Texture overlay;
	private Texture coinPick;
	private BitmapFont font;
	private Texture mapTexture, minimapOutline, playerIcon;

	private Texture enchantedGlow;

	//    private float timer;
	public static float worldTimer;

	Timer time = new Timer();

	private Player player2;

	private Collect co;

	private final int EXIT_WIDTH = 50;
	private final int EXIT_HEIGHT = 20;

	public static ArrayList<Item> mapItems = new ArrayList<Item>();

	private final int EXIT_Y = VIEWPORT_HEIGHT;

	private int tempMapItemssize;

	private MazeGame game;
	private OrthoCam cam;




	private int numOfAI;
	private String map;
	private String playerSkin;
	private String AIDifficulty;

	private AssetManager manager;

	int overlayWidth;
	int overlayHeight;
	int keyFrame;

	Coordinate playerPos;
	
	 private ArrayList<Collect> aicos;

	public GameScreen(MazeGame game) {
		this.game = game;
		
		 aiPlayerAttack = null;
		 playerAttack = null;

		aicos = new ArrayList<Collect>();


		inputHandler = new InputHandler();

		worldTimer = 30;
		aiPlayers = new ArrayList<AIPlayer> ();


		// read csv file
		ArrayList<String> output = CSVStuff.readCSVFile("csvFile");

		this.map = output.get(0);
		this.playerSkin = output.get(1);
		this.AIDifficulty = output.get(2);
		this.numOfAI = Integer.parseInt(output.get(3));
		String name = output.get(4);


		if(this.map.equals( "map1")) {
			tileMap = new TmxMapLoader().load("Map1.tmx");
			mapTexture = Assets.manager.get(Assets.map1Icon, Texture.class) ;
		}
		else if(this.map.equals("map2")) {
			tileMap = new TmxMapLoader().load("Map2.tmx");
			mapTexture = Assets.manager.get(Assets.map2Icon, Texture.class);
		}
		else {
			tileMap = new TmxMapLoader().load("Map3.tmx");
			mapTexture = Assets.manager.get(Assets.map3Icon, Texture.class);
		}

		tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

		collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
		collisionLayer1 = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

		player = new Player(this.collisionLayer,name,123 , this.playerSkin,PlayersType.single);
		
		
        player.initialPosition();
       
            
        aiPlayer = new AIPlayer(this.collisionLayer1, "Albert", 124, "red" , PlayersType.single);
        aiPlayers = aiPlayer.AITakingOver(2);

        
    
        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, player.position.getX(),player.position.getY());

		collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");







		//coinAnimation = new AnimationTool(50,50,player,coinPick,true);
		//coinAnimation.create();
	}

	public void getAsset(){
		// buttons

		exitButtonActive = Assets.manager.get(Assets.exit_button_active,Texture.class);
		exitButtonInactive = Assets.manager.get(Assets.exit_button_inactive,Texture.class);
		audioButtonActive = Assets.manager.get(Assets.audioOn,Texture.class);
		audioButtonInactive= Assets.manager.get(Assets.audioOff,Texture.class);
		heartTexture = Assets.manager.get(Assets.heart,Texture.class);
		coinTexture = Assets.manager.get(Assets.coin,Texture.class);
		swordTexture = Assets.manager.get(Assets.sword,Texture.class);
		shieldTexture = Assets.manager.get(Assets.shield,Texture.class);
		healingPotionTexture = Assets.manager.get(Assets.Potion,Texture.class);
		gearEnchantmentTexture = Assets.manager.get(Assets.Potion2,Texture.class);
		damagingPotionTexture = Assets.manager.get(Assets.Potion3,Texture.class);
		minimapTexture = Assets.manager.get(Assets.RolledMap,Texture.class);
		overlay = Assets.manager.get(Assets.circularOverlay,Texture.class);
		coinPick= Assets.manager.get(Assets.coinAnimation,Texture.class);
		minimapOutline = Assets.manager.get(Assets.minimapOutline,Texture.class);
		enchantedGlow = Assets.manager.get(Assets.ENCHANTED,Texture.class);
		playerIcon = Assets.manager.get(Assets.playerOnMap,Texture.class);


		overlayWidth = overlay.getWidth() +300;
		overlayHeight = overlay.getHeight() +300;


	}

	private void writeCoinCSV() {
		ArrayList<String> input = new ArrayList<>();




		input.add(player.getName() + " = " + player.coins);

    	for(int i = 0; i < numOfAI; i ++) {
    		input.add(aiPlayers.get(i).getName() + " = " + aiPlayers.get(i).coins);
    	}


		CSVStuff.writeCSV(input , "coinCSV");
	}

	@Override
	public void show() {

		getAsset();

		//assuming it's a square map -> only need width of map and width of tile
		generateMapItems((int) collisionLayer.getWidth(), 100 );
		co = new Collect(player);
		tempMapItemssize = mapItems.size();
		//start timer
		player.initialPosition();
		font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);

		playerPos = new Coordinate(player.position.getX(), player.position.getY());
		
		
		for (int i = 0; i < aiPlayers.size(); i++) {
			aicos.add(new Collect(aiPlayers.get(i)));
		}
		
		for (int i = 0; i < aiPlayers.size(); i++) {

            aiPlayers.get(i).initialPosition();

		}


	}
	int iconSize = 30;
	@Override
	public void render(float delta) { //method repeats a lot

		updateTime(delta);
//    	player.removeShield();
//    	removeEnchantment();


		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		delta = Gdx.graphics.getDeltaTime();

		//updates - player position
		inputHandler.update();
		player.update(delta,0,0);
		
		for (int i = 0; i < aiPlayers.size(); i++) {
			aiPlayers.get(i).update(delta, 2,worldTimer);

		}

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
		
		aiMultiPickUp();

		game.batch.draw(overlay,player.position.getX() - overlayWidth/2,player.position.getY() - overlayHeight/2 , overlayWidth ,overlayHeight);


		int buffer = 10;
		playerPos.setX(player.position.getX());
		playerPos.setY(player.position.getY());
		drawIcons(iconSize,buffer,playerPos);
		drawExitButton(playerPos);
		
		
		// genereates list of players at the moment empty but with networking will be changed
				ArrayList<Player> emptyattack = new ArrayList<>();
//				emptyattack.add(player);
				// first checks for the player to see if there is a player
				if(isPlayerOnSameP(player, emptyattack, aiPlayers)) {
					System.out.println("on same");
					//checks if the player is human or not
					if(isHuman1()) {
						System.out.println("is human");
						//if player is human removes the player from the list then calls attack
						emptyattack.remove(0);
						player.attackP(emptyattack.get(posAP), worldTimer);
						// earlier issue with not respawning so checks the move to and changes move to to coords generated by death
						emptyattack.get(posAP).x = emptyattack.get(posAP).moveTo.getX();
						emptyattack.get(posAP).y = emptyattack.get(posAP).moveTo.getY();
						// adds player back to list
						emptyattack.add(player);
					} else {
						System.out.println("is AI ");
						// does same thing as above except iwth ai plaeyrs
						AIPlayer playerinsert = aiPlayers.remove(posAAI);
						System.out.println("before attack");
						System.out.println("after attack");
						player.attackAI(playerinsert, worldTimer);
						playerinsert.x = playerinsert.moveTo.getX();
						playerinsert.y = playerinsert.moveTo.getY();
						aiPlayers.add(posAAI, playerinsert);
						System.out.println("aiplayers.added");
					}
				}
				// creates list of players and adds them so the ai can attack players
				ArrayList<Player> forAI = new ArrayList<>();
				forAI.add(player);
				// goes throught the list of ai players
				for (int i = 0; i < aiPlayers.size(); i++) {
					// takes one player to attack
					AIPlayer playerTurn = aiPlayers.remove(i);
					// does the same thing as the above lines of code just with ai
					if (isPlayerOnSameAI(playerTurn, forAI, aiPlayers)) {
						if(isHuman1()) {
							System.out.println("An ai is about to attack me");
							playerTurn.attackP(forAI.get(posAP), worldTimer);
							forAI.get(posAP).x = forAI.get(posAP).moveTo.getX();
							forAI.get(posAP).y = forAI.get(posAP).moveTo.getY();
						} else {
							playerTurn.attackAI(aiPlayers.get(posAAI), worldTimer);
							aiPlayers.get(posAAI).x = aiPlayers.get(posAAI).moveTo.getX();
							aiPlayers.get(posAAI).y = aiPlayers.get(posAAI).moveTo.getY();
						}
					}
					// adds the ai plaeyr back into the list so it is avaible to be attacked
					aiPlayers.add(i, playerTurn);
				}



		if(player.items.contains("gearEnchantment"))game.batch.draw(enchantedGlow ,player.position.getX() -enchantedGlow.getWidth()/2 ,player.position.getY() - enchantedGlow.getHeight()/2 , enchantedGlow.getWidth() ,enchantedGlow.getHeight());


		player.render(game.batch);
		player.attack();
		
		for(int i = 0 ; i < numOfAI; i ++) {
	         
            aiPlayers.get(i).render(game.batch);
        }

		String message = "Time = " + (int) (worldTimer - (time.currentTime())) ;


		font.draw(game.batch,message, player.position.getX(),player.position.getY() + VIEWPORT_HEIGHT/2 -10);

		//if timer runs out
		if((worldTimer - time.currentTime()) < 3) {
			overlayWidth -= 15;
			overlayHeight -= 15;

			if((worldTimer - time.currentTime()) < 0) {
				this.dispose();

				writeCoinCSV();
				 
				if( numOfAI != 0) {
					AIPlayer AIwinner = aiPlayers.get(0);
					
					
					
					int maxCoins = 0;
					for(int i = 0; i < numOfAI; i++) {
						int tempCoins = aiPlayers.get(i).coins;
						if(tempCoins > maxCoins) {
							maxCoins = tempCoins;
							AIwinner = aiPlayers.get(i);
							
						}
					}
					
					if(AIwinner.coins > player.coins) {
						//then AI have won
						player.setName( AIwinner.getName());
						player.coins = AIwinner.coins;
						player.setColour(AIwinner.getColour());
					}
				}
				
				game.setScreen(new EndScreen(this.game , player,false));

			}
		}
		game.batch.end();
	}
	
	/**
	 * Method checks if a player is a human or ai
	 * @return boolean
	 */
	private boolean isHuman1() {
    	if (aiPlayerAttack != null && playerAttack == null) {
    		// checks whetehr one or the other is null
    		return false;
		} else if (aiPlayerAttack == null && playerAttack != null) {
    		return true;
    		// shouldn't return antying so will through exception
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("This should not be happening");
			}
		}
    	return true;
	}

	/**
	 * Checks if a ai player is on the same space as an ai or human player
	 * @param current
	 * @param playersA
	 * @param aiPlayersA
	 * @return
	 */
    private boolean isPlayerOnSameAI(AIPlayer current, ArrayList<Player> playersA, ArrayList<AIPlayer> aiPlayersA) {
    	// if one person on same square that is the player attack return true and set attackplayer
		// starts by setting both to null

		this.aiPlayerAttack = null;
		this.playerAttack = null;
		// gets the current coordiantes of the current player
		Coordinate currentC = new Coordinate(current.x, current.y);
		if (!playersA.isEmpty()) {
			for (int i = 0; i < playersA.size(); i++) {
				// cycles through the players checking if thecorroridnates are the same
				Coordinate currentA = new Coordinate(playersA.get(i).x, playersA.get(i).y);
				if (sameSpace(currentC, currentA)) {
					// sets the player to attack globally
					this.playerAttack = playersA.get(i);
					// makes usre ai is null
					this.aiPlayerAttack = null;
					// logs the index so it can be used later
					this.posAP = i;
					return true;
				}
			}
		}
		if (!aiPlayersA.isEmpty()) {
			// does the same thing as the above method
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
		// if the method doesn't return true throught it will return false
		return false;

	}

	/**
	 * Cheks if a human player is on the same space as an ai or player
	 * @param current
	 * @param playersA
	 * @param aiPlayersA
	 * @return
	 */
	

	
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

	/**
	 * works out if two coordinates are the same or not
	 * @param investigation
	 * @param check
	 * @return
	 */
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
			float  remaining = (player.getTime() - player.initialisedShieldTime);

			float shieldSize = 50;
			float xShield = VIEWPORT_WIDTH - shieldSize -buffer + playerX;
			float yShield = VIEWPORT_HEIGHT - (shieldSize *3) + playerY -50;
			game.batch.draw(shieldTexture, xShield, yShield,shieldSize - remaining *2, shieldSize - remaining*2);

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
		float xMap = player.getX() + VIEWPORT_WIDTH /2 - mapSize - 50;
		float yMap =  player.getY() - VIEWPORT_HEIGHT/2 + 50;


		if(player.items.contains("minimap")) {
			game.batch.draw(minimapOutline, xMap -8 ,yMap -9,mapSize + 17 , mapSize +17);
			game.batch.draw(mapTexture, xMap,yMap,mapSize , mapSize);

			//draw player position

			int x =( player.position.getX() - 500 + 60 )/20;
			int y = (player.position.getY() - 500 + 80)/20;
			game.batch.draw(playerIcon, xMap + x ,yMap + y,5,5);
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
	
	private void aiPickUp(AIPlayer player, Collect aico) {
		Item item =  aico.nearestItem(player);



		if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")&& !(item.getType() == "healingPotion")&& !(item.getType() == "damagingPotion")) {
			item = aico.pickedUp(aico.nearestItem(player));


			if (item.getType() == "shield") {
				item.setInitialisedTime((time.currentTime()));
				player.initialisedShieldTime = time.currentTime();
				aico.sword(item, player);
				if(player.items.contains("gearEnchantment")) {
					player.initialisedShieldTime += 3;
				}
			}
			if (item.getType() == "sword") {
				aico.sword(item, player);
			}


			if (item.getType() == "gearEnchantment") {
				aico.gearEnchantment(item , player);
				player.initialisedEnchantmentTime = time.currentTime();
				if(player.items.contains("shield"))
					player.initialisedShieldTime += 3;
			}
			if(item.getType() == "minimap") {
				aico.minimap(item);
			}
		} else if (item.getType() == "coin") {
			mapItems.remove(item);
			player.coins++;
		}else if (item.getType() == "healingPotion") {
			mapItems.remove(item);
			aico.healingPotion (player);
		}else if (item.getType() == "damagingPotion") {
			mapItems.remove(item);
			aico.damagingPotion(player);
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
				texture = gearEnchantmentTexture;
			}
			if (mapItems.get(i).getType() == "minimap") {
				texture = minimapTexture;
			}

			int x = mapItems.get(i).getPosition().getX();
			int y = mapItems.get(i).getPosition().getY();
			game.batch.draw(texture,x, y ,100,100);
		}

	}
	// -------------------------------------------------could move to collect class
	private void pickUpItem() {
		Item item =  co.nearestItem(player);



		if (!(player.items.contains(item.getType())) && !(item.getType() == "coin")&& !(item.getType() == "healingPotion")&& !(item.getType() == "damagingPotion")) {
			item = co.pickedUp(co.nearestItem(player));


			if (item.getType() == "shield") {
				item.setInitialisedTime((time.currentTime()));
				player.initialisedShieldTime = time.currentTime();
				co.sword(item, player);
				if(player.items.contains("gearEnchantment")) {
					player.initialisedShieldTime += 3;
				}
			}
			if (item.getType() == "sword") {
				co.sword(item, player);
			}


			if (item.getType() == "gearEnchantment") {
				co.gearEnchantment(item , player);
				player.initialisedEnchantmentTime = time.currentTime();
				if(player.items.contains("shield"))
					player.initialisedShieldTime += 3;
			}
			if(item.getType() == "minimap") {
				co.minimap(item);
			}
		} else if (item.getType() == "coin") {
			mapItems.remove(item);
			player.coins++;
		}else if (item.getType() == "healingPotion") {
			mapItems.remove(item);
			co.healingPotion (player);
		}else if (item.getType() == "damagingPotion") {
			mapItems.remove(item);
			co.damagingPotion(player);
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
		int maxMinimaps = 15;
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

		for (int i = 0; i < maxMinimaps; i++) {
			Coordinate position = new Coordinate(0,0);
			position.changeX((int)((Math.random() * (maxX ))) * tileWidth);
			position.changeY((int)((Math.random() * (maxY )))* tileWidth);
			Item item = new Item("minimap", position);
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

		if (!(time.currentTime() == initial)) {
			worldTimer--;
		}
	}
}
