package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Coordinate;

import java.util.ArrayList;


public class MultiPlayer extends Player {
    public int x, y;
    private Texture player, player_up, player_right, player_left, player_down, sword,shield;
    private float speed = 6;
    private float width, height;
    public int coins;
    private int health = 5;
    private int id;
    public int swordDamage;
    public Coordinate position;

    private String name;
    public ArrayList<String> items;
    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    private Direction dir = Direction.STOP;
    private TiledMapTileLayer collisionLayer;


    //constructors=================================================================================
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir ) {
        super();
        System.out.println("multiplayer is constructing...");
        this.health=5;
        this.coins=0;
        this.name=username;
        this.items=new ArrayList<>();
        this.position=new Coordinate(x,y);
        this.gameClient=gameClient;
        this.dir=dir;
        this.collisionLayer = collisionLayer;

        loadPlayerTextures();

        width = player_up.getWidth();
        height = player_up.getHeight();

        ArrayList<Item> items = new ArrayList<Item>();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                            loadPlayerTextures();
//                            width = player_middle.getWidth();
//                            height = player_middle.getHeight();
//                        }
//                });
//            }
//        }).start();

        System.out.println("multiplayer construction done!");
    }


    //Getter&Setter=================================================================================
    public Direction getDir() { return dir; }

    public void setDir(Direction dir) { this.dir = dir; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Coordinate getPosition() { return position; }

    public void setPosition(Coordinate position) { this.position = position; }

    public Texture getPlayerTexture() { return player; }

    public void setPlayerTexture(Texture player) { this.player = player; }

    public Texture getPlayer_up() { return player_up; }

    public void setPlayer_up(Texture player_up) { this.player_up = player_up; }

    public Texture getPlayer_right() { return player_right; }

    public void setPlayer_right(Texture player_right) { this.player_right = player_right; }

    public Texture getPlayer_left() { return player_left; }

    public void setPlayer_left(Texture player_left) { this.player_left = player_left; }

    public Texture getPlayer_down() { return player_down; }

    public void setPlayer_down(Texture player_down) { this.player_down = player_down; }
    //==============================================================================================

    // update player movement
    public void update (float delta){

       this.position.setX((int)x);
       this.position.setY((int)y);

        if (RIGHT_TOUCHED) {
            //set direction boolean true
            bR=true;
            //try move player right
             this.x+=speed;
            //change direction
            locateDirection();
            //check player isn't in a wall
            if(!checkCollisionMap(x, y)) {
                //move player back if needed
                this.x -= speed;
            }
            //change it back to the original value
            bR=false;
        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
                bL=true;
                this.x -= speed;
                locateDirection();
                if(!checkCollisionMap(x,y)) {
                    this.x += speed;
                }
            }
            bL=false;
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
                bU=true;
                this.y += speed;
                locateDirection();
                if(!checkCollisionMap(x, y)) {
                    this.y -= speed;
                }
                bU=false;
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
                bD=true;
                this.y -= speed;
                locateDirection();
                if(!checkCollisionMap(x, y)) {
                    this.y += speed;
                }
                bD=false;
            }
        }

        //change player texture
        if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
            player = player_up;
        } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
            player = player_down;
        }  else if (LEFT_TOUCHED == true && RIGHT_TOUCHED == false) {
            player = player_left;
        } else if (RIGHT_TOUCHED == true && LEFT_TOUCHED == false) {
            player = player_right;
        }else {
            player = player_down;
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
            MoveMessage message = new MoveMessage(id, this.position.getX(),this.position.getY(), dir);
            gameClient.getNc().send(message);
        }
    }
    public void loadPlayerTextures(){

        player_up = new Texture("playerRedBackCrop.png");
        player_right = new Texture("playerRedRightCrop.png");
        player_left = new Texture("playerRedLeftCrop.png");
        player_down = new Texture("playerRedFrontCrop.png");
        sword = new Texture("sword2.png");
        shield = new Texture("shield.png");
    }

}
