package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Messagess.ItemCreateMessage;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.AIGameClient;
import com.project.mazegame.objects.AIPlayer;
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.OrthoCam;
import com.project.mazegame.tools.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;
import static com.project.mazegame.tools.Variables.V_HEIGHT;
import static com.project.mazegame.tools.Variables.V_WIDTH;

public class MultiPlayerGameScreen implements Screen, InputProcessor {
    private boolean debug = true;
    //Use for Host player
    private GameServer server;
    private boolean isHost;
    private boolean HostStartGame = false;

    //Item List List
    public ArrayList<Item> mapItems = new ArrayList<Item>();

    private MazeGame game;
    private OrthoCam cam;

    private MultiPlayer myMultiPlayer;
    private NetClient netClient = new NetClient(this);
    private List<Player> players = new ArrayList<>();
    public HashMap<Integer, Integer> playersIdIndexList = new HashMap<>();
    private boolean imHost;
    private AIPlayer aiPlayer;
    public ArrayList<AIPlayer> aiPlayers;
    public ArrayList<NetClient> aiNetClients = new ArrayList<>();
    public ArrayList<AIGameClient> aiGameClients = new ArrayList<>();

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;
    private int tempMapItemssize;
    private ArrayList<Collect> aicos = new ArrayList<>();
    private MultiPlayer player2;

    public Texture player,coinPick;
    private Texture exitButtonActive;
    private Texture exitButtonInactive;
    private Texture minimapTexture;
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
    public BitmapFont bitmapFont;
    public Texture enchantedGlow;
    private Texture mapTexture, minimapOutline, playerIcon;;


    private float timer;
    public static float worldTimer = 360;
    public Timer time = new Timer();
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

    public MultiPlayerGameScreen(MazeGame game, String username, String serverIP, boolean isHost) {
        this.game = game;
        this.isHost = isHost;
        timer = 0;

        tileMap = new TmxMapLoader().load("Map3.tmx");
        //TODO this 113 line should be changed after integrate with customize.
        mapTexture = new Texture("Maps\\Map3Icon.png");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        //TODO need to think about colour thing(customize)
        myMultiPlayer = new MultiPlayer(this.collisionLayer, username, this, Direction.STOP,"orange");
        netClient.connect(serverIP,false,0);

        if (isHost) {
            //create AI players
            createAIplayers(0);
            //Every AI player connect to GameServer
            for (int i = 0; i < aiGameClients.size(); i++) {
                NetClient nc = aiGameClients.get(i).getNetClient();
                nc.connect(serverIP, true, i);
            }
        }

        Gdx.input.setInputProcessor(this);

        cam = new OrthoCam(game, false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, myMultiPlayer.position.getX(), myMultiPlayer.position.getY());

        getAsset();
    }


    //===================================Getter&Setter==============================================

    public MultiPlayer getMultiPlayer() {
        return myMultiPlayer;
    }

    public void setMultiPlayer(MultiPlayer multiPlayer) {
        this.myMultiPlayer = multiPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public NetClient getNc() {
        return netClient;
    }

    public void setNc(NetClient netClient) {
        this.netClient = netClient;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public boolean isHostStartGame() {
        return HostStartGame;
    }

    public void setHostStartGame(boolean hostStartGame) {
        HostStartGame = hostStartGame;
    }

    public boolean isImHost() {
        return imHost;
    }

    public void setImHost(boolean imHost) {
        this.imHost = imHost;
    }

    public GameServer getServer() {
        return server;
    }

    public void setServer(GameServer server) {
        this.server = server;
    }


    //==============================================================================================

    public void createAIplayers(int aiNum) {
        if (aiNum != 0) {
            aiPlayer = new AIPlayer(this.collisionLayer, "AI" + aiNum, aiNum, "red",Direction.STOP);
            aiPlayers = aiPlayer.AITakingOver(aiNum - 1);
            aiPlayers.add(aiPlayer);

            for (int i = 0; i < aiPlayers.size(); i++) {
                aiNetClients.add(new NetClient(this));
                aicos.add(new Collect(aiPlayers.get(i), null));
                //create AIGameClients for every ai player
                aiGameClients.add(new AIGameClient(aiPlayers.get(i), aicos.get(i), aiNetClients.get(i)));
            }
        }
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

    @Override
    public void show() {
        //assuming it's a square map -> only need width of map and width of tile
        if (isImHost()) {
            generateMapItems(collisionLayer.getWidth(), 100);
        }

        tempMapItemssize = mapItems.size();
        //start timer
        myMultiPlayer.initialPosition();
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);

        playerPos = new Coordinate(myMultiPlayer.position.getX(), myMultiPlayer.position.getY());

        System.out.println("mapItems: ");
        for (int i = 0; i < mapItems.size(); i++) {
            System.out.print("(" + mapItems.get(i).getPosition().getX() + "," + mapItems.get(i).getPosition().getY() + ")");
        }
        System.out.println();
    }

    int iconSize = 30;

    @Override
    public void render(float delta) { //method repeats a lot
        updateTime(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();

        //comment out ai player line to run correctly
//       aiPlayer.update(delta);

        //update other player
        for(Player otherPlayer : players){
            otherPlayer.update(delta);
        }

        //update myself
        myMultiPlayer.update(delta);

        //camera
        cam.update(myMultiPlayer.position.getX(), myMultiPlayer.position.getY(), game);

        //draws tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();


        game.batch.begin();
        {

           //draw collectibles
            drawCollectibles();

            //Collectibles pick up for me
            if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception
                if ((myMultiPlayer.position.getX() > myMultiPlayer.co.nearestItem(myMultiPlayer).getPosition().getX()) && (myMultiPlayer.position.getX() < myMultiPlayer.co.nearestItem(myMultiPlayer).getPosition().getX() + 100) &&
                        (myMultiPlayer.position.getY() > myMultiPlayer.co.nearestItem(myMultiPlayer).getPosition().getY()) && (myMultiPlayer.position.getY() < myMultiPlayer.co.nearestItem(myMultiPlayer).getPosition().getY() + 100)) {
                    pickUpItem(myMultiPlayer);
                }
            }

            //Collectibles pick up for other player
            if(!(mapItems.size() == 0)){
                for(int i=0; i<players.size();i++){
                    Player otherPlayer = players.get(i);
                    if((otherPlayer.position.getX() > otherPlayer.co.nearestItem(otherPlayer).getPosition().getX())&& (otherPlayer.position.getX()<otherPlayer.co.nearestItem(otherPlayer).getPosition().getX()+100) &&
                            (otherPlayer.position.getY()>otherPlayer.co.nearestItem(otherPlayer).getPosition().getY()) && (otherPlayer.position.getY()<otherPlayer.co.nearestItem(otherPlayer).getPosition().getY()+100)){
                        pickUpItem(otherPlayer);
                    }
                }
            }

            game.batch.draw(overlay, myMultiPlayer.position.getX() - overlayWidth / 2, myMultiPlayer.position.getY() - overlayHeight / 2, overlayWidth, overlayHeight);

            int buffer = 10;
            playerPos.setX(myMultiPlayer.position.getX());
            playerPos.setY(myMultiPlayer.position.getY());
            drawIcons(iconSize, buffer, playerPos);
            drawExitButton(playerPos);

            //draw other players on my screen
            for (int i = 0; i < players.size(); i++) {
                Player otherPlayer = players.get(i);
                otherPlayer.render(game.batch);
            }

            //draw myself on my screen
            if (null != myMultiPlayer) {
                myMultiPlayer.render(game.batch);
                myMultiPlayer.attack();
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
                    if (isHost) {
                        this.server.dispose(this);
                        if (debug) {
                            System.out.println("Server Close");
                        }
                    }
                    //if gameover, then close server from hostplayer
                }
            }
        }
        game.batch.end();
    }


    int coinSize = iconSize * 2;


    private void drawIcons(int iconSize, int buffer, Coordinate position) {
        //take player x and y into account
        int playerX = position.getX() - VIEWPORT_WIDTH / 2;
        int playerY = position.getY() - VIEWPORT_HEIGHT / 2;
        //draw hearts in top left corner

        int xheart = buffer + playerX;
        int yheart = VIEWPORT_HEIGHT - iconSize - buffer + playerY;
        int lives = myMultiPlayer.getHealth();
        for (int i = 0; i < lives; i++) {
            game.batch.draw(heartTexture, xheart, yheart, iconSize, iconSize);
            xheart += (iconSize + buffer);
        }
        if (myMultiPlayer.items.contains("shield")) {
            //draw shield icon
            float shieldSize = 50;
            float xShield = VIEWPORT_WIDTH - shieldSize - buffer + playerX;
            float yShield = VIEWPORT_HEIGHT - (shieldSize * 3) + playerY - 50;
            game.batch.draw(shieldTexture, xShield, yShield, shieldSize, shieldSize);

            String message = "XP :" + myMultiPlayer.getShieldXP();
            font.getData().setScale(0.5f, 0.5f);
            font.draw(game.batch, message, xShield, yShield);
        }
        //sword icon
        if (myMultiPlayer.items.contains("sword")) {
            float swordSize = 50;
            float xSword = VIEWPORT_WIDTH - swordSize - buffer + playerX;
            float ySword = VIEWPORT_HEIGHT - (swordSize * 2) + playerY;
            game.batch.draw(swordTexture, xSword, ySword, swordSize, swordSize);
            String message = "XP :" + myMultiPlayer.getSwordXP();
            font.getData().setScale(0.5f, 0.5f);
            font.draw(game.batch, message, xSword, ySword);
        }
        //draws coin icon
        for (int i = 0; i < myMultiPlayer.coins; i++) {
            float xCoin = buffer + playerX;
            float yCoin = VIEWPORT_HEIGHT - (iconSize * 3) - buffer + playerY;
            if (coinSize != iconSize * 2)
                coinSize -= 5;
            game.batch.draw(coinTexture, xCoin + (i * 10), yCoin, coinSize, coinSize);
        }
        float mapSize = 100;
        float xMap = myMultiPlayer.getX() + VIEWPORT_WIDTH / 2 - mapSize - 50;
        float yMap = myMultiPlayer.getY() - VIEWPORT_HEIGHT / 2 + 50;


        if (myMultiPlayer.items.contains("minimap")) {
            game.batch.draw(minimapOutline, xMap - 8, yMap - 9, mapSize + 17, mapSize + 17);
            game.batch.draw(mapTexture, xMap, yMap, mapSize, mapSize);

            //draw player position
            int x = (myMultiPlayer.position.getX() - 500 + 60) / 20;
            int y = (myMultiPlayer.position.getY() - 500 + 80) / 20;
            game.batch.draw(playerIcon, xMap + x, yMap + y, 5, 5);
        }
    }

    private void drawCollectibles() {
        for (int i = 0; i < mapItems.size(); i++) {
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
            if (mapItems.get(i).getType().equals("minimap")) {
                texture = minimapTexture;
            }

            int x = mapItems.get(i).getPosition().getX();
            int y = mapItems.get(i).getPosition().getY();
            game.batch.draw(texture, x, y, 100, 100);
        }

    }

    public void pickUpItem(Player player) {
        Item item = player.getCo().nearestItem(player);

        boolean containItems = player.items.contains(item.getType());
        boolean isCoin = item.getType().equals("coin");
        boolean isHealingPotion = item.getType().equals("healingPotion");
        boolean isDamagingPotion = item.getType().equals("damagingPotion");

        if (!containItems && !isCoin && !isHealingPotion && !isDamagingPotion) {
            //Only shield, gearEnchantment, sword ,minimap can add to items list.
            item = player.getCo().pickedUp(player.getCo().nearestItem(player));

            if (item.getType().equals("shield")) {
                item.setInitialisedTime(time.currentTime());
                player.initialisedShieldTime = time.currentTime();
                player.getCo().shield(item, player);
                if (player.items.contains("gearEnchantment")) {
                    player.initialisedShieldTime += 3;
                }
            }
            if (item.getType().equals("sword")) {
                player.getCo().sword(item, player);
            }
            if (item.getType().equals("gearEnchantment")) {
                player.getCo().gearEnchantment(item, player);
                player.initialisedEnchantmentTime = time.currentTime();
                if (player.items.contains("shield"))
                    player.initialisedShieldTime += 3;
            }
            if (item.getType().equals("minimap")) {
                player.getCo().minimap(item);
            }
        } else if (isCoin) {
            mapItems.remove(item);
            player.coins++;
        } else if (isHealingPotion) {
            mapItems.remove(item);
            player.getCo().healingPotion(player);
        } else if (isDamagingPotion) {
            mapItems.remove(item);
            player.getCo().damagingPotion(player);
        }
    }

    private void drawExitButton(Coordinate position) {

        //take player x and y into account
        int playerX = position.getX() - VIEWPORT_WIDTH / 2;
        int playerY = position.getY() - VIEWPORT_HEIGHT / 2;

        int x = VIEWPORT_WIDTH - EXIT_WIDTH + playerX;
        int y = VIEWPORT_HEIGHT - EXIT_HEIGHT + playerY;

        //origin of cursor is top left hand corner
        //exit button in top right corner

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.dispose();
            game.setScreen(new MenuScreen(this.game));
        }

        if (Gdx.input.getX() < V_WIDTH && Gdx.input.getX() > V_WIDTH - EXIT_WIDTH && Gdx.input.getY() < EXIT_HEIGHT && Gdx.input.getY() > 0) {
            game.batch.draw(exitButtonActive, x, y, EXIT_WIDTH, EXIT_HEIGHT);
            if (Gdx.input.justTouched()) {
                this.dispose();
                game.setScreen(new MenuScreen(this.game));
                this.server.dispose(this);
                if (debug) {
                    System.out.println("Server close");
                }

            }
        } else game.batch.draw(exitButtonInactive, x, y, EXIT_WIDTH, EXIT_HEIGHT);
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
        for (int i = 0; i < players.size(); i++) {
            Player otherMultiPlayer = players.get(i);
            otherMultiPlayer.dispose();
        }

        //TODO handle client and server exit game events here
        mapItems.clear();
        cam.cam.viewportHeight = 1000;
        cam.cam.viewportWidth = 1000;
        cam.update(V_WIDTH / 2, V_HEIGHT / 2, game);
        cam.cam.update();
    }

    public void generateMapItems(int widthInTiles, int tileWidth) {
        HashSet<String> positions = new HashSet<String>();

        int maxShields = 15;
        int maxCoins = 50;
        int maxSwords = 25;
        int maxMinimaps = 15;
        int maxPotions = 50;
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
                sendItemGenMsg(position, item);
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
                sendItemGenMsg(position, item);
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
                sendItemGenMsg(position, item);
            }
        }

        for (int i = 0; i < maxMinimaps; i++) {
            Coordinate position = new Coordinate(0, 0);
            position.changeX((int) ((Math.random() * (maxX))) * tileWidth);
            position.changeY((int) ((Math.random() * (maxY))) * tileWidth);
            Item item = new Item("minimap", position);
            if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                mapItems.add(item);
                positions.add(position.toString());
                //Send Item message to server
                sendItemGenMsg(position, item);
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
                    sendItemGenMsg(position, item);
                }
            } else if (whatPotion == 2) {
                Item item = new Item("damagingPotion", position);
                if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                    //Send Item message to server
                    sendItemGenMsg(position, item);
                }
            } else {
                Item item = new Item("gearEnchantment", position);
                if (!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float) position.getX(), (float) position.getY()))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                    //Send Item message to server
                    sendItemGenMsg(position, item);
                }
            }
        }
    }

    private void sendItemGenMsg(Coordinate position, Item item) {
        ItemCreateMessage message = new ItemCreateMessage(myMultiPlayer.getID(), item.getType(), position.getX(), position.getY());
        System.out.println(position.toString());
        netClient.send(message);
    }


    private void updateTime(float dt) {
        float initial = time.currentTime();
        time.updateTimer(dt);

        if (!(time.currentTime() == initial)) {
            worldTimer--;
        }
    }

    //------------------------------------InputProcessor--------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean keyUp(int keycode) {
        return myMultiPlayer.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
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
