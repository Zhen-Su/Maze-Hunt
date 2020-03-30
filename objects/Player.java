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

/**
 * <h1>Player</h1>
 * Class which handles player animation, movment, attack and other things to do with player
 */
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

    Texture frames,walkRight,walkLeft,walkUp,walkDown, coinPick;
    public Coordinate moveTo;
    protected int attackcount;
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
        int attackcount = 0;
        colour = "orange"; //----------default
       
        loadPlayerTextures();
        
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

    /**
     * Gets sword xp
     * @return returns sword xp
     */
    public int getswordXP() {
    	return this.swordXP;
    }

    /**
     * @return  shield cp
     */
    public int getShieldXP() {
    	return this.shieldXP;
    }

    /**
     * @return name of player
     */
    protected String getName() {return this.name;};

    /**
     * Makes an initial postion for the player
     */
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

    /**
     * handles updating player movment and animation
     * @param delta
     * @param mode can be ignored here
     * @param time time for atttacking
     */
    public void update (float delta, int mode, float time){
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
        // update the move to as they contantly get updated in the render method
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

    /**
     * reduces health by number
     * @param number to reduce health by
     */
    public void decreaseHealth(int number) {
      this.health -= number;
    }


    /**
     * Method for a player  to attack another player with a time delay
     * @param playerA
     * @param time
     */
    public void attackP(Player playerA, float time) {
        // first checks the time delay to stop spamming and the plaeyer hans't attacked before

            // checks if the space key is pressed
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                // checks if the player has a swrod and the player its attacking doesn't have a shield
                if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                    if (attackcount <= 8) {
                        this.attackcount++;
                    } else {
//              System.out.println("Player is attacking");
                        // sets is attacking to true
                        isAttacking = true;
                        // animation for sowrd
                        sword = swordAttack;
                        // decreases health by one plus any gearenchatnments
                        playerA.decreaseHealth(1 + getGearCount());
                        if (playerA.health == 0) {
                            // adds the cons to the opposing player
//                        this.coins += playerA.coins;
                            // calls the death mehtod
                            playerA.death(time);

                        }
                        attackcount = 0;
                    }
                }
            } else sword = swordNotAttack;

        // sets the time again and gives tur to startattack
        this.playerAttackTime = time;
        startPAttack = true;
    }

    /**
     * Method to attack an ai player
     * @param playerA
     * @param time
     * @return
     */
    public AIPlayer attackAI(AIPlayer playerA, float time) {
//        if (aiAttackTime - time > 0.3 || !startAIAttack) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

                if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                    if (attackcount <= 8) {
                        this.attackcount++;
                    } else {
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
                        attackcount = 0;
                    }
                }
            }

            startAIAttack = true;
//        }

        return playerA;
    }

    /**
     * Counts the number of gears in the list
     * @return the number of gears
     */
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


    /**
     * generates health for the player by 1
     */
    public void generateHealth() {
        if(this.health != 9) {
            this.health++;
        }
    }

    /**
     * gets the player position
     * @return coordinate of player
     */
    public Coordinate getPosition() {
        return new Coordinate(this.x, this.y);
    }

    /**
     * Causes the player to die and resets everything
     * @param time
     */
    public void death(float time) {
    // death method
        // resets helath and coins and initialpositon
        this.health = 5;
        this.coins = 0;
        this.initialPosition();
        // logs the new moveto cooreinate had some problems with that before
        this.moveTo = new Coordinate(this.position.getX(), this.position.getY());
        // clears the itmes
        this.items.clear();
        // records the death time for respawn
        this.deathTime = time;
        this.haveyoudied = true;


    }









    

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

}
