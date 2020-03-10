package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;
import static java.lang.Math.random;

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
    public ArrayList<Player> otherPlayers;
    public int maxX, minX, maxY, minY;

    
    private TiledMapTileLayer collisionLayer;
    public Player() {}
    public Player(TiledMapTileLayer collisionLayer,String name, Collect co) {
    	this.co = co;
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
    public int getID() {return this.ID;}
    public void update (float delta, int mode, Collect lets){
    	// update player movement

            this.position.setX((int) x);
            this.position.setY((int) y);

            if (RIGHT_TOUCHED) {
                //try move player right
                this.x += speed;
                //check player isn't in a wall
                if (!checkCollisionMap(x, y)) {
                    //move player back if needed

                    this.x -= speed;
                }

            }
            if (LEFT_TOUCHED) {
                if (x > 0) {
                    this.x -= speed;
                    if (!checkCollisionMap(x, y)) {

                        this.x += speed;
                    }
                }
            }
            if (UP_TOUCHED) {
                if (y < VIEWPORT_HEIGHT - height) {
                    this.y += speed;
                    if (!checkCollisionMap(x, y)) {

                        this.y -= speed;
                    }
                }
            }
            if (DOWN_TOUCHED) {
                if (y > 0) {
                    this.y -= speed;
                    if (!checkCollisionMap(x, y)) {

                        this.y += speed;
                    }
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
    	 
        player_up = new Texture("playerRedBackCrop.png");
        player_right = new Texture("playerRedFrontCrop.png");
        player_left = new Texture("playerRedFrontCrop.png");
        player_down = new Texture("playerRedFrontCrop.png");
        sword = new Texture("sword.png");
        shield = new Texture("shield.png");
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

    public Coordinate getPosition() {
        return new Coordinate(this.x, this.y);
    }
    public Player nearestPlayer(ArrayList<Player> players) {
        Coordinate position = new Coordinate();
        position = players.get(0).getPosition();
        Player nearestPlayer = players.get(0);
//		Item nearestItem = new Item(" ", position);

        //System.out.println("items: " + mapItems.size());
        for (int i = 0; i < players.size(); i++) {

            int tempX = players.get(i).getPosition().getX();
            int tempY = players.get(i).getPosition().getY();

//			int tempDist =player.position.getX() + player.position.getY() - tempX - tempY;
            int tempDist = andinsEuclidianForPlayers(this.position.getX(), tempX, this.position.getY(), tempY);
            //System.out.println("temp Dist: " + tempDist);
//			int shortDist = player.position.getX() + player.position.getY() - nearestItem.getPosition().getX() - nearestItem.getPosition().getY();
            int shortDist = andinsEuclidianForPlayers(this.position.getX(), nearestPlayer.getPosition().getX(), this.position.getY(), nearestPlayer.getPosition().getY());
            //System.out.println("shortDist: " + shortDist);

            if (tempDist < shortDist) {
                nearestPlayer = players.get(i);
                //System.out.println("found shorter!");
            }
        }
        //System.out.println(nearestItem.getPosition().getX() + " , " + nearestItem.getPosition().getY());
        return nearestPlayer;
    }

    private int andinsEuclidianForPlayers(int x1, int x2, int y1, int y2) {
        int sqrEucl = ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
//		System.out.println()
        return sqrEucl;
    }


    public Coordinate genSpace(int minX, int maxX, int minY, int maxY) {
        int firstX = (int) (Math.random() * maxX - minX + 1) + minX;
        int firstY = (int) (Math.random() * maxY - minY + 1) + minY;
        boolean canGo = checkCollisionMap(firstX, firstY);
        while(!canGo) {
             firstX = (int) (Math.random() * maxX - minX + 1) + minX;
            firstY = (int) (Math.random() * maxY - minY + 1) + minY;
        }
        return new Coordinate(firstX, firstY);
    }

    public void death() {
        System.out.println("Player has died respawning now");
        this.health = 5;
        this.coins = 0;
        Coordinate newPlace = genSpace(minX, maxX, minY, maxY);
        this.x = newPlace.getX();
        this.y = newPlace.getY();
        this.items.clear();

        //this.items = new ArrayList<>();
      }
    
    public void playerHitPlayer(Player hit) {

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

    public boolean isplayerSameSpace(ArrayList<Player> players) { return this.getPosition().getX() == nearestPlayer(players).getPosition().getX() && this.getPosition().getY() == nearestPlayer(players).getPosition().getY();}

    public void playerOnSameSpace(Player player1) {
        // first thing is in event of key press
        if (isplayerSameSpace(otherPlayers)) {
            if (SPACE_TOUCHED && this.hasSword() && !player1.hasShield()) {
                player1.decreaseHealth(1);
                if (player1.health == 0) {
                    player1.death();
                }
            }
        }
    }

    public boolean hasSword () {return this.items.contains("Sword"); }
    public boolean hasShield () {return this.items.contains("Shield"); }

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
