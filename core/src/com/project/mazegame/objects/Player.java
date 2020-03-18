package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;

import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.*;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.IntIntMap;
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
    private int respawnCounter = 0;
    private BitmapFont font;

    Texture frames,walkRight,walkLeft,walkUp,walkDown, coinPick , swipeRight , swipeLeft , swipeUp , swipeDown , playerDying;

    private boolean isAttacking = false;

    public String name;
    public ArrayList<String> items;
    public Coordinate position;

    private TiledMapTileLayer collisionLayer;

    AnimationTool RightAnim, LeftAnim ,UpAnim ,DownAnim ,animation , DyingAnim;
    AnimationTool coinAnimation,swordSwipeRight,swordSwipeLeft,swordSwipeUp,swordSwipeDown , swipeAnim;
    SpriteBatch batch;

    String colour;

    private boolean isDying = false;
    Timer time;

    public float initialisedShieldTime;
    public float initialisedPotionTime;
    public float initialisedEnchantmentTime;

    private static int shieldIconSize = 50;


    public Player(TiledMapTileLayer collisionLayer,String name, int ID ,String colour) {

        this.health = 5;
        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x,y);
        this.swordDamage = 0;
        this.ID = ID;
        this.collisionLayer = collisionLayer;
        this.colour = colour;
        swordXP = 0;
        shieldXP = 0;
        font = new BitmapFont(Gdx.files.internal("myFont.fnt"), false);

        initialPosition();


        loadPlayerTextures();

        ArrayList<Item> items = new ArrayList<Item>();

        createAnimations();
        time = new Timer();

    }
    public Player(){}

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

        x = this.position.getX();
        y = this.position.getY();
    }

    public void update (float delta){
        removeShield();
        removeEnchantment();
        time.updateTimer(delta);

        // update player movement
        this.position.setX(x);
        this.position.setY(y);

        if(this.isDead()) {
            if(respawnCounter == 0) {
                respawnCounter = time.currentTime();
            }

            if(time.currentTime() - respawnCounter == 3) {
                this.death();

            }
            setAnimation(DyingAnim);

        }else {


            if (RIGHT_TOUCHED) {
                if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) { // if its on map
                    //try move player right
                    x += speed;
                    //check player isn't in a wall
                    if(!checkCollisionMap(x, y))  //if it's in a wall, move player back
                        x -= speed;
                    else
                        this.position.setX( x );
                }
            }
            if (LEFT_TOUCHED) {
                if (x > 0) {
                    x -= speed;
                    if(!checkCollisionMap(x,y))
                        x += speed;
                    else
                        this.position.setX( x );
                }
            }
            if (UP_TOUCHED) {
                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if(!checkCollisionMap(x, y))
                        y -= speed;
                    else
                        this.position.setY( y );
                }
            }
            if (DOWN_TOUCHED) {
                if (y > 0) {
                    y -= speed;
                    if(!checkCollisionMap(x, y  ))
                        y += speed;
                    else
                        this.position.setY( y );

                }
            }

            //change player texture
            if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
                setAnimation( UpAnim);
            } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
                setAnimation(DownAnim);
            }  else if (LEFT_TOUCHED == true && RIGHT_TOUCHED == false) {
                setAnimation( LeftAnim);
            } else if (RIGHT_TOUCHED == true && LEFT_TOUCHED == false) {
                setAnimation( RightAnim);
            } else {
                setAnimation(DownAnim);
            }


        }

    }

    public void render (SpriteBatch sb){

        setBatch(sb);
        animation.render();

        //draw items held by player
//        if(this.items.contains("sword")) {
//            sb.draw(sword,(float)(x),y - (height/4),50,50);
//        }
        if(this.items.contains("shield")) {

            sb.draw(shield,(float) (x- (width/1.5)),y - (height/2),shieldIconSize, shieldIconSize);
        }


        font.getData().setScale(0.5f,0.5f);
        font.draw(sb,this.name, this.position.getX() - 30,this.position.getY() + 60);

        if(this.isDead()) {
            font.getData().setScale(1f,1f);
            String message = "Respawn in: " + ( respawnCounter -  time.currentTime()  +3);
            font.draw(sb,message, this.position.getX() - 100,this.position.getY() + 200);
        }
    }

    //------------------------setters

    public void setAnimation(AnimationTool direction) {
        animation = direction;
    }
    public void setSwordAnimation(AnimationTool direction) {
        swipeAnim = direction;

    }
    public void setBatch(SpriteBatch sb) {
        this.batch = sb;

    }

    //-----------------------getters
    public SpriteBatch getSpriteBatch () {
        return this.batch;
    }

    public Texture getFrames() {
        return frames;
    }

    public int getSwordXP() {
        return this.swordXP;
    }
    public int getShieldXP() {
        return this.shieldXP;
    }
    public int getHealth() {
        return health;
    }
    public float getTime() {
        return this.time.currentTime();
    }
    //-----------------functions
    public void increaseSwordXP(int XP) {
        this.swordXP += XP;
    }
    public void increaseShieldXP(int XP) {
        this.shieldXP += XP;
    }

    public void decreaseHealth(int number) {
        this.health -= number;


    }

    public void generateHealth() {
        if(this.health != 9) {
            this.health++;
        }
    }

    // ---------------------------player functionality

    private void removeShield() {
        if(!this.items.contains("shield")) {
            return;
        }

        if ((time.currentTime()) - initialisedShieldTime  == 10) {
            this.items.remove("shield");
        }
    }

    private void removeEnchantment() {
        if(!this.items.contains("gearEnchantment")) {

            return;
        }
        if ((time.currentTime()) - initialisedEnchantmentTime == 10) {
            this.items.remove("gearEnchantment");

        }
    }

    public void attack() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if(this.items.contains("sword") && !this.isDead()) {
                if (animation.toString().equals(RightAnim.toString()))
                    setSwordAnimation(swordSwipeRight);
                else if (animation.toString().equals(LeftAnim.toString()))
                    setSwordAnimation(swordSwipeLeft);
                else if (animation.toString().equals(UpAnim.toString()))
                    setSwordAnimation(swordSwipeUp);
                else if (animation.toString().equals(DownAnim.toString()))
                    setSwordAnimation(swordSwipeDown);

                //do animation

                swipeAnim.render();
                isAttacking = true;
                sword = swordAttack;
            }
        }

        else {
            sword = swordNotAttack;
            swordSwipeRight.elapsedTime = 0;
            swordSwipeLeft.elapsedTime = 0;
            swordSwipeUp.elapsedTime = 0;
            swordSwipeDown.elapsedTime = 0;
        }
    }

    public boolean isDead() {
        if(this.health <= 0) {
            return true;

        }else
            return false;
    }

    public void death() {
        this.initialPosition();
        setAnimation(DownAnim);
        this.coins = 0;
        this.health = 5;
        this.items.clear();
        this.respawnCounter = 0;




        //this.items = new ArrayList<>();
    }

    public void playerHitPlayer(Player hit) {
        // write boolean to check sword

        if (this.items.contains("sword") && !hit.items.contains("shield")) {
            // will need to do if have item then that can be called
            // then decrease the helath based on that
            // could have a damage do attribute and various attributes which change throught the generateMapItems
            hit.decreaseHealth(this.swordDamage);
            if (hit.isDead()) {
                this.swordDamage++;
                this.coins += hit.coins;

//            respawnCounter = time
                hit.death();
            }
        }
        //need to add shield stuffr
    }

    //-------------------------check collisions

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



    //-------------------------loading textures and animations
    public void createAnimations() {
        width = (int) walkUp.getWidth()/2;
        height = walkUp.getHeight()/2;
        coinSize = coinPick.getHeight()/2;


        frames = walkRight;
        RightAnim = new AnimationTool(width,height,this,walkRight,true);
        RightAnim.create();

        frames = walkLeft;
        LeftAnim = new AnimationTool(width,height,this,walkLeft,true);
        LeftAnim.create();

        frames = walkUp;
        UpAnim = new AnimationTool(width,height,this,walkUp,true);
        UpAnim.create();

        frames = walkDown;
        DownAnim = new AnimationTool(width,height,this,walkDown,true);
        DownAnim.create();

        frames = playerDying;
        DyingAnim = new AnimationTool(width,height,this,playerDying,false);
        DyingAnim.create();
        //Create animations - make the frames but don't render them

        frames = swipeRight;
        swordSwipeRight = new AnimationTool(100, 100, this ,swipeRight, false );
        swordSwipeRight.xOffset = 70;
        swordSwipeRight.yOffset = 0;

        swordSwipeRight.create();

        frames = swipeLeft;
        swordSwipeLeft = new AnimationTool(100, 100, this ,swipeLeft, false );
        swordSwipeLeft.xOffset = -70;
        swordSwipeLeft.yOffset = 0;
        swordSwipeLeft.create();

        frames = swipeUp;
        swordSwipeUp = new AnimationTool(100, 100, this ,swipeUp, false );
        swordSwipeUp.xOffset = 0;
        swordSwipeUp.yOffset = 70;
        swordSwipeUp.create();

        frames = swipeDown;
        swordSwipeDown = new AnimationTool(100, 100, this ,swipeDown, false );
        swordSwipeDown.xOffset = 0;
        swordSwipeDown.yOffset = -70;
        swordSwipeDown.create();

        swipeAnim= new AnimationTool(100, 100, this ,swipeDown, false );
        swipeAnim.create();

        animation = new AnimationTool(width, height, this, walkUp,true);

        animation.create();
        setAnimation( UpAnim);
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

        swipeRight = new Texture ("Player\\swipeRight.png");
        swipeLeft = new Texture ("Player\\swipeLeft.png");
        swipeUp = new Texture ("Player\\swipeUp.png");
        swipeDown = new Texture ("Player\\swipeDown.png");

        playerDying = new Texture ("Player\\playerDying.png");


        sword = swordNotAttack;
    }

    public void dispose()
    {
        walkDown.dispose();
        walkLeft.dispose();
        walkRight.dispose();
        walkUp.dispose();
        // player.dispose();

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
