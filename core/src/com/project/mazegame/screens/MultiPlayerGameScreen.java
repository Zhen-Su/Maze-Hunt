package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Messagess.CollectMessage;
import com.project.mazegame.networking.Messagess.ItemCreateMessage;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.AIPlayer;
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.MultiCollect;
import com.project.mazegame.tools.OrthoCam;
import com.project.mazegame.tools.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//import static com.project.mazegame.screens.GameScreen.mapItems;
import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;
import static com.project.mazegame.tools.Variables.V_HEIGHT;
import static com.project.mazegame.tools.Variables.V_WIDTH;

public class MultiPlayerGameScreen implements Screen,InputProcessor {

    private boolean HostStartGame = false;

    //Item List List
    public ArrayList<Item> mapItems = new ArrayList<Item>();

    //Items position Set
    public static HashSet<String> positions = new HashSet<String>();

    private MazeGame game;
    private OrthoCam cam;

    private MultiPlayer myMultiPlayer;
    private NetClient netClient = new NetClient(this);
    private AIPlayer aiPlayer;
    private ArrayList<AIPlayer> aiPlayers;
    private List<MultiPlayer> players = new ArrayList<MultiPlayer>();
    public HashMap<Integer, Integer> playersIdIndexList = new HashMap<>();
    private boolean imHost;

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;
    private int tempMapItemssize;
    private MultiCollect co;
    private ArrayList<Collect> aicos;
    private MultiPlayer player2;

    private AssetManager manager;
    public Texture player, sword,swordAttack,swordNotAttack,shield;
    public  Texture frames,walkRight,walkLeft,walkUp,walkDown, coinPick , swipeRight , swipeLeft , swipeUp , swipeDown , playerDead;
    private Texture exitButtonActive;
    private Texture exitButtonInactive;
    private Texture heartTexture;
    private Texture coinTexture;
    private Texture swordTexture;
    private Texture shieldTexture;
    private Texture audioButtonActive;
    private Texture audioButtonInactive;
    private Texture compassTexture;
    private Texture healingPotionTexture;
    private Texture damagingPotionTexture;
    private Texture gearEnchantmentTexture;
    private Texture overlay;
    //private Texture coinPick;
    private BitmapFont font;
    private Texture enchantedGlow;
    private Texture mapTexture, minimapOutline;

    private float timer;
    public static float worldTimer;
    Timer time = new Timer();
    private float initialisedShieldTime;
    private float initialisedPotionTime;
    private float initialisedEnchantmentTime;
    int overlayWidth;
    int overlayHeight;

    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
    private final int EXIT_Y = VIEWPORT_HEIGHT;
    Coordinate playerPos;


    //=====================================constructors=============================================

    public MultiPlayerGameScreen(MazeGame game,String username,String serverIP) {
        this.game = game;

        timer = 0;
        worldTimer = 60;

        tileMap = new TmxMapLoader().load("Map1.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        manager = new AssetManager();
        loadAsset("orange");

        myMultiPlayer=new MultiPlayer(this.collisionLayer,username,this, Direction.STOP);
        netClient.connect(serverIP,GameServer.SERVER_TCP_PORT);

        //create AI player in multiplayer
        aiPlayer = new AIPlayer( (TiledMapTileLayer) tileMap.getLayers().get("wallLayer"), "Albert", 124);
        aiPlayers = aiPlayer.AITakingOver(5);
        aicos = new ArrayList<Collect>();

        Gdx.input.setInputProcessor(this);

        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, myMultiPlayer.position.getX(),myMultiPlayer.position.getY());

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
        enchantedGlow = new Texture("Player\\ENCHANTED.png");
        mapTexture = new Texture("Maps\\Map2Icon.png");
        minimapOutline = new Texture("Maps\\minimapOutline.png");

        overlayWidth = overlay.getWidth() +300;
        overlayHeight = overlay.getHeight() +300;
    }


    //===================================Getter&Setter==============================================

    public MultiPlayer getMultiPlayer() { return myMultiPlayer; }

    public void setMultiPlayer(MultiPlayer multiPlayer) { this.myMultiPlayer = multiPlayer; }

    public List<MultiPlayer> getPlayers() { return players; }

    public void setPlayers(List<MultiPlayer> players) { this.players = players; }

    public NetClient getNc() { return netClient; }

    public void setNc(NetClient netClient) { this.netClient = netClient; }

    public TiledMapTileLayer getCollisionLayer() { return collisionLayer; }

    public boolean isHostStartGame() { return HostStartGame; }

    public void setHostStartGame(boolean hostStartGame) { HostStartGame = hostStartGame; }

    public boolean isImHost() {
        return imHost;
    }

    public void setImHost(boolean imHost) {
        this.imHost = imHost;
    }

    //==============================================================================================

    public void loadAsset(String colour){
        switch (colour) {
            case "blue":
                manager.load("Player\\walkRightBlue.png",Texture.class);
                manager.load("Player\\walkLeftBlue.png",Texture.class);
                manager.load("Player\\walkUpBlue.png",Texture.class);
                manager.load("Player\\walkDownBlue.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player\\walkRightBlue.png",Texture.class);
                walkLeft = manager.get("Player\\walkLeftBlue.png",Texture.class);
                walkUp = manager.get("Player\\walkUpBlue.png",Texture.class);
                walkDown = manager.get("Player\\walkDownBlue.png",Texture.class);
                break;
            case "green":
                manager.load("Player\\walkRightGreen.png",Texture.class);
                manager.load("Player\\walkLeftGreen.png",Texture.class);
                manager.load("Player\\walkUpGreen.png",Texture.class);
                manager.load("Player\\walkDownGreen.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player\\walkRightGreen.png",Texture.class);
                walkLeft= manager.get("Player\\walkLeftGreen.png",Texture.class);
                walkUp = manager.get("Player\\walkUpGreen.png",Texture.class);
                walkDown =manager.get("Player\\walkDownGreen.png",Texture.class);
                break;
            case "pink":
                manager.load("Player\\walkRightPink.png",Texture.class);
                manager.load("Player\\walkLeftPink.png",Texture.class);
                manager.load("Player\\walkUpPink.png",Texture.class);
                manager.load("Player\\walkDownPink.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player\\walkRightPink.png",Texture.class);
                walkLeft= manager.get("Player\\walkLeftPink.png",Texture.class);
                walkUp = manager.get("Player\\walkUpPink.png",Texture.class);
                walkDown =manager.get("Player\\walkDownPink.png",Texture.class);
                break;
            case "orange":
                manager.load("Player\\walkRightOrange.png",Texture.class);
                manager.load("Player\\walkLeftOrange.png",Texture.class);
                manager.load("Player\\walkUpOrange.png",Texture.class);
                manager.load("Player\\walkDownOrange.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player/walkRightOrange.png",Texture.class);
                walkLeft= manager.get("Player/walkLeftOrange.png",Texture.class);
                walkUp = manager.get("Player/walkUpOrange.png",Texture.class);
                walkDown =manager.get("Player/walkDownOrange.png",Texture.class);
                break;
            case "lilac":
                manager.load("Player\\walkRightLilac.png",Texture.class);
                manager.load("Player\\walkLeftLilac.png",Texture.class);
                manager.load("Player\\walkUpLilac.png",Texture.class);
                manager.load("Player\\walkDownLilac.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player\\walkRightLilac.png",Texture.class);
                walkLeft= manager.get("Player\\walkLeftLilac.png",Texture.class);
                walkUp = manager.get("Player\\walkUpLilac.png",Texture.class);
                walkDown =manager.get("Player\\walkDownLilac.png",Texture.class);
                break;
            case "yellow":
                manager.load("Player\\walkRightYellow.png",Texture.class);
                manager.load("Player\\walkLeftYellow.png",Texture.class);
                manager.load("Player\\walkUpYellow.png",Texture.class);
                manager.load("Player\\walkDownYellow.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player\\walkRightYellow.png",Texture.class);
                walkLeft= manager.get("Player\\walkLeftYellow.png",Texture.class);
                walkUp = manager.get("Player\\walkUpYellow.png",Texture.class);
                walkDown =manager.get("Player\\walkDownYellow.png",Texture.class);
                break;
            default:
                manager.load("Player\\walkRight.png",Texture.class);
                manager.load("Player\\walkLeft.png",Texture.class);
                manager.load("Player\\walkUp.png",Texture.class);
                manager.load("Player\\walkDown.png",Texture.class);
                manager.finishLoading();
                walkRight = manager.get("Player\\walkRight.png",Texture.class);
                walkLeft= manager.get("Player\\walkLeft.png",Texture.class);
                walkUp = manager.get("Player\\walkUp.png",Texture.class);
                walkDown =manager.get("Player\\walkDown.png",Texture.class);
        }

        coinPick = new Texture("Collectibles\\coinAnimation.png");
        swordAttack = new Texture("Collectibles\\swordAttack.png");
        swordNotAttack = new Texture("Collectibles\\sword2.png");
        shield = new Texture("Collectibles\\shield.png");
        swipeRight = new Texture ("Player\\swipeRight.png");
        swipeLeft = new Texture ("Player\\swipeLeft.png");
        swipeUp = new Texture ("Player\\swipeUp.png");
        swipeDown = new Texture ("Player\\swipeDown.png");
        playerDead = new Texture ("Player\\player1Selected.png");

        sword = swordNotAttack;
    }


    @Override
    public void show() {
        //assuming it's a square map -> only need width of map and width of tile
        if(isImHost()) {
            generateMapItems(collisionLayer.getWidth(), 100);
        }
        co = new MultiCollect(game, myMultiPlayer,this);
        tempMapItemssize = mapItems.size();
        //start timer
        myMultiPlayer.initialPosition();
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);

        playerPos = new Coordinate(myMultiPlayer.position.getX(), myMultiPlayer.position.getY());

        System.out.println("mapItems: ");
        for(int i=0; i<mapItems.size();i++){
            System.out.print("("+mapItems.get(i).getPosition().getX()+","+mapItems.get(i).getPosition().getY()+")");
        }
        System.out.println();

        for (int i = 0; i < aiPlayers.size(); i++) {
            aiPlayers.get(i).initialPosition();
        }
    }

    int iconSize = 30;
    @Override
    public void render(float delta) { //method repeats a lot
        updateTime(delta);
        removeShield();
        removeEnchantment();
        playerDamaging();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();

        ArrayList<Item> empty = new ArrayList<>();

        for (int i = 0; i < aiPlayers.size(); i++) {
            aiPlayers.get(i).update(delta, 1, mapItems, worldTimer);
        }

        //camera
        cam.update(myMultiPlayer.position.getX(), myMultiPlayer.position.getY(), game);

        //draws tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();


        game.batch.begin();
        {

           // draw collectibles
            drawCollectibles();

            //Collectibles pick up
            if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception
                if ((myMultiPlayer.position.getX() > co.nearestItem(myMultiPlayer).getPosition().getX()) && (myMultiPlayer.position.getX() < co.nearestItem(myMultiPlayer).getPosition().getX() + 100) &&
                        (myMultiPlayer.position.getY() > co.nearestItem(myMultiPlayer).getPosition().getY()) && (myMultiPlayer.position.getY() < co.nearestItem(myMultiPlayer).getPosition().getY() + 100)) {
                    pickUpItem();
                }
            }

            //Collectibles pick up for AI players
            aiMultiPickUp();

            game.batch.draw(overlay, myMultiPlayer.position.getX() - overlayWidth / 2, myMultiPlayer.position.getY() - overlayHeight / 2, overlayWidth, overlayHeight);

            int buffer = 10;
            playerPos.setX(myMultiPlayer.position.getX());
            playerPos.setY(myMultiPlayer.position.getY());
            drawIcons(iconSize,buffer,playerPos);
            drawExitButton(playerPos);

            if(myMultiPlayer.items.contains("gearEnchantment"))
                game.batch.draw(enchantedGlow ,myMultiPlayer.position.getX() -enchantedGlow.getWidth()/2 ,myMultiPlayer.position.getY() - enchantedGlow.getHeight()/2 , enchantedGlow.getWidth() ,enchantedGlow.getHeight());

            //draw other players on my screen
            for (int i = 0; i < players.size(); i++) {
                MultiPlayer otherPlayer = players.get(i);
                otherPlayer.render(game.batch);
            }

            //draw myself on my screen
            if (null != myMultiPlayer) {
                myMultiPlayer.render(game.batch);
            }

            //draw AI players on my screen
            for (int i = 0; i < aiPlayers.size(); i++) {
                aiPlayers.get(i).render(game.batch);
            }

            //draw timer
            String message = "Time = " + (int) (worldTimer - (time.currentTime()));
            font.draw(game.batch, message, myMultiPlayer.position.getX(), myMultiPlayer.position.getY() + VIEWPORT_HEIGHT / 2 - 10);

            //if timer runs out
            if ((worldTimer - time.currentTime()) < 3) {
                overlayWidth -= 15;
                overlayHeight -= 15;

                if ((worldTimer - time.currentTime()) < 0) {
                    this.dispose();
                    //TODO when this game over, player want to start a new game again
                    //need to handle player exit
                    game.setScreen(new EndScreen(this.game));

                }
            }
        }game.batch.end();
    }


    private void aiMultiPickUp() {
        for (int i  = 0; i < aiPlayers.size(); i++) {
            if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception
                if ((aiPlayers.get(i).position.getX() > co.nearestItem(aiPlayers.get(i)).getPosition().getX()) && (aiPlayers.get(i).position.getX() < co.nearestItem(aiPlayers.get(i)).getPosition().getX() + 100) &&
                        (aiPlayers.get(i).position.getY() > co.nearestItem(aiPlayers.get(i)).getPosition().getY()) && (aiPlayers.get(i).position.getY() < co.nearestItem(aiPlayers.get(i)).getPosition().getY() + 100)) {

                    aiPickUp(aiPlayers.get(i), co);
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
        int lives = myMultiPlayer.getHealth();
        for(int i = 0; i < lives; i ++) {
            game.batch.draw(heartTexture, xheart, yheart,iconSize , iconSize);
            xheart += (iconSize + buffer);
        }
        if (myMultiPlayer.items.contains("shield")) {
            //draw shield icon
            float shieldSize = 50;
            float xShield = VIEWPORT_WIDTH - shieldSize -buffer + playerX;
            float yShield = VIEWPORT_HEIGHT - (shieldSize *3) + playerY -50;
            game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);

            String message = "XP :" + myMultiPlayer.getShieldXP() ;
            font.getData().setScale(0.5f,0.5f);
            font.draw(game.batch,message, xShield  ,yShield );
        }
        //sword icon
        if ( myMultiPlayer.items.contains("sword")) {
            float swordSize = 50;
            float xSword = VIEWPORT_WIDTH  - swordSize - buffer + playerX;
            float ySword = VIEWPORT_HEIGHT - (swordSize *2) + playerY;
            game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);
            String message = "XP :" + myMultiPlayer.getSwordXP();
            font.getData().setScale(0.5f,0.5f);
            font.draw(game.batch,message, xSword ,ySword  );
        }
        //draws coin icon
        for ( int i = 0; i < myMultiPlayer.coins; i ++ ) {
            float xCoin = buffer + playerX;
            float yCoin = VIEWPORT_HEIGHT - ( iconSize*3) -buffer + playerY;
            if ( coinSize != iconSize*2)
                coinSize -=5;
            game.batch.draw(coinTexture, xCoin + (i*10), yCoin, coinSize,coinSize);
        }
        float mapSize = 100;
        float xMap = myMultiPlayer.getX() + VIEWPORT_WIDTH /2 - mapSize - 50;
        float yMap =  myMultiPlayer.getY() - VIEWPORT_HEIGHT/2 + 50;

        game.batch.draw(minimapOutline, xMap -8 ,yMap -9,mapSize + 17 , mapSize +17);
        game.batch.draw(mapTexture, xMap,yMap,mapSize , mapSize);
    }

    private void drawCollectibles() {
        for(int i = 0; i < mapItems.size(); i ++) {
            Texture texture = heartTexture;

            if (mapItems.get(i).getType().equals("shield")) {
                texture = shieldTexture;
            }
            if (mapItems.get(i).getType().equals("coin")) {
                texture = coinTexture;
            }
            if (mapItems.get(i).getType().equals("sword")) {
                texture = swordTexture;
            }
            if (mapItems.get(i).getType().equals("healingPotion")) {
                texture = healingPotionTexture;
            }

            if (mapItems.get(i).getType().equals("damagingPotion")) {
                texture = damagingPotionTexture;
            }
            if (mapItems.get(i).getType().equals("gearEnchantment")) {
                texture = healingPotionTexture;
            }
            if (mapItems.get(i).getType().equals("compass")) {
                texture = compassTexture;
            }

            int x = mapItems.get(i).getPosition().getX();
            int y = mapItems.get(i).getPosition().getY();
            game.batch.draw(texture,x, y ,100,100);
        }

    }

    private void aiPickUp(AIPlayer player, MultiCollect aico) {
        Item item = aico.nearestItem(player);
        if (!(player.items.contains(item.getType())) && !(item.getType().equals("coin"))) {
            item = aico.pickedUp(aico.nearestItem(player));

            if (item.getType().equals("shield")) {
                item.setInitialisedTime(worldTimer);
                initialisedShieldTime = worldTimer;
                //aico.shield(item, player);
            }
            if (item.getType().equals("sword")) {
               // aico.sword(item, player, player2);
            }
            if (item.getType().equals("healingPotion")) {
                player.loadPlayerTextures();
                //aico.healingPotion(player);
            }
            if (item.getType().equals("damagingPotion")) {
                item.setInitialisedTime(worldTimer);
                initialisedPotionTime = worldTimer;
                //aico.damagingPotion(item, player);
            }
            if (item.getType().equals("gearEnchantment")) {
                //aico.gearEnchantment(item, player);
            }

        } else if (item.getType().equals("coin")) {
            mapItems.remove(item);
            player.coins++;
        }

    }

    public void pickUpItem() {

        Item item =  co.nearestItem(myMultiPlayer);
        boolean containItems = myMultiPlayer.items.contains(item.getType());
        boolean isCoin = item.getType().equals("coin");
        int indexOfItem = co.getIndexOfItem();

        if (!containItems && !isCoin) {

            item = co.pickedUp(co.nearestItem(myMultiPlayer));

            if (item.getType().equals("shield")) {
                item.setInitialisedTime(worldTimer);
                initialisedShieldTime = worldTimer;
                co.shield(item, myMultiPlayer);
            }
            if (item.getType().equals("sword")) {
                co.sword(item, myMultiPlayer, player2);
            }
            if (item.getType().equals("healingPotion")) {
                //TODO need to think about texture
                //myMultiPlayer.loadPlayerTextures();
                co.healingPotion (myMultiPlayer);
            }
            if (item.getType().equals("damagingPotion")) {
                item.setInitialisedTime(worldTimer);
                initialisedPotionTime = worldTimer;
                co.damagingPotion(item, myMultiPlayer);
            }
            if (item.getType().equals("gearEnchantment")) {
                co.gearEnchantment(item , myMultiPlayer);
                initialisedEnchantmentTime = worldTimer - time.currentTime();
            }
        } else if (isCoin) {
            mapItems.remove(item);
            myMultiPlayer.coins++;
            coinSize = 100;
        }

        //send message when player pick up coin or other items
        if(!containItems||isCoin) {
            CollectMessage itemCollected = new CollectMessage(this.getMultiPlayer().getId(), this, item.getType(), item.getPosition().getX(), item.getPosition().getY(), indexOfItem);
            this.getNc().send(itemCollected);
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
        myMultiPlayer.dispose();
        for(int i=0;i<players.size();i++){
            MultiPlayer otherMultiPlayer = players.get(i);
            otherMultiPlayer.dispose();
        }

        //TODO handle client and server exit game events here
        //unloadAsset();
        manager.clear();
        manager.dispose();
        mapItems.clear();
        cam.cam.viewportHeight = 1000;
        cam.cam.viewportWidth = 1000;
        cam.update(V_WIDTH/2, V_HEIGHT/2, game);
        cam.cam.update();
    }

    public void generateMapItems( int widthInTiles, int tileWidth ) {
        int maxShields = 30;
        int maxCoins = 100;
        int maxSwords = 50;
        int maxCompasses = 30;
        int maxPotions = 100;
        int maxX = widthInTiles;
        int maxY = widthInTiles;

        for (int i = 0; i <= maxShields; i++) {
            Coordinate position = new Coordinate(0, 0);
            position.changeX((int) ((Math.random() * (maxX))) * tileWidth);
            position.changeY((int) ((Math.random() * (maxY))) * tileWidth);

            Item item = new Item("shield", position);

            if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                mapItems.add(item);
                positions.add(position.toString());
                //Send Item message to server
                sendItemGenMsg(position,item);
            }
        }

        for (int i = 0; i < maxCoins; i++) {
            Coordinate position = new Coordinate(0, 0);
            position.changeX((int) ((Math.random() * (maxX))) * tileWidth);
            position.changeY((int) ((Math.random() * (maxY))) * tileWidth);

            Item item = new Item("coin", position);

            if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                mapItems.add(item);
                positions.add(position.toString());
                //Send Item message to server
                sendItemGenMsg(position,item);
            }
        }

        for (int i = 0; i < maxSwords; i++) {
            Coordinate position = new Coordinate(0, 0);
            position.changeX((int) ((Math.random() * (maxX))) * tileWidth);
            position.changeY((int) ((Math.random() * (maxY))) * tileWidth);

            Item item = new Item("sword", position);

            if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                mapItems.add(item);
                positions.add(position.toString());

                //Send Item message to server
                sendItemGenMsg(position,item);
            }
        }

        for (int i = 0; i < maxCompasses; i++) {
            Coordinate position = new Coordinate(0, 0);
            position.changeX((int) ((Math.random() * (maxX))) * tileWidth);
            position.changeY((int) ((Math.random() * (maxY))) * tileWidth);
            Item item = new Item("compass", position);
            if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                mapItems.add(item);
                positions.add(position.toString());
                //Send Item message to server
                sendItemGenMsg(position,item);
            }
        }

        for (int i = 0; i < maxPotions; i++) {
            Coordinate position = new Coordinate(0, 0);
            position.changeX((int) ((Math.random() * (maxX))) * tileWidth);
            position.changeY((int) ((Math.random() * (maxY))) * tileWidth);
            int whatPotion = (int) (Math.random() * 4);

            if (whatPotion == 1) {
                Item item = new Item("healingPotion", position);
                if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                    //Send Item message to server
                    sendItemGenMsg(position,item);
                }
            } else if (whatPotion == 2) {
                Item item = new Item("damagingPotion", position);
                if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                    //Send Item message to server
                    sendItemGenMsg(position,item);
                }
            } else {
                Item item = new Item("gearEnchantment", position);
                if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                    //Send Item message to server
                    sendItemGenMsg(position,item);
                }
            }
        }
    }

    private void sendItemGenMsg(Coordinate position,Item item){
        ItemCreateMessage message = new ItemCreateMessage(myMultiPlayer.getId(), item.getType(), position.getX(), position.getY());
        System.out.println(position.toString());
        netClient.send(message);
    }

    private void removeShield() {
        if(!myMultiPlayer.items.contains("shield")) {
            return;
        }
        if ((worldTimer - time.currentTime()) - initialisedShieldTime  == 10) {
            myMultiPlayer.items.remove("shield");
        }
    }

    private void removeEnchantment() {
        if(!myMultiPlayer.items.contains("gearEnchantment")) {

            return;
        }
        if ((worldTimer - time.currentTime()) - initialisedEnchantmentTime == 10) {
            myMultiPlayer.items.remove("gearEnchantment");
            System.out.println("removed enchantment");
        }
    }

    private void playerDamaging() {
        if(!myMultiPlayer.items.contains("damagingPotion")) {
            return;
        }
        if ((worldTimer - time.currentTime()) - initialisedPotionTime == 2) {
            myMultiPlayer.loadPlayerTextures();
            myMultiPlayer.items.remove("damagingPotion");
        }
    }

    private void updateTime(float dt) {
        timer += dt;
        if (timer >= 1) {
            worldTimer--;
//    		System.out.println("World Timer: " + worldTimer);
            timer = 0;
        }
    }

    //------------------------------------InputProcessor--------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean keyUp(int keycode){
        return myMultiPlayer.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode){
        return myMultiPlayer.keyDown(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
