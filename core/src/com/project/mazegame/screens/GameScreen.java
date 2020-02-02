package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

    private Texture exitButtonActive;
    private Texture exitButtonInactive;
    private final int EXIT_WIDTH = 50;
    private final int EXIT_HEIGHT = 20;
    private final int EXIT_Y = VIEWPORT_HEIGHT;

    public GameScreen(MazeGame game) {
        this.game = game;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delta = Gdx.graphics.getDeltaTime();
        System.out.println("Delta is " + delta);

        inputHandler = new InputHandler(this.game);

        tileMap = new TmxMapLoader().load("prototypeMap.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("wallLayer");

        System.out.println("Tile's width " + collisionLayer.getWidth());
        player = new Player(this.collisionLayer);
        cam = new OrthoCam(game,false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT, player.x, player.y);

        //updates
        inputHandler.update();
        player.update(delta);

        //tilemap
        tileMapRenderer.setView(cam.cam);
        tileMapRenderer.render();

        // buttons
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");

        //rendering
        game.batch.begin();

        float x = player.x + VIEWPORT_WIDTH / 2 - EXIT_WIDTH;
        float y = player.y + VIEWPORT_HEIGHT / 2 - EXIT_HEIGHT;
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
