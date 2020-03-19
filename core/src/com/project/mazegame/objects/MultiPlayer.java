package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.networking.Messagess.AttackMessage;
import com.project.mazegame.networking.Messagess.DeadMessage;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.AnimationTool;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.Coordinate;

import java.util.ArrayList;


public class MultiPlayer extends Player {

    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    private Direction dir;
    public static boolean debug = false;

    //constructors=================================================================================
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, MultiPlayerGameScreen gameClient, Direction dir,String colour) {
        super(collisionLayer,username);
        if (debug) System.out.println("My Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.health = 5;
        this.coins = 0;
        this.name = username;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x, y);
        this.swordDamage = 0;
        this.colour=colour;
        swordXP = 0;
        shieldXP = 0;
        this.font=gameClient.bitmapFont;

        initialPosition();
        x = this.position.getX();
        y = this.position.getY();

        loadPlayerTextures();
        this.gameClient = gameClient;
        this.dir = dir;
        ArrayList<Item> items = new ArrayList<Item>();
        createAnimations();

        if (debug) System.out.println("My Multiplayer instance construction done!");
    }

    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir,String colour) {
        super(collisionLayer,username,x,y,dir);
        if (debug) System.out.println("Other Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.health = 5;
        this.coins = 0;
        this.name = username;
        this.items = new ArrayList<>();
        this.font=gameClient.bitmapFont;
        this.colour = colour;

        this.x = x;
        this.y = y;
        this.position = new Coordinate(x, y);

        loadPlayerTextures();
        this.gameClient = gameClient;
        this.dir = dir;
        ArrayList<Item> items = new ArrayList<Item>();
        createAnimations();

        if (debug) System.out.println("Other Multiplayer instance construction done!");
    }

    //Getter&Setter=================================================================================
    public Direction getDir() {
        return dir;
    }
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    //==============================================================================================

//    public void loadPlayerTextures(){
//
//        switch (colour) {
//            case "blue":
//                walkRight=Assets.manager.get(Assets.walkRightBlue, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeftBlue, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUpBlue, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDownBlue, Texture.class);
//                break;
//            case "green":
//                walkRight=Assets.manager.get(Assets.walkRightGreen, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeftGreen, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUpGreen, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDownGreen, Texture.class);
//                break;
//            case "pink":
//                walkRight=Assets.manager.get(Assets.walkRightPink, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeftPink, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUpPink, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDownPink, Texture.class);
//                break;
//            case "orange":
//                walkRight=Assets.manager.get(Assets.walkRightOrange, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeftOrange, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUpOrange, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDownOrange, Texture.class);
//                break;
//            case "lilac":
//                walkRight=Assets.manager.get(Assets.walkRightLilac, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeftLilac, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUpLilac, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDownLilac, Texture.class);
//                break;
//            case "yellow":
//                walkRight=Assets.manager.get(Assets.walkRightYellow, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeftYellow, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUpYellow, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDownYellow, Texture.class);
//                break;
//            default:
//                walkRight=Assets.manager.get(Assets.walkRight, Texture.class);
//                walkLeft=Assets.manager.get(Assets.walkLeft, Texture.class);
//                walkUp=Assets.manager.get(Assets.walkUp, Texture.class);
//                walkDown=Assets.manager.get(Assets.walkDown, Texture.class);
//        }
//
//        coinPick=Assets.manager.get(Assets.coinPick, Texture.class);
//        swordAttack=Assets.manager.get(Assets.swordAttack, Texture.class);
//        swordNotAttack=Assets.manager.get(Assets.swordNotAttack, Texture.class);
//        shield=Assets.manager.get(Assets.shield, Texture.class);
//        swipeRight=Assets.manager.get(Assets.swipeRight, Texture.class);
//        swipeLeft=Assets.manager.get(Assets.swipeLeft, Texture.class);
//        swipeUp=Assets.manager.get(Assets.swipeUp, Texture.class);
//        swipeDown=Assets.manager.get(Assets.swipeDown, Texture.class);
//        playerDying=Assets.manager.get(Assets.playerDying, Texture.class);
//        font=Assets.manager.get(Assets.font, BitmapFont.class);
//
//        sword = swordNotAttack;
//    }


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

        this.animation.render();
        updateMotion();

        if (this.items.contains("sword"))
            sb.draw(gameClient.sword, (float) (x), y - (height / 4), 50, 50);

        if (this.items.contains("shield"))
            sb.draw(gameClient.shield, (float) (x - (width / 1.5)), y - (height / 2), 50, 50);

        font.getData().setScale(0.5f,0.5f);
        font.draw(sb,this.name, this.position.getX() - 30,this.position.getY() + 60);
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

        if(this.isDead()) {
            if(respawnCounter == 0) {
                respawnCounter = time.currentTime();
            }

            if(time.currentTime() - respawnCounter == 3) {
                this.death();

            }
            setAnimation(DyingAnim);

        }else {

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
            MoveMessage message = new MoveMessage(ID, this.position.getX(), this.position.getY(), dir);
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

        //Send dead info to server
        DeadMessage deadmsg = new DeadMessage(ID,"DEAD");
        this.gameClient.getNc().send(deadmsg);
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
