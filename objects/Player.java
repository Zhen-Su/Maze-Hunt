package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;

import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.*;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
//import com.project.mazegame.Pair;
//import com.project.mazegame.Player;
import com.project.mazegame.tools.*;

public class Player {
	public int x, y;
    protected Texture player, sword,swordAttack,swordNotAttack,shield;
    private float speed = 6;
    private int width, height ,coinSize;
    public int coins;
    public int health = 5;
    private int ID;
    public int swordDamage;
    private int swordXP;
    private int shieldXP;
    private ArrayList<Player> otherPlayers;
    Texture frames,walkRight,walkLeft,walkUp,walkDown, coinPick;
    public Coordinate moveTo;
    private Thread playerThread;
    private float aiAttackTime;
    private float playerAttackTime;
    protected float deathTime;
    private boolean startPAttack;
    private boolean startAIAttack;
    protected boolean haveyoudied;
    
    protected boolean isAttacking = false;
    
    public String name;
    public ArrayList<String> items;
    public Coordinate position;
   
    private TiledMapTileLayer collisionLayer;
    
    AnimationTool RightAnim;
    AnimationTool LeftAnim;
    AnimationTool UpAnim;
    AnimationTool DownAnim;
    AnimationTool animation;
    AnimationTool coinAnimation;
    SpriteBatch batch;
    
    String colour;
    
    String initialFrames;

    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    public Player() {}

    public Player(TiledMapTileLayer collisionLayer,String name, int ID) {
    	
    	this.health = 5;
        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x,y);
        this.swordDamage = 0;
        this.ID = ID;
    	this.collisionLayer = collisionLayer;
    	this.haveyoudied = false;
    	initialPosition();
        x = this.position.getX();
        y = this.position.getY();
        this.moveTo = new Coordinate(this.position.getX(), this.position.getY());
        this.startPAttack = false;
        this.startAIAttack = false;

        colour = "orange"; //----------default
       
        loadPlayerTextures();
        playerThread = new Thread(new PlayerThread());
//        playerThread.setPriority(1);
//        playerThread.start();
        
        width = (int) walkUp.getWidth()/2; 
        height = walkUp.getHeight()/2; 
        coinSize = coinPick.getHeight()/2;
        ArrayList<Item> items = new ArrayList<Item>();
        
        frames = walkDown;
//        System.out.println("new animations");
        RightAnim = new AnimationTool(width,height,this,walkRight,true);
        RightAnim.create();
//        System.out.println("new animations");
        LeftAnim = new AnimationTool(width,height,this,walkLeft,true);
        LeftAnim.create();
//        System.out.println("new animations");
        UpAnim = new AnimationTool(width,height,this,walkUp,true);
        UpAnim.create();
//        System.out.println("new animations");
        DownAnim = new AnimationTool(width,height,this,walkDown,true);
        DownAnim.create();
//        System.out.println("new animations");
        coinAnimation = new AnimationTool(width,height,this,coinPick,true);
        coinAnimation.create();
        animation = new AnimationTool(width,height,this,walkDown,true);
        animation.create();
        
        
        swordXP = 0;
        shieldXP = 0;
        
    }


    public int getswordXP() {
    	return this.swordXP;
    }
    public int getShieldXP() {
    	return this.shieldXP;
    }
    protected String getName() {return this.name;};
    
    public void initialPosition () {

    	int maxX = this.collisionLayer.getWidth() ;
//    	System.out.println("Is here");
//    	System.out.println(maxX);
    	int maxY= this.collisionLayer.getHeight();

    	int ranx = (int)  (( Math.random() * (maxX) ));
    	int rany = (int)  (( Math.random() * (maxY) ));

		this.position.setX( ranx * (int) this.collisionLayer.getTileWidth() + 50);
		this.position.setY( rany * (int) this.collisionLayer.getTileHeight() + 50);
		
		if(isCellBlocked((float)position.getX(), (float)position.getY())) {
			initialPosition();
		}
    }


    public void update (float delta, int mode, ArrayList<Item> items, float time){
        // update player movement
//        playerThread.run();
        if (this.haveyoudied) {
            if (deathTime - time > 5) {
                this.haveyoudied = false;
            }
        }
        if (!haveyoudied) {
            this.position.setX(x);
            this.position.setY(y);


            if (RIGHT_TOUCHED) {

                right = true;


                if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) { // if its on map
                    //try move player right
                    x += speed;
                    //check player isn't in a wall
                    if (!checkCollisionMap(x, y)) { //if it's in a wall, move player back
                        x -= speed;

                    } else
                        this.position.setX(x);
                }
            }
            if (LEFT_TOUCHED) {
                left = true;

                if (x > 0) {
                    x -= speed;
                    if (!checkCollisionMap(x, y)) {
                        x += speed;

                    } else
                        this.position.setX(x);
                }
            }
            if (UP_TOUCHED) {
                up = true;

                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if (!checkCollisionMap(x, y)) {
                        y -= speed;

                    } else
                        this.position.setY(y);
                }
            }
            if (DOWN_TOUCHED) {
                down = true;

                if (y > 0) {
                    y -= speed;
                    if (!checkCollisionMap(x, y)) {
                        y += speed;

                    } else
                        this.position.setY(y);

                }
            }


            //change player texture
            if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
                // player = player_up;
                frames = walkUp;
                animation.setFrames(UpAnim.getFrames());
//            System.out.println(animation.getImgName());


            } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
                //player = player_down;
                frames = walkDown;
                animation.setFrames(DownAnim.getFrames());

            } else if (LEFT_TOUCHED == true && RIGHT_TOUCHED == false) {
                //player = player_left;
                frames = walkLeft;
                animation.setFrames(LeftAnim.getFrames());


            } else if (RIGHT_TOUCHED == true && LEFT_TOUCHED == false) {
                //player = player_right;
                frames = walkRight;
                animation.setFrames(RightAnim.getFrames());

            }
        }
        moveTo.setX(x);
        moveTo.setY(y);

    }
   

    public void render (SpriteBatch sb){
    	
    	
    	setBatch(sb);
//        System.out.println(animation.getImgName());
    	animation.render();
    	
        if(this.items.contains("sword")) {	  // possible errors may occur
            sb.draw(sword,(float)(x),y - (height/4),50,50);
        }
         if(this.items.contains("shield")) {
            sb.draw(shield,(float) (x- (width/1.5)),y - (height/2),50,50);
        }
         
        

    }
    
    public void setBatch(SpriteBatch sb) {
    	this.batch = sb;
    	
    }
    public SpriteBatch getSpriteBatch () {
    	return this.batch;
    }
     
    public void loadPlayerTextures(){
    	 
    	switch (colour) {
    	case "blue":
    		walkRight = new Texture("Player\\walkRightBlue.png");
            walkLeft = new Texture("Player\\walkLeftBlue.png");
            walkUp = new Texture("Player\\walkUpBlue.png");
            walkDown = new Texture("Player\\walkDownBlue.png");
            break;
    	case "green":
    		walkRight = new Texture("Player\\walkRightGreen.png");
            walkLeft = new Texture("Player\\walkLeftGreen.png");
            walkUp = new Texture("Player\\walkUpGreen.png");
            walkDown = new Texture("Player\\walkDownGreen.png");
            break;
    	case "pink":
    		walkRight = new Texture("Player\\walkRightPink.png");
            walkLeft = new Texture("Player\\walkLeftPink.png");
            walkUp = new Texture("Player\\walkUpPink.png");
            walkDown = new Texture("Player\\walkDownPink.png");
            break;
    	case "orange":
    		walkRight = new Texture("Player\\walkRightOrange.png");
            walkLeft = new Texture("Player\\walkLeftOrange.png");
            walkUp = new Texture("Player\\walkUpOrange.png");
            walkDown = new Texture("Player\\walkDownOrange.png");
            break;
    	case "lilac":
    		walkRight = new Texture("Player\\walkRightLilac.png");
            walkLeft = new Texture("Player\\walkLeftLilac.png");
            walkUp = new Texture("Player\\walkUpLilac.png");
            walkDown = new Texture("Player\\walkDownLilac.png");
            break;
    	case "yellow":
    		walkRight = new Texture("Player\\walkRightYellow.png");
            walkLeft = new Texture("Player\\walkLeftYellow.png");
            walkUp = new Texture("Player\\walkUpYellow.png");
            walkDown = new Texture("Player\\walkDownYellow.png");
            break;
    	default:
    		walkRight = new Texture("Player\\walkRight.png");
            walkLeft = new Texture("Player\\walkLeft.png");
            walkUp = new Texture("Player\\walkUp.png");
            walkDown = new Texture("Player\\walkDown.png");
            
    	}
        
        coinPick = new Texture("Collectibles\\coinAnimation.png");
        
        swordAttack = new Texture("Collectibles\\swordAttack.png");
        swordNotAttack = new Texture("Collectibles\\sword2.png");
        shield = new Texture("Collectibles\\shield.png");
        
        
        sword = swordNotAttack;
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

    	Cell cell = this.collisionLayer.getCell(
            (int) (x / this.collisionLayer.getTileWidth()),
            (int) (y / this.collisionLayer.getTileHeight()));

    	return cell != null && cell.getTile() != null
            & cell.getTile().getProperties().containsKey("isWall");
    }
    
    public void decreaseHealth(int number) {
      this.health -= number;
    }
    

    
    public void attackP(Player playerA, float time) {
        if (playerAttackTime - time > 0.3 || !startPAttack) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (this.items.contains("sword") && !playerA.items.contains("shield")) {
//              System.out.println("Player is attacking");
                    isAttacking = true;
                    sword = swordAttack;
                    playerA.decreaseHealth(1 + getGearCount());
                    if (playerA.health == 0) {
                        this.coins += playerA.coins;
                        playerA.death(time);

                    }

                }
            } else sword = swordNotAttack;
        }
        this.playerAttackTime = time;
        startPAttack = true;
    }

    public AIPlayer attackAI(AIPlayer playerA, float time) {
        if (aiAttackTime - time > 0.3 || !startAIAttack) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                    System.out.println("Player as attacking me");
                    isAttacking = true;
                    sword = swordAttack;
                    int gearEnchantCount = 0;

                    playerA.decreaseHealth(1 + getGearCount());
                    this.coins += 1;
                    if (playerA.health == 0) {
                        System.out.println("I am about to die");
                        this.coins += playerA.coins;
                        playerA.death(time);

                    }
                }
            }
            this.aiAttackTime = time;
            startAIAttack = true;
        }

        return playerA;
    }
    protected int getGearCount() {
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals("gearEnchantment"))
                count++;
        }
        return count;
    }
    
    public Texture getFrames() {
    	return frames;
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


    public Coordinate genSpace() {
        int maxX = collisionLayer.getWidth();
        int maxY = collisionLayer.getHeight();
        int minY = 0;
        int minX = 0;
        int firstX = (int) (Math.random() * maxX - minX + 1) + minX;
        int firstY = (int) (Math.random() * maxY - minY + 1) + minY;
        boolean canGo = checkCollisionMap(firstX, firstY);
        while(!canGo) {
            firstX = (int) (Math.random() * maxX - minX + 1) + minX;
            firstY = (int) (Math.random() * maxY - minY + 1) + minY;
        }
        return new Coordinate(firstX, firstY);
    }

    public void death(float time) {

//        System.out.println("Player has died respawning now");
        this.health = 5;
        this.coins = 0;
        this.initialPosition();
        this.moveTo = new Coordinate(this.position.getX(), this.position.getY());
        this.items.clear();
        this.deathTime = time;
        this.haveyoudied = true;

        //this.items = new ArrayList<>();
    }

    public void playerHitPlayer(Player hit) {

    }



    public boolean isplayerSameSpace(ArrayList<Player> players) { return this.getPosition().getX() == nearestPlayer(players).getPosition().getX() && this.getPosition().getY() == nearestPlayer(players).getPosition().getY();}
    /*
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
    */

    public boolean hasSword () {return this.items.contains("Sword"); }
    public boolean hasShield () {return this.items.contains("Shield"); }


    

    public int getHealth() {
    	return health;
    }
    public void dispose()
    {
        walkDown.dispose();
        walkLeft.dispose();
        walkRight.dispose();
        walkUp.dispose();
       // player.dispose();

    }
    protected int getID() {return this.ID;}

   
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
    
   
    
*/
}
