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
    
    public String name;
    public ArrayList<String> items;
    public Coordinate position;
    
    private TiledMapTileLayer collisionLayer;

    public Texture getPlayer() {
        return player;
    }

    public void setPlayer(Texture player) {
        this.player = player;
    }

    public Texture getPlayer_up() {
        return player_up;
    }

    public void setPlayer_up(Texture player_up) {
        this.player_up = player_up;
    }

    public Texture getPlayer_right() {
        return player_right;
    }

    public void setPlayer_right(Texture player_right) {
        this.player_right = player_right;
    }

    public Texture getPlayer_left() {
        return player_left;
    }

    public void setPlayer_left(Texture player_left) {
        this.player_left = player_left;
    }

    public Texture getPlayer_down() {
        return player_down;
    }

    public void setPlayer_down(Texture player_down) {
        this.player_down = player_down;
    }

    public Texture getSword() {
        return sword;
    }

    public void setSword(Texture sword) {
        this.sword = sword;
    }

    public Texture getShield() {
        return shield;
    }

    public void setShield(Texture shield) {
        this.shield = shield;
    }

    public Player(){}

    public Player(TiledMapTileLayer collisionLayer,String name, int ID) {
    	
    	this.health = 5;
        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x,y);
        this.swordDamage = 0;
        this.ID = ID;
    	this.collisionLayer = collisionLayer;
    	
        x = VIEWPORT_WIDTH / 2;
        y = VIEWPORT_HEIGHT / 2;
       
        loadPlayerTextures();
        
        width = player_up.getWidth(); 
        height = player_up.getHeight(); 
        ArrayList<Item> items = new ArrayList<Item>();
        
    }
     
    public void update (float delta){
    	// update player movement
    	this.position.setX((int) x ); 
    	this.position.setY((int) y ); 
        
        if (RIGHT_TOUCHED) {
        	//try move player right
            this.x += speed; 
            //check player isn't in a wall
            if(!checkCollisionMap(x, y)) { 
            	//move player back if needed
            	
               	this.x -= speed;
            }
          
        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
            	this.x -= speed;
            	if(!checkCollisionMap(x,y)) {
            		
            		this.x += speed;
            	}
            }
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
            	this.y += speed;
                if(!checkCollisionMap(x, y)) {
                	
                	this.y -= speed;
                }
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
            	this.y -= speed;
                if(!checkCollisionMap(x, y  )) {
                	
                	this.y += speed;
                } 
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
