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
//import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Coordinate;

import java.util.ArrayList;


public class MultiPlayer extends Player {
    private int x, y;
    private float speed = 6;
    private float width, height;
    public int coins;
    public int health = 5;
    private int id;
    public int swordDamage;
    public Coordinate position;
    public boolean isPoisoned;

    private String name;
    public ArrayList<String> items;
    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    private Direction dir;
    private TiledMapTileLayer collisionLayer;
    public static boolean debug = false;


    //constructors=================================================================================
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, MultiPlayerGameScreen gameClient, Direction dir ){
        super();
//        if (debug) System.out.println("My Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.health=5;
        this.coins=0;
        this.name=username;
        this.items=new ArrayList<>();
        this.position=new Coordinate(x,y);

        initialPosition();
        x = this.position.getX();
        y = this.position.getY();

        this.gameClient=gameClient;
        this.dir=dir;
        ArrayList<Item> items = new ArrayList<Item>();
        width = gameClient.player_up.getWidth();
        height = gameClient.player_up.getHeight();
//        if (debug) System.out.println("My Multiplayer instance construction done!");

    }
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir ) {
        super();
//        if (debug) System.out.println("Other Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.health=5;
        this.coins=0;
        this.name=username;
        this.items=new ArrayList<>();
        this.x=x;
        this.y=y;
        this.position=new Coordinate(x,y);
        this.gameClient=gameClient;
        this.dir=dir;
        ArrayList<Item> items = new ArrayList<Item>();
        width = gameClient.player_up.getWidth();
        height = gameClient.player_up.getHeight();
//        if (debug) System.out.println("Other Multiplayer instance construction done!");
    }
    //Getter&Setter=================================================================================
    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y;
    }
    public Direction getDir() { return dir; }

    public void setDir(Direction dir) { this.dir = dir; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getCoins() { return coins; }

    public void setCoins(int coins) { this.coins = coins; }

//    public void setPlayerTexture(Texture player) { this.player = player; }
//
//    public Texture getPlayer_up() { return player_up; }
//
//    public Texture getPlayer_right() { return player_right; }
//
//    public Texture getPlayer_left() { return player_left; }
//
//    public Texture getPlayer_down() { return player_down; }



    //==============================================================================================

    public void render (SpriteBatch sb){

//        if(isPoisoned){
//            switch (dir) {
//                case U:
//                    sb.draw(gameClient.player_up1, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
//                    break;
//                case D:
//                    sb.draw(gameClient.player_down1, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
//                    break;
//                case L:
//                    sb.draw(gameClient.player_left1, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
//                    break;
//                case R:
//                    sb.draw(gameClient.player_right1, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
//                    break;
//                case STOP:
//                    sb.draw(gameClient.player_down1, this.position.getX() - (width / 2), this.position.getY() - (height / 2));
//                    break;
//            }
//        }else {
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

    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.RIGHT:
                bR=true;
                break;
            case Input.Keys.LEFT:
                bL=true;
                break;
            case Input.Keys.UP:
                bU=true;
                break;
            case Input.Keys.DOWN:
                bD=true;
                break;
        }
        locateDirection();
        return true;
    }

    public boolean keyUp (int keycode){
        switch(keycode){
            case Input.Keys.RIGHT:
                bR=false;
                break;
            case Input.Keys.LEFT:
                bL=false;
                break;
            case Input.Keys.UP:
                bU=false;
                break;
            case Input.Keys.DOWN:
                bD=false;
                break;
        }
        locateDirection();
        return true;
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

    /**
     * If direction is changed, send message to server immediately.
     * boolean bL, bU, bR, bD is only used to decide when should send message
     */
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






    // private MazeGame game;

    //    public void pickUpItem(Item itemPicked , Collect co) {
//
//        switch(itemPicked.getType()) {
//          case "Coin":
//            this.coins++;
//            //remove from map
////            co.pickedUp(itemPicked);
//
//            break;
//          case "Shield":
//
//            //hasShield = true;
//            co.shield(itemPicked, this);
//            //hasShield = false;
//            break;
//          case "Sword":
//            hasSword = true;
//            //Player player2 = new Player(collisionLayer"Hi", 234);
//            //co.sword(itemPicked, this, player2);
//
//            break;
//          case "Compass":
//            hasCompass = true;
//            co.compass(itemPicked);
//            break;
//          case "Healing Potion":
//            hasHealingPotion = true;
//            co.healingPotion(this);
//            hasDamagingPotion = false;
//            break;
//          case "Damaging Potion":
//            hasDamagingPotion = true;
//            co.damagingPotion(itemPicked, this);
//            hasDamagingPotion = false;
//            break;
//          /*default:
//            throw new Exception("Item does not exist yet");*/
//        }
//      }
//



    public int getHealth() {
        return health;
    }

    public void dispose() { }


    /*
    public void playerKillAI(AIPlayer AI) {
        if (AI.health == 0) {
        this.pickUpCoins(5);
      } else {
        AI.decreaseHealth(1);
      }
    }

    public void move(ItemCell coord) {
        this.position = (ItemCell) coord;
      }

    public void changeXAndY(int x, int y) {

        this.position.changeX(x);
        this.position.changeY(y);
     }

    public boolean sameSpot(Player h) {
       return this.position.same(h.position);
    }
    public boolean itemOnSquare(Item item) {
       return this.position.same(item.getPosition());
    }

    public float getSpeed() {
    	return speed;
    }


    public String toString() {
        return "Name: " + this.name + " Health: " + this.health + " Coins: " + this.coins + " Items " + this.items + " Postion: " + position.toString();
      }

    public int getID() {
        return this.ID;
    }
    public int getX () {
    	return (int) x;
    }
    public int getY () {
    	return (int) y;
    }
    */

}
