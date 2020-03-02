package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.Item;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.MultiCollect;
import com.project.mazegame.tools.OrthoCam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.project.mazegame.screens.GameScreen.mapItems;
import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;

public class MultiPlayerGameScreen implements Screen,InputProcessor {

    private MazeGame game;
    private OrthoCam cam;

    private MultiPlayer myMultiPlayer;
    private  NetClient netClient = new NetClient(this);
    private List<MultiPlayer> players = new ArrayList<MultiPlayer>();

    private TiledMap tileMap;//
    private OrthogonalTiledMapRenderer tileMapRenderer;//
    private TiledMapTileLayer collisionLayer;
    private int tempMapItemssize;
    private MultiCollect co;
    private MultiPlayer player2;

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

    private float timer;
    private float worldTimer;

    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
    private final int EXIT_Y = VIEWPORT_HEIGHT;
    private AssetManager manager;
    public Texture player, player_up, player_right, player_left, player_down, sword,shield;

    //=====================================constructors=============================================

    public MultiPlayerGameScreen(MazeGame game,String username,String serverIP) {
        this.game = game;

        tileMap = new TmxMapLoader().load("prototypeMap.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        manager = new AssetManager();
        loadAsset();

        myMultiPlayer=new MultiPlayer(this.collisionLayer,username,VIEWPORT_WIDTH / 2,VIEWPORT_HEIGHT / 2,this, Direction.STOP);
        netClient.connect(serverIP,GameServer.SERVER_TCP_PORT);

        Gdx.input.setInputProcessor(this);

        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, VIEWPORT_WIDTH/2,VIEWPORT_HEIGHT/2);

        // buttons
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        audioButtonActive = new Texture("audioOn.png");
        audioButtonInactive = new Texture("audioOff.png");

        heartTexture = new Texture("heart.png");
        coinTexture = new Texture("coin.png");
        swordTexture = new Texture("sword2.png");
        shieldTexture = new Texture("shield.png");
        healingPotionTexture = new Texture("Potion2.png");
        compassTexture = new Texture("RolledMap.png");
        damagingPotionTexture = new Texture("Potion3.png");
    }

    //===================================Getter&Setter==============================================

    public MultiPlayer getMultiPlayer() { return myMultiPlayer; }

    public void setMultiPlayer(MultiPlayer multiPlayer) {
        this.myMultiPlayer = multiPlayer;
    }

    public List<MultiPlayer> getPlayers() { return players; }

    public void setPlayers(List<MultiPlayer> players) {
        this.players = players;
    }

    public NetClient getNc() { return netClient; }

    public void setNc(NetClient netClient) {
        this.netClient = netClient;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    //==============================================================================================

    public void loadAsset(){
        manager.load("playerRedBackCrop.png", Texture.class);
        manager.load("playerRedRightCrop.png", Texture.class);
        manager.load("playerRedLeftCrop.png", Texture.class);
        manager.load("playerRedFrontCrop.png", Texture.class);
        manager.load("sword2.png", Texture.class);
        manager.load("shield.png", Texture.class);

        manager.finishLoading();// waiting for all assets load.

        player_up = manager.get("playerRedBackCrop.png", Texture.class);
        player_right = manager.get("playerRedRightCrop.png", Texture.class);
        player_left = manager.get("playerRedLeftCrop.png", Texture.class);
        player_down = manager.get("playerRedFrontCrop.png", Texture.class);
        sword = manager.get("sword2.png", Texture.class);
        shield = manager.get("shield.png", Texture.class);
    }

    public void unloadAsset(){
        manager.unload("playerRedBackCrop.png");
        manager.unload("playerRedRightCrop.png");
        manager.unload("playerRedLeftCrop.png");
        manager.unload("playerRedFrontCrop.png");
        manager.unload("sword2.png");
        manager.unload("shield.png");
    }

    @Override
    public void show() {
        //assuming it's a square map -> only need width of map and width of tile
        generateMapItems((int) collisionLayer.getWidth(), 100 );
        co = new MultiCollect(game, myMultiPlayer);
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

        //comment out ai player line to run correctly
//       aiPlayer.update(delta);

        //draws tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();


        game.batch.begin();

//        draw collectibles
        drawCollectibles();

//        System.out.println("here");
//        System.out.println("player.position x:"+myMultiPlayer.position.getX());
//        System.out.println("player.position y:"+myMultiPlayer.position.getY());
//        System.out.println(myMultiPlayer.position.getX() + " , " + co.nearestItem(myMultiPlayer).getPosition().getX());
        //Collectibles pick up
        if (!(mapItems.size() == 0)) { // if there is something to pick up - avoid null pointer exception
            if ((myMultiPlayer.position.getX() > co.nearestItem(myMultiPlayer).getPosition().getX()) && (myMultiPlayer.position.getX() < co.nearestItem(myMultiPlayer).getPosition().getX()+100) &&
                    (myMultiPlayer.position.getY() > co.nearestItem(myMultiPlayer).getPosition().getY()) && (myMultiPlayer.position).getY() < co.nearestItem(myMultiPlayer).getPosition().getY()+100)
            {
                System.out.println("over item");
                pickUpItem();

            }
        }

        drawExitButton();

        int iconSize = 30;
        int buffer = 10;

        drawIcons(iconSize,buffer);

        //draw other players on my screen
        for(int i=0;i<players.size();i++){
            MultiPlayer otherPlayer = players.get(i);
            otherPlayer.render(game.batch);
        }

        //draw myself on my screen
        if(null!=myMultiPlayer) {
            myMultiPlayer.render(game.batch);
        }

        game.batch.end();

        //camera follow player
        cam.update(500,500);

    }

    private void drawIcons(int iconSize, int buffer) {

        //draw hearts in top left corner

        int xheart = buffer;
        int yheart = VIEWPORT_HEIGHT - iconSize -buffer;
        int lives = myMultiPlayer.getHealth();
        for(int i = 0; i < lives; i ++) {
            game.batch.draw(heartTexture, xheart, yheart,iconSize , iconSize);
            xheart += (iconSize + buffer);
        }
        if (myMultiPlayer.items.contains("shield")) {
            //draw shield icon
            float shieldSize = 50;
            float xShield = VIEWPORT_WIDTH - shieldSize -buffer;
            float yShield = VIEWPORT_HEIGHT - (shieldSize *3) ;
            game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);
        }
        //sword icon
        if ( myMultiPlayer.items.contains("sword")) {
            float swordSize = 50;
            float xSword = VIEWPORT_WIDTH  - swordSize - buffer;
            float ySword = VIEWPORT_HEIGHT - (swordSize *2) ;
            game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);
        }
        //draws coin icon
        for ( int i = 0; i < myMultiPlayer.coins; i ++ ) {
            float xCoin = buffer;
            float yCoin = VIEWPORT_HEIGHT - ( iconSize*3) -buffer;
            game.batch.draw(coinTexture, xCoin + (i*10), yCoin,iconSize*2 , iconSize*2);
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
        Item item =  co.nearestItem(myMultiPlayer);

        if (!(myMultiPlayer.items.contains(item.getType())) && !(item.getType() == "coin")) {
            item = co.pickedUp(co.nearestItem(myMultiPlayer));

            if (item.getType() == "shield") {
                co.shield(item, myMultiPlayer);
            }
            if (item.getType() == "sword") {
                co.sword(item, myMultiPlayer, player2);
            }
            if (item.getType() == "healingPotion") {
                co.healingPotion (myMultiPlayer);
            }
            if (item.getType() == "damagingPotion") {
                co.damagingPotion(item, myMultiPlayer);
            }
            if (item.getType() == "gearEnchantment") {
                co.gearEnchantment(item , myMultiPlayer);
            }
        } else if (item.getType() == "coin") {
            mapItems.remove(item);
            myMultiPlayer.coins++;
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
        unloadAsset();
        manager.clear();
        manager.dispose();
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
            if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
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
            if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
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
            if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
                mapItems.add(item);
                positions.add(position.toString());
            }

        }

        for (int i = 0; i < maxCompasses; i++) {
            Coordinate position = new Coordinate(0,0);
            position.changeX((int)((Math.random() * (maxX ))) * tileWidth);
            position.changeY((int)((Math.random() * (maxY )))* tileWidth);
            Item item = new Item("compass", position);
            if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
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
                if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                }
            } else if (whatPotion == 2) {
                Item item = new Item("damagingPotion", position);
                if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                }
            } else {
                Item item = new Item("gearEnchantment", position);
                if(!(positions.contains(position.toString())) && !(myMultiPlayer.isCellBlocked((float)position.getX(), (float)position.getY(),collisionLayer))) {
                    mapItems.add(item);
                    positions.add(position.toString());
                }
            }
        }
        System.out.println(positions);
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
