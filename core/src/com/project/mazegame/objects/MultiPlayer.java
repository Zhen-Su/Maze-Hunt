package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.InputHandler;

import java.util.ArrayList;


public class MultiPlayer extends Player {

    private int x, y;
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
    private Direction dir;
    private TiledMapTileLayer collisionLayer;


    //constructors=================================================================================
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir ) {
        super();
        System.out.println("multiplayer is constructing...");
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

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                            loadPlayerTextures();
                            width = player_up.getWidth();
                            height = player_up.getHeight();
                        }
                });

        System.out.println("multiplayer construction done!");
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

    public void render (SpriteBatch sb){

       // update(Gdx.graphics.getDeltaTime());

        switch(dir){
            case U:
                sb.draw(player_up,this.position.getX()- (width/2),this.position.getY() - (height/2));
                break;
            case D:
                sb.draw(player_down,this.position.getX()- (width/2),this.position.getY() - (height/2));
                break;
            case L:
                sb.draw(player_left,this.position.getX()- (width/2),this.position.getY() - (height/2));
                break;
            case R:
                sb.draw(player_right,this.position.getX()- (width/2),this.position.getY() - (height/2));
                break;
            case STOP:
                sb.draw(player_down,this.position.getX()- (width/2),this.position.getY() - (height/2));
                break;
        }
        move();

        if(this.items.contains("sword")) {	  // possible errors may occur
            sb.draw(sword,(float)(x),y - (height/4),50,50);
        }
        if(this.items.contains("shield")) {
            sb.draw(shield,(float) (x- (width/1.5)),y - (height/2),50,50);
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

    // update player movement
//    @Override
//    public void update (float delta){
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            bR=true;
//        }else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//                bL=true;
//        }else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//                bU=true;
//        }else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//                bD=true;
//        }else{
//            //if no input, then this player direction is STOP.
//            bR=false;
//            bL=false;
//            bU=false;
//            bD=false;
//        }
//        locateDirection();
//        move();
//        changeTexture();
//
//        //reset,and reset direction
//        bR=false;
//        bL=false;
//        bU=false;
//        bD=false;
//        locateDirection();
//        //move();
//
//    }
//
//    public void changeTexture(){
//        if (bU&& !bD) {
//            player = player_up;
//        } else if (bD&& !bU) {
//            player = player_down;
//        }  else if (bL&& !bR) {
//            player = player_left;
//        } else if (bR && !bL) {
//            player = player_right;
//        }else {
//            player = player_down;
//        }
//    }

    public void move(){

        this.position.setX(x);
        this.position.setY(y);

        switch (dir){
            case R:
                this.x+=speed;
                if(!checkCollisionMap(x, y)) {
                    this.x -= speed;
                    System.out.println("hit right wall");
                }
                break;
            case L:
                if(x>0) {
                    this.x -= speed;
                    if(!checkCollisionMap(x,y)) {
                        this.x += speed;
                        System.out.println("hit left wall");
                    }
                }
                break;
            case U:
                if (y < VIEWPORT_HEIGHT - height) {
                    this.y += speed;
                    if(!checkCollisionMap(x, y)) {
                        this.y -= speed;
                        System.out.println("hit up wall");
                    }
                }
                break;
            case D:
                if(y>0){
                    this.y -= speed;
                    if(!checkCollisionMap(x, y)) {
                        this.y += speed;
                        System.out.println("hit down wall");
                    }
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

    @Override
    public void loadPlayerTextures(){
        player_up = new Texture("playerRedBackCrop.png");
        player_right = new Texture("playerRedRightCrop.png");
        player_left = new Texture("playerRedLeftCrop.png");
        player_down = new Texture("playerRedFrontCrop.png");
        sword = new Texture("sword2.png");
        shield = new Texture("shield.png");
    }

    public boolean checkCollisionMap(float possibleX , float possibleY){ // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX ;
        float yWorld = possibleY ;

        boolean collisionWithMap = false;

        //Check corners of player to check for collision
        //check corners T = top, B = bottom, R = right, L = left
        boolean TLbool= isCellBlocked(xWorld - (width/2) , yWorld + (height/2),collisionLayer );
        boolean TRbool= isCellBlocked(xWorld +( width/2) , yWorld + (height/2),collisionLayer);
        boolean BLbool= isCellBlocked(xWorld -(width/2), yWorld - (height/2),collisionLayer);
        boolean BRbool= isCellBlocked(xWorld + (width/2), yWorld - (height/2),collisionLayer);

        collisionWithMap = TLbool || TRbool || BLbool || BRbool;

        //If there is a collision
        if (collisionWithMap) return false;
        else return true;
    }

    public boolean isCellBlocked(float x, float y,TiledMapTileLayer collisionLayer) {

        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y /collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isWall");
    }

    public void decreaseHealth(int number) {
        this.health -= number;
        if(health <= 0) {
            this.death();
        }
    }

    public void generateHealth() {
        if(this.health != 9) {
            this.health++;
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
    public void death() {
        System.out.println("Player has died respawning now");
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
    public void dispose()
    {
        player_up.dispose();
        player_down.dispose();
        player_right.dispose();
        player_left.dispose();
    }


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
