package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;
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
	public float x, y;
    private Texture player, player_up, player_middle, player_down, sword,shield;
    private float speed = 6;
    private float width, height;
    //private int lives = 5;

    public boolean hasCoin;
    public boolean hasCompass;
    public boolean hasDamagingPotion;
    public boolean hasHealingPotion;
    public boolean hasShield;
    public boolean hasSword;
    
    public int coins;
    public int health;
    private int ID;
    public int swordDamage;
    
    public String name;
    public ArrayList<String> items;
    public ItemCell position;
    
    private TiledMapTileLayer collisionLayer;

    public Player(TiledMapTileLayer collisionLayer,String name, int ID) {
    	
    	this.health = 5;
        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<>();
        // this.position = new Point(0, 0);
        this.position = new ItemCell();
        this.swordDamage = 0;
        this.ID = ID;
    	
    	this.collisionLayer = collisionLayer;
    	
        x = VIEWPORT_WIDTH / 2;
        y = VIEWPORT_HEIGHT / 2;
       
        loadPlayerTextures();
        
        width = player_middle.getWidth(); 
        height = player_middle.getHeight(); 
        
    }
     
    public void update (float delta){
    	// update player movement
    	
        if (RIGHT_TOUCHED) {
        	//try move player right
            CAMERA_X += speed; 
            //check player isn't in a wall
            if(!checkCollisionMap(x, y)) { 
            	//move player back if needed
            	System.out.println("hit right wall");
               	CAMERA_X -= speed;
            }
          
        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
            	CAMERA_X -= speed;
            	if(!checkCollisionMap(x,y)) {
            		System.out.println("hit left wall");
            		CAMERA_X += speed;
            	}
            }
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
            	CAMERA_Y += speed;
                if(!checkCollisionMap(x, y)) {
                	System.out.println("hit top wall");
                	CAMERA_Y -= speed;
                }
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
            	CAMERA_Y -= speed;
                if(!checkCollisionMap(x, y  )) {
                	System.out.println("hit bottom wall");
                	CAMERA_Y += speed;
                } 
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

    public void render (SpriteBatch sb){
    	sb.draw(player,x- (width/2),y - (height/2));
    
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
    
    public boolean checkCollisionMap(float possibleX , float possibleY){ // true = good to move | false = can't move there
    	//Overall x and y of player
        float xWorld = possibleX + CAMERA_X;
        float yWorld = possibleY + CAMERA_Y; 
        
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
    
    /*public void decreaseHealth() {
        this.health--;
        
        if (this.health == 0) {
          this.death();
        }
      }*/
    public void decreaseHealth(int number) {
        this.health -= number;
        if(health <= 0) {
          this.death();
        }
      }
    
    public void generateHealth() {
        if(health != 5) {
        this.health++;
        }
      }
    
    public void pickUpCoins(int coinPick) {
        this.coins += coinPick;
        
      }
    
    public void pickUpCoins() {
        this.coins++;
      }
    
    public void pickUpItem(Item itemPicked) {
        Collect co = new Collect(); // will need to be changed maybe put the collect class in parameters
        switch(itemPicked.getType()) {
          case "Coin":
            this.pickUpCoins();
            //remove from map
            co.pickedUp(itemPicked);
            break;
          case "Shield":

            hasShield = true;
            co.shield(itemPicked, this);
            hasShield = false;
            break;
          case "Sword":
            hasSword = true;
            //Player player2 = new Player(collisionLayer"Hi", 234);
            //co.sword(itemPicked, this, player2);

            break;
          case "Compass":
            hasCompass = true;
            co.compass(itemPicked);
            break;
          case "Healing Potion":
            hasHealingPotion = true;
            co.healingPotion(itemPicked, this);
            hasDamagingPotion = false;
            break;
          case "Damaging Potion":
            hasDamagingPotion = true;
            co.damagingPotion(itemPicked, this);
            hasDamagingPotion = false;
            break;
          default:
            throw new Exception("Item does not exist yet");
        }
      }
    
    public void death() {
        System.out.println("Player has died respawning now");
        this.health = 5;
        this.coins = 0;

        this.hasCompass = false;
        this.hasDamagingPotion = false;
        this.hasHealingPotion = false;
        this.hasShield = false;
        this.hasSword = false;

        this.items = new ArrayList<>();
      }
    
    public void playerHitPlayer(Player hit) {
        // write boolean to check sword

        if (this.hasSword && !hit.hasShield) {
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

    public void dispose()
    {
        player_up.dispose();
        player_down.dispose();
        player_middle.dispose();
        player.dispose();
    }
   
    public String toString() {
        return "Name: " + this.name + " Health: " + this.health + " Coins: " + this.coins + " Items " + this.items + " Postion: " + position.toString();
      }
    
    public int getLives() {
    	return health;
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
    
}
