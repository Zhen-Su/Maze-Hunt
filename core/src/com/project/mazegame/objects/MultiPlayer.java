package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.networking.Messages.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;


public class MultiPlayer {
    public float x, y;
    public float pX,pY;

    private Texture player, player_up, player_middle, player_down,sword,shield;
    //private Texture other_player,other_player_up,other_player_middle,other_player_down,other_player_sword,other_player_shield;
    private float speed = 6;
    private float width, height;
    private int lives = 5;
    private boolean hasSword = true;
    private boolean hasShield = true;
    private TiledMapTileLayer collisionLayer, coinLayer;
    private MapLayer objLayer;

    public boolean hasCompass;
    public boolean hasDamagingPotion;
    public boolean hasHealingPotion;

    public int coins;
    public int swordDamage;

    private int id;
    private String name;
    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    private Direction dir = Direction.STOP;


    //constructors=================================================================================
    public MultiPlayer(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name=name;
    }

    public MultiPlayer(String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir) {
        this(x, y,username);
        this.gameClient = gameClient;
        this.dir = dir;
    }

    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir ) {
        this(username, x, y,gameClient,dir);
        System.out.println("multiplayer is constructing...");
        this.collisionLayer = collisionLayer;
        pX = x+SCROLLTRACKER_X;
        pY = y+SCROLLTRACKER_Y;
//        loadPlayerTextures();
//        width = player_middle.getWidth();
//        height = player_middle.getHeight();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                            loadPlayerTextures();
                            width = player_middle.getWidth();
                            height = player_middle.getHeight();
                        }
                });
            }
        }).start();
        System.out.println("multiplayer construction done!");
    }


    //Getter&Setter=================================================================================
    public Direction getDir() { return dir; }

    public void setDir(Direction dir) { this.dir = dir; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
    public float getpX() {
        return pX;
    }

    public void setpX(float pX) {
        this.pX = pX;
    }

    public float getpY() {
        return pY;
    }

    public void setpY(float pY) {
        this.pY = pY;
    }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Texture getPlayerTexture() { return player; }

    public void setPlayerTexture(Texture player) { this.player = player; }

    public Texture getPlayer_up() { return player_up; }

    public Texture getPlayer_middle() { return player_middle; }

    public Texture getPlayer_down() { return player_down; }

    public boolean isHasSword() { return hasSword; }

    public void setHasSword(boolean hasSword) { this.hasSword = hasSword; }

    public boolean isHasShield() { return hasShield; }

    public void setHasShield(boolean hasShield) { this.hasShield = hasShield; }

    //==============================================================================================

    // update player movement
    public void update (float delta){
        pX = x+SCROLLTRACKER_X;
        pY = y+SCROLLTRACKER_Y;

        if (RIGHT_TOUCHED) {
            //set direction boolean true
            bR=true;
            //try move player right
            SCROLLTRACKER_X += speed;
            //change direction
            locateDirection();
            //check player isn't in a wall
            if(!checkCollisionMap(x, y)) {
                //move player back if needed
                System.out.println("hit right wall");
                SCROLLTRACKER_X -= speed;
            }
            //change it back to the original value
            bR=false;
        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
                bL=true;
                SCROLLTRACKER_X -= speed;
                locateDirection();
                if(!checkCollisionMap(x,y)) {
                    System.out.println("hit left wall");
                    SCROLLTRACKER_X += speed;
                }
            }
            bL=false;
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
                bU=true;
                SCROLLTRACKER_Y += speed;
                locateDirection();
                if(!checkCollisionMap(x, y)) {
                    System.out.println("hit top wall");
                    SCROLLTRACKER_Y -= speed;
                }
                bU=false;
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
                bD=true;
                SCROLLTRACKER_Y -= speed;
                locateDirection();
                if(!checkCollisionMap(x, y)) {
                    System.out.println("hit bottom wall");
                    SCROLLTRACKER_Y += speed;
                }
                bD=false;
            }
        }

        //change player texture
        if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
            player = player_up;
        } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
            player = player_down;
        } else {
            player = player_middle;
        }
    }

    /**
     * If direction is changed, send message to server immediately.
     * boolean bL, bU, bR, bD is only used to decide when should send message
     */
    private void locateDirection(){
        Direction oldDir = this.dir;

        if(bR) dir = Direction.R;
        else if(bL) dir=Direction.L;
        else if(bU) dir=Direction.U;
        else if(bD) dir=Direction.D;

        if (dir != oldDir) {
            MoveMessage message = new MoveMessage(id, (int)pX,(int) pY, dir);
            gameClient.getNc().send(message);
        }
    }


    public void render (SpriteBatch sb){
//            System.out.println("Player x: "+ pX);
//            System.out.println("Player y:" + pY);
            sb.draw(player, x - (width / 2), y - (height / 2));
            if(hasSword) {
                sb.draw(sword,(float)(x),y - (height/4),50,50);
            }
            if(hasShield) {
                sb.draw(shield,(float) (x- (width/1.5)),y - (height/2),50,50);
            }
    }

    public void loadPlayerTextures(){
        player_up = new Texture("playerRedBackCrop.png");
        player_middle = new Texture("playerRedFrontCrop.png");
        player_down = new Texture("playerRedFrontCrop.png");
        sword = new Texture("sword.png");
        shield = new Texture("shield.png");
    }

//    public void loadOtherPlayerTextures() {
//        other_player_up = new Texture("playerBlueBackCrop.png");
//        other_player_middle = new Texture("playerBlueFrontCrop.png");
//        other_player_down = new Texture("playerBlueFrontCrop.png");
//        other_player_sword = new Texture("sword1.png");
//        other_player_shield = new Texture("shield1.png");
//    }

    public boolean checkCollisionMap(float possibleX , float possibleY){ // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX + SCROLLTRACKER_X;
        float yWorld = possibleY + SCROLLTRACKER_Y;

        boolean collisionWithMap = false;

        //Check corners of player to check for collision
        //check corners T = top, B = bottom, R = right, L = left
        boolean TLbool= isCellBlocked(xWorld - (width/2) , yWorld + (height/2) );
        boolean TRbool= isCellBlocked(xWorld +( width/2) , yWorld + (height/2));
        boolean BLbool= isCellBlocked(xWorld -(width/2), yWorld - (height/2));
        boolean BRbool= isCellBlocked(xWorld + (width/2), yWorld - (height/2));

        collisionWithMap = TLbool || TRbool || BLbool || BRbool;




        //If there is a collision
        if (collisionWithMap) return false;
        else return true;

    }

    public boolean isCellBlocked(float x, float y) {

        //System.out.println("debug: " + collisionLayer.getTileWidth());
        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isWall");
//        return false;
    }

    public boolean IsCellCoin(float x, float y) {
        Cell cell = coinLayer.getCell(
                (int) (x / coinLayer.getTileWidth()),
                (int) (y / coinLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isCoin");
    }

    public float getSpeed() {
        return speed;
    }

    public void dispose()
    {
        player_up.dispose();
        player_down.dispose();
        player_middle.dispose();
        player.dispose();
    }
    public int getLives() {
        return lives;
    }

}
