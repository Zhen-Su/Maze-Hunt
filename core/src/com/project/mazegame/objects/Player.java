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
    private Texture player, sword,swordAttack,swordNotAttack,shield;
    private float speed = 6;
    private int width, height ,coinSize;
    public int coins;
    public int health = 5;
    private int ID;
    public int swordDamage;
    private int swordXP;
    private int shieldXP;

    Texture frames,walkRight,walkLeft,walkUp,walkDown, coinPick;

    private boolean isAttacking = false;

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

        initialPosition();
        x = this.position.getX();
        y = this.position.getY();

        colour = "orange"; //----------default

        loadPlayerTextures();

        width = (int) walkUp.getWidth()/2;
        height = walkUp.getHeight()/2;
        coinSize = coinPick.getHeight()/2;
        ArrayList<Item> items = new ArrayList<Item>();

        frames = walkDown;
        System.out.println("new animations");
        RightAnim = new AnimationTool(width,height,this,walkRight,true);
        RightAnim.create();
        System.out.println("new animations");
        LeftAnim = new AnimationTool(width,height,this,walkLeft,true);
        LeftAnim.create();
        System.out.println("new animations");
        UpAnim = new AnimationTool(width,height,this,walkUp,true);
        UpAnim.create();
        System.out.println("new animations");
        DownAnim = new AnimationTool(width,height,this,walkDown,true);
        DownAnim.create();
        System.out.println("new animations");
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

    public void update (float delta){
        // update player movement

        this.position.setX(x);
        this.position.setY(y);



        if (RIGHT_TOUCHED) {

            right = true;


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
            left = true;

            if (x > 0) {
                x -= speed;
                if(!checkCollisionMap(x,y)) {
                    x += speed;

                }else
                    this.position.setX( x );
            }
        }
        if (UP_TOUCHED) {
            up = true;

            if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                y += speed;
                if(!checkCollisionMap(x, y)) {
                    y -= speed;

                }else
                    this.position.setY( y );
            }
        }
        if (DOWN_TOUCHED) {
            down = true;

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
            // player = player_up;
            frames = walkUp;
            animation.setFrames(UpAnim.getFrames());
            System.out.println(animation.getImgName());


        } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
            //player = player_down;
            frames = walkDown;
            animation.setFrames(DownAnim.getFrames());

        }  else if (LEFT_TOUCHED == true && RIGHT_TOUCHED == false) {
            //player = player_left;
            frames = walkLeft;
            animation.setFrames(LeftAnim.getFrames());


        } else if (RIGHT_TOUCHED == true && LEFT_TOUCHED == false) {
            //player = player_right;
            frames = walkRight;
            animation.setFrames(RightAnim.getFrames());

        }
    }


    public void render (SpriteBatch sb){


        setBatch(sb);
        System.out.println(animation.getImgName());
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

    public void attack() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if(this.items.contains("sword")) {

                System.out.println("hit");
                isAttacking = true;
                sword = swordAttack;

            }
        }
        else sword = swordNotAttack;
    }

    public Texture getFrames() {
        return frames;
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
        walkDown.dispose();
        walkLeft.dispose();
        walkRight.dispose();
        walkUp.dispose();
        // player.dispose();

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
