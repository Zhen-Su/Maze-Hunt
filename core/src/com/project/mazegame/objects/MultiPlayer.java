package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.AnimationTool;
import com.project.mazegame.tools.Coordinate;

import java.util.ArrayList;


public class MultiPlayer extends Player {
    private int x, y;
    private float speed = 6;
    private int width, height, coinSize;
    public int coins;
    public int health = 5;
    private int id;
    public int swordDamage;
    public Coordinate position;
    public boolean isPoisoned;
    String colour;
    private int swordXP;
    private int shieldXP;

    private String name;
    public ArrayList<String> items;
    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    private Direction dir;
    private TiledMapTileLayer collisionLayer;
    public static boolean debug = false;

    AnimationTool RightAnim, LeftAnim, UpAnim, DownAnim, animation;
    AnimationTool coinAnimation, swordSwipeRight, swordSwipeLeft, swordSwipeUp, swordSwipeDown, swipeAnim;
    SpriteBatch batch;


    //constructors=================================================================================
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, MultiPlayerGameScreen gameClient, Direction dir) {
        super();
        if (debug) System.out.println("My Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.health = 5;
        this.coins = 0;
        this.name = username;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x, y);
        this.swordDamage = 0;
//        this.colour=colour;
        swordXP = 0;
        shieldXP = 0;

        initialPosition();
        x = this.position.getX();
        y = this.position.getY();

        this.gameClient = gameClient;
        this.dir = dir;
        ArrayList<Item> items = new ArrayList<Item>();
        createAnimations();

        if (debug) System.out.println("My Multiplayer instance construction done!");
    }

    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir) {
        super();
        if (debug) System.out.println("Other Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.health = 5;
        this.coins = 0;
        this.name = username;
        this.items = new ArrayList<>();

        this.x = x;
        this.y = y;
        this.position = new Coordinate(x, y);

        this.gameClient = gameClient;
        this.dir = dir;
        ArrayList<Item> items = new ArrayList<Item>();
        createAnimations();

        if (debug) System.out.println("Other Multiplayer instance construction done!");
    }

    //Getter&Setter=================================================================================
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getSwordXP() {
        return this.swordXP;
    }

    public int getShieldXP() {
        return this.shieldXP;
    }

    public void setAnimation(AnimationTool direction) {
        animation = direction;
    }

    public void setSwordAnimation(AnimationTool direction) {
        swipeAnim = direction;
    }

    public void setBatch(SpriteBatch sb) {
        this.batch = sb;
    }

    public SpriteBatch getSpriteBatch() {
        return this.batch;
    }

    public Texture getFrames() {
        return gameClient.frames;
    }

    //==============================================================================================

    public void render(SpriteBatch sb) {

        setBatch(sb);
        switch (dir) {
            case U:
                setAnimation(UpAnim);
                break;
            case D:
                setAnimation(DownAnim);
                break;
            case R:
                setAnimation(RightAnim);
                break;
            case L:
                setAnimation(LeftAnim);
                break;
            case STOP:
                setAnimation(DownAnim);
                break;
        }

        animation.render();

        updateMotion();

        if (this.items.contains("sword"))
            sb.draw(gameClient.sword, (float) (x), y - (height / 4), 50, 50);

        if (this.items.contains("shield"))
            sb.draw(gameClient.shield, (float) (x - (width / 1.5)), y - (height / 2), 50, 50);

    }

    public void createAnimations() {
        width = gameClient.walkUp.getWidth() / 2;
        height = gameClient.walkUp.getHeight() / 2;
        coinSize = gameClient.coinPick.getHeight() / 2;

        gameClient.frames = gameClient.walkRight;
        RightAnim = new AnimationTool(width, height, this, gameClient.walkRight, true);
        RightAnim.create();

        gameClient.frames = gameClient.walkLeft;
        LeftAnim = new AnimationTool(width, height, this, gameClient.walkLeft, true);
        LeftAnim.create();

        gameClient.frames = gameClient.walkUp;
        UpAnim = new AnimationTool(width, height, this, gameClient.walkUp, true);
        UpAnim.create();

        gameClient.frames = gameClient.walkDown;
        DownAnim = new AnimationTool(width, height, this, gameClient.walkDown, true);
        DownAnim.create();

        //Create animations - make the frames but don't render them
        gameClient.frames = gameClient.swipeRight;
        swordSwipeRight = new AnimationTool(100, 100, this, gameClient.swipeRight, false);
        swordSwipeRight.create();

        gameClient.frames = gameClient.swipeLeft;
        swordSwipeLeft = new AnimationTool(100, 100, this, gameClient.swipeLeft, false);
        swordSwipeLeft.create();

        gameClient.frames = gameClient.swipeUp;
        swordSwipeUp = new AnimationTool(100, 100, this, gameClient.swipeUp, false);
        swordSwipeUp.create();

        gameClient.frames = gameClient.swipeDown;
        swordSwipeDown = new AnimationTool(100, 100, this, gameClient.swipeDown, false);
        swordSwipeDown.create();

        swipeAnim = new AnimationTool(100, 100, this, gameClient.swipeDown, false);
        swipeAnim.create();

        //setAnimation( UpAnim);
        animation = new AnimationTool(width, height, (MultiPlayer) this, gameClient.walkUp, true);

        animation.create();
        setAnimation(UpAnim);
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
                bR = true;
                break;
            case Input.Keys.LEFT:
                bL = true;
                break;
            case Input.Keys.UP:
                bU = true;
                break;
            case Input.Keys.DOWN:
                bD = true;
                break;
        }
        locateDirection();
        return true;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
                bR = false;
                break;
            case Input.Keys.LEFT:
                bL = false;
                break;
            case Input.Keys.UP:
                bU = false;
                break;
            case Input.Keys.DOWN:
                bD = false;
                break;
        }
        locateDirection();
        return true;
    }

    public void updateMotion() {

        this.position.setX(x);
        this.position.setY(y);

        switch (dir) {
            case R:
                if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) {
                    x += speed;
                    //check player isn't in a wall
                    if (!checkCollisionMap(x, y)) { //if it's in a wall, move player back
                        x -= speed;
                    } else
                        this.position.setX(x);
                }
                break;
            case L:
                if (x > 0) {
                    x -= speed;
                    if (!checkCollisionMap(x, y)) {
                        x += speed;
                    } else
                        this.position.setX(x);
                }
                break;
            case U:
                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if (!checkCollisionMap(x, y)) {
                        y -= speed;
                    } else
                        this.position.setY(y);
                }
                break;
            case D:
                if (y > 0) {
                    y -= speed;
                    if (!checkCollisionMap(x, y)) {
                        y += speed;
                    } else
                        this.position.setY(y);
                }
                break;
            case STOP:
                break;
        }
    }

    /**
     * If direction is changed, send message to server immediately.
     * boolean bL, bU, bR, bD is only used to decide when should send message
     */
    private void locateDirection() {
        Direction oldDir = this.dir;

        if (bR && !bL && !bD && !bU) {
            dir = Direction.R;
        } else if (bL && !bR && !bU && !bD) {
            dir = Direction.L;
        } else if (bU && !bD && !bL && !bR) {
            dir = Direction.U;
        } else if (bD && !bU && !bR && !bL) {
            dir = Direction.D;
        } else if (!bL && !bD && !bR && !bU) {
            dir = Direction.STOP;
        }

        if (dir != oldDir) {
            MoveMessage message = new MoveMessage(id, this.position.getX(), this.position.getY(), dir);
            gameClient.getNc().send(message);
        }
    }

    public void initialPosition() {
        //Coordinate position = new Coordinate();

        int maxX = collisionLayer.getWidth();
        int maxY = collisionLayer.getHeight();
//    	System.out.println("maxX" + maxX + " , " + maxY);

        int ranx = (int) ((Math.random() * (maxX)));
        int rany = (int) ((Math.random() * (maxY)));
//    	System.out.println("ran" + ranx + " , " + rany);

        this.position.setX(ranx * (int) collisionLayer.getTileWidth() + 50);
        this.position.setY(rany * (int) collisionLayer.getTileHeight() + 50);

        if (isCellBlocked((float) position.getX(), (float) position.getY())) {
            initialPosition();
        }
    }

    public boolean checkCollisionMap(float possibleX, float possibleY) { // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX;
        float yWorld = possibleY;

        boolean collisionWithMap = false;

        //Check corners of player to check for collision
        //check corners T = top, B = bottom, R = right, L = left
        boolean TLbool = isCellBlocked(xWorld - (width / 2), yWorld + (height / 2));
        boolean TRbool = isCellBlocked(xWorld + (width / 2), yWorld + (height / 2));
        boolean BLbool = isCellBlocked(xWorld - (width / 2), yWorld - (height / 2));
        boolean BRbool = isCellBlocked(xWorld + (width / 2), yWorld - (height / 2));

        collisionWithMap = TLbool || TRbool || BLbool || BRbool;

        //If there is a collision
        if (collisionWithMap) return false;
        else return true;
    }

    public boolean isCellBlocked(float x, float y) {

        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isWall");
    }

    public void decreaseHealth(int number) {
        this.health -= number;
        if (health <= 0) {
            this.death();
        }
    }

    public void generateHealth() {
        if (this.health != 9) {
            this.health++;
        }
    }

    public void increaseSwordXP(int XP) {
        this.swordXP += XP;
    }

    public void increaseShieldXP(int XP) {
        this.shieldXP += XP;
    }

    public void death() {
        if (debug) System.out.println("Player has died respawning now");
        this.health = 5;
        this.coins = 0;

        this.items.clear();

        //this.items = new ArrayList<>();
    }

    public void playerHitPlayer(Player hit) {
        // write boolean to check sword

        if (this.items.contains("sword") && !hit.items.contains("shield")) {
            // will need to do if have item then that can be called
            // then decrease the helath based on that
            // could have a damage do attribute and various attributes which change throught the generateMapItems
            hit.decreaseHealth(this.swordDamage);
            if (hit.health == 0) {
                this.swordDamage++;
                this.coins += hit.coins;
                hit.death();
            }
        }
        //need to add shield stuffr
    }

    public int getHealth() {
        return health;
    }

    public void dispose() {
    }

}
