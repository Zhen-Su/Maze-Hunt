package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.InputHandler;
import com.project.mazegame.tools.OrthoCam;

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

        //MapObjects mapObjects = tileMap.getLayers().get("Object Layer 1").getObjects();
        //System.out.println("coins : " + mapObjects.getCount());
        //  MapObject mo = mapObjects.get(55);





        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");
        //coinLayer = (TiledMapTileLayer) tileMap.getLayers().get("coinLayer");
        //objects


        //System.out.println("Tile's width " + collisionLayer.getWidth());
        player = new Player(this.collisionLayer);
        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, player.x, player.y);


        // buttons
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        audioButtonActive = new Texture("audioOn.png");
        audioButtonInactive = new Texture("audioOff.png");


        heartTexture = new Texture("heart.png");
        coinTexture = new Texture("coin.png");
        swordTexture = new Texture("sword.png");
        shieldTexture = new Texture("shield.png");


        //sort out where coins are going

        //choose the x and y cooridinates, multiply these with the collision layer tile widht/height. This should work

        //then simply draw coins in a loop using the coin texture

        //selma's code
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();

        //updates
        inputHandler.update();
        player.update(delta);

        //tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();

        //rendering
        game.batch.begin();

        float x = player.x + VIEWPORT_WIDTH / 2 - EXIT_WIDTH;
        float y = player.y + VIEWPORT_HEIGHT / 2 - EXIT_HEIGHT;

        //exit button in top right corner
        game.batch.draw(exitButtonActive, x, y,EXIT_WIDTH,EXIT_HEIGHT);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
            this.dispose();
//            game.batch.end();
            game.setScreen(new MenuScreen(this.game));
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

        //draw shield icon
        float shieldSize = 50;
        float xShield =  player.x + VIEWPORT_WIDTH /2 - shieldSize -buffer;
        float yShield = player.y + VIEWPORT_HEIGHT/2 - (shieldSize *3) ;
        game.batch.draw(shieldTexture, xShield, yShield,shieldSize , shieldSize);

        //sword icon
        float swordSize = 50;
        float xSword =  player.x + VIEWPORT_WIDTH /2 - swordSize - buffer;
        float ySword = player.y + VIEWPORT_HEIGHT/2 - (swordSize *2) ;
        game.batch.draw(swordTexture, xSword, ySword,swordSize , swordSize);

        //draws coin icon
        float xCoin = player.x - (VIEWPORT_WIDTH /2) ;
        float yCoin = player.y + VIEWPORT_HEIGHT/2 - ( iconSize*3) -buffer;
        game.batch.draw(coinTexture, xCoin, yCoin,iconSize*2 , iconSize*2);

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
}
