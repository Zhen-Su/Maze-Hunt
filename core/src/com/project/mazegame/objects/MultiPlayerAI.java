package com.project.mazegame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;

import java.util.ArrayList;

public class MultiPlayerAI extends AIPlayer {
    private int x, y;
    private float speed = 6;
    private float width, height;
    public int coins;
    public int health;
    private int id;
    public int swordDamage;
    public Coordinate position;
    public boolean isPoisoned;

    private String name;
    public ArrayList<String> items;
    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    public Direction dir;
    private TiledMapTileLayer collisionLayer;
    public static boolean debug = false;
    private Collect co;

    public MultiPlayerAI(TiledMapTileLayer collisionLayer, String username, MultiPlayerGameScreen gameClient, Collect co, Direction dir) {
        super();
        this.collisionLayer=collisionLayer;
        initialPosition();
        this.name=username;
        coins=0;
        health=5;
        this.co = co;
        x = this.position.getX();
        y = this.position.getY();
        this.gameClient = gameClient;
        this.dir = dir;
        ArrayList<Item> items = new ArrayList<>();
        width = gameClient.player_up.getWidth();
        height = gameClient.player_up.getHeight();

    }

    public MultiPlayerAI(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir, Collect co) {
        super();
        this.x = x;
        this.y = y;
        this.position = new Coordinate(x,y);
        this.gameClient = gameClient;
        this.dir = dir;
        ArrayList<Item> items = new ArrayList<>();
        width = gameClient.player_up.getWidth();
        height = gameClient.player_up.getHeight();

    }

    public int getId() {return this.id;}
    public int getX() {return this.x;}
    public void setX(int x) {this.x = x;}
    public int getY() {return this.y;}
    public void setY(int y) { this.y = y; }
    public String getName() { return this.name; }

    public void setId(int id) {this.id = id;}
    public Collect getCo() {return this.co;}

    @Override
    public void render (SpriteBatch sb) {
        switch (dir) {
            case U:
                sb.draw(gameClient.player_up, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
                break;
            case D:
                sb.draw(gameClient.player_down, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
                break;
            case L:
                sb.draw(gameClient.player_left, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
                break;
            case R:
                sb.draw(gameClient.player_right, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
                break;
            case STOP:
                sb.draw(gameClient.player_down, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
                break;
        }
        //}

        updateMotion();

        if(this.items.contains("sword")) {
            sb.draw(gameClient.sword,(float)(x),y - (height/4),50,50);
        }
        if(this.items.contains("shield")) {
            sb.draw(gameClient.shield,(float) (x- (width/1.5)),y - (height/2),50,50);
        }
    }

    public void updateMotion(){

        this.position.setX(x);
        this.position.setY(y);

        switch (dir){
            case R:
                if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) { // if its on map
                    //try move player right
                    x += speed;
                    //check player isn't in a wall
                    if(!checkCollisionMap(x, y)) { //if it's in a wall, move player back
                        x -= speed;
                    }else
                        this.position.setX( x );
                }
                break;
            case L:
                if (x > 0) {
                    x -= speed;
                    if(!checkCollisionMap(x,y)) {
                        x += speed;
                    }else
                        this.position.setX( x );
                }
                break;
            case U:
                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if(!checkCollisionMap(x, y)) {
                        y -= speed;
                    }else
                        this.position.setY( y );
                }
                break;
            case D:
                if (y > 0) {
                    y -= speed;
                    if(!checkCollisionMap(x, y  )) {
                        y += speed;
                    } else
                        this.position.setY( y );
                }
                break;
            case STOP:
                break;
        }
    }

    private void locateDirection(){
        Direction oldDir = this.dir;

        if(bR&&!bL&&!bD&&!bU)
            dir = Direction.R;
        else if(bL&&!bR&&!bU&&!bD)
            dir=Direction.L;
        else if(bU&&!bD&&!bL&&!bR)
            dir=Direction.U;
        else if(bD&&!bU&&!bR&&!bL)
            dir=Direction.D;
        else if(!bL&&!bD&&!bR&&!bU)
            dir=Direction.STOP;

        if (dir != oldDir) {
            MoveMessage message = new MoveMessage(id, this.position.getX(),this.position.getY(), dir);
            gameClient.getNc().send(message);
        }
    }
    public void initialPosition () {
        //Coordinate position = new Coordinate();

        int maxX = collisionLayer.getWidth() ;
        int maxY= collisionLayer.getHeight();
//    	System.out.println("maxX" + maxX + " , " + maxY);

        int ranx = (int)  (( Math.random() * (maxX) ));
        int rany = (int)  (( Math.random() * (maxY) ));
//    	System.out.println("ran" + ranx + " , " + rany);


        this.position.setX( ranx * (int) collisionLayer.getTileWidth() + 50);
        this.position.setY( rany * (int) collisionLayer.getTileHeight() + 50);


        if(isCellBlocked((float)position.getX(), (float)position.getY())) {
            initialPosition();
        }
    }

}
