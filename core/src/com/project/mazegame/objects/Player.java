package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;

import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.*;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.tools.*;
//import com.project.mazegame.Pair;
//import com.project.mazegame.Player;
import com.project.mazegame.tools.Collect;

public class Player {
    public int x, y;
    protected Texture player, player_up, player_right, player_left, player_down, sword,shield;
    private float speed = 6;
    private float width, height;
    public int coins;
    public int health = 5;
    private int ID;
    public int swordDamage;
    public Collect co;
    public String name;
    public ArrayList<String> items;
    public Coordinate position;
    private int swordXP;
    private int shieldXP;


    private TiledMapTileLayer collisionLayer;
    public Player() {}
    public Player(TiledMapTileLayer collisionLayer,String name, Collect co,int ID) {
        this.co = co;
        this.health = 5;
        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x,y);
        this.swordDamage = 0;
        this.ID = ID;
        this.collisionLayer = collisionLayer;

        initialPosition();
        x = this.position.getX();
        y = this.position.getY();


        loadPlayerTextures();

        width = player_up.getWidth();
        height = player_up.getHeight();
        ArrayList<Item> items = new ArrayList<Item>();

        swordXP = 0;
        shieldXP = 0;

    }
    public int getswordXP() {
        return this.swordXP;
    }

    public int getShieldXP() {
        return this.shieldXP;
    }

    public int getID() {return this.ID;}

    public void update (float delta){
        // update player movement

        this.position.setX(x);
        this.position.setY(y);

        
        if (RIGHT_TOUCHED) {


            if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) { // if its on map
                //try move player right
                x += speed;
                //check player isn't in a wall
                if(!checkCollisionMap(x, y)) { //if it's in a wall, move player back
                    x -= speed;

                }else
                    this.position.setX( x );
            }
        }
        if (LEFT_TOUCHED) {

            if (x > 0) {
                x -= speed;
                if(!checkCollisionMap(x,y)) {
                    x += speed;

                }else
                    this.position.setX( x );
            }
        }
        if (UP_TOUCHED) {

            if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                y += speed;
                if(!checkCollisionMap(x, y)) {
                    y -= speed;

                }else
                    this.position.setY( y );
            }
        }
        if (DOWN_TOUCHED) {

            if (y > 0) {
                y -= speed;
                if(!checkCollisionMap(x, y  )) {
                    y += speed;

                } else
                    this.position.setY( y );
            }
        }

        //change player texture
        if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
            player = player_up;
        } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
            player = player_down;
        } else if (LEFT_TOUCHED == true && RIGHT_TOUCHED == false) {
            player = player_left;
        } else if (RIGHT_TOUCHED == true && LEFT_TOUCHED == false) {
            player = player_right;
        } else {
            player = player_down;
        }

    }

    public void initialPosition () {
        int maxX = collisionLayer.getWidth() ;
        int maxY= collisionLayer.getHeight();

        int ranx = (int)  (( Math.random() * (maxX) ));
        int rany = (int)  (( Math.random() * (maxY) ));

        this.position.setX( ranx * (int) collisionLayer.getTileWidth() + 50);
        this.position.setY( rany * (int) collisionLayer.getTileHeight() + 50);

        if(isCellBlocked((float)position.getX(), (float)position.getY())) {
            initialPosition();
        }
    }

    private Texture change(Coordinate old, Coordinate update) {
        if (old.getX() < update.getX() && old.getY() == update.getY()) {
            return player_right;
        } else if (old.getX() > update.getX() && old.getY() == update.getY()) {
            return player_left;
        } else if (old.getX() == update.getX() && old.getY() < update.getY()) {
            return player_up;
        } else if (old.getX() == update.getX() && old.getY() > update.getY()) {
            return player_down;
        } else {
            return player;
        }

    }
    public void render (SpriteBatch sb){
        sb.draw(player,x- (width/2),y - (height/2));

        if(this.items.contains("sword")) {	  // possible errors may occur
            sb.draw(sword,(float)(x),y - (height/4),50,50);
        }
        if(this.items.contains("shield")) {
            sb.draw(shield,(float) (x- (width/1.5)),y - (height/2),50,50);
        }

    }

    public void loadPlayerTextures(){

        player_up = new Texture("Player\\playerRedBackCrop.png");
        player_right = new Texture("Player\\playerRedFrontCrop.png");
        player_left = new Texture("Player\\playerRedFrontCrop.png");
        player_down = new Texture("Player\\playerRedFrontCrop.png");
        sword = new Texture("Collectibles\\sword.png");
        shield = new Texture("Collectibles\\shield.png");
    }

    public boolean checkCollisionMap(float possibleX , float possibleY){ // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX ;
        float yWorld = possibleY ;

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

        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

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


    /*
    public void update(float delta) {
        while(true) {
            try {
                // contantsnatly throwing exeption possibly becasue not linked to player
                Coordinate moveToTake = direction(avaibleMoves(x, y));
                System.out.println(moveToTake.toString());
                /*
                if (x == moveToTake.getX() && y < moveToTake.getY()) {
                    SCROLLTRACKER_Y += super.speed;
                } else if (x == moveToTake.getX() && y > moveToTake.getY()) {
                    SCROLLTRACKER_Y -= super.speed;
                } else if (y == moveToTake.getY() && x < moveToTake.getX()) {
                    SCROLLTRACKER_X += super.speed;
                } else if (y == moveToTake.getY() && x > moveToTake.getX()) {
                    SCROLLTRACKER_X -= super.speed;
                }

                this.x = (int) moveToTake.getX();
                this.y = (int) moveToTake.getY();
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Something gone wrong");
            }
        }
    }

     */


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
        player.dispose();
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
