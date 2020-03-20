package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.tools.*;

public class Player {

    private AssetManager manager;
    protected int x, y;
    protected float speed = 6;
    protected String colour;
    protected String name;
    public ArrayList<String> items;
    public Coordinate position;
    protected int width, height, coinSize;
    public int coins;
    public int health = 5;
    protected int ID;
    protected Direction dir;
    public int swordDamage;
    protected int swordXP;
    protected int shieldXP;
    protected int respawnCounter = 0;
    protected TiledMapTileLayer collisionLayer;

    private float aiAttackTime;
    private float playerAttackTime;
    private boolean startPAttack;
    private boolean startAIAttack;
    protected boolean isAttacking = false;

    protected BitmapFont font;
    protected Texture frames, walkRight, walkLeft, walkUp, walkDown, coinPick, swipeRight, swipeLeft, swipeUp, swipeDown, playerDying;
    protected Texture player, sword, swordAttack, swordNotAttack, shield;

    protected AnimationTool RightAnim, LeftAnim, UpAnim, DownAnim, animation, DyingAnim;
    protected AnimationTool coinAnimation, swordSwipeRight, swordSwipeLeft, swordSwipeUp, swordSwipeDown, swipeAnim;
    private SpriteBatch batch;

    protected boolean isDying = false;
    protected Timer time;

    public float initialisedShieldTime;
    public float initialisedPotionTime;
    public float initialisedEnchantmentTime;

    public static int shieldIconSize = 50;


    public Player(TiledMapTileLayer collisionLayer, String name, int ID, String colour) {

        this.health = 5;
        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x, y);
        this.swordDamage = 0;
        this.ID = ID;
        this.collisionLayer = collisionLayer;
        this.colour = colour;
        swordXP = 0;
        shieldXP = 0;

        initialPosition();

        loadPlayerTextures();
        createAnimations();
        time = new Timer();
    }

    public Player(TiledMapTileLayer collisionLayer, String username) {
    }

    public Player(TiledMapTileLayer collisionLayer, String username, int x, int y, Direction dir) {
    }

    //Getter&Setter=================================================================================
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnimation(AnimationTool direction) {
        animation = direction;
    }

    public void setSwordAnimation(AnimationTool direction) {
        swipeAnim = direction;
    }

    public void setBatch(SpriteBatch sb) {
        this.batch = sb;
    }

    public SpriteBatch getSpriteBatch() {
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

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    //==============================================================================================


    public void initialPosition() {
        int maxX = collisionLayer.getWidth();
        int maxY = collisionLayer.getHeight();

        int ranx = (int) ((Math.random() * (maxX)));
        int rany = (int) ((Math.random() * (maxY)));

        this.position.setX(ranx * (int) collisionLayer.getTileWidth() + 50);
        this.position.setY(rany * (int) collisionLayer.getTileHeight() + 50);

        if (isCellBlocked((float) position.getX(), (float) position.getY())) {
            initialPosition();
        }

        x = this.position.getX();
        y = this.position.getY();
    }

    public void update(float delta) {
        removeShield();
        removeEnchantment();
        time.updateTimer(delta);

        // update player movement
        this.position.setX(x);
        this.position.setY(y);

        if (this.isDead()) {
            if (respawnCounter == 0) {
                respawnCounter = time.currentTime();
            }

            if (time.currentTime() - respawnCounter == 3) {
                this.death();
            }
            setAnimation(DyingAnim);

        } else {

            if (RIGHT_TOUCHED) {
                if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) { // if its on map
                    //try move player right
                    x += speed;
                    //check player isn't in a wall
                    if (!checkCollisionMap(x, y))  //if it's in a wall, move player back
                        x -= speed;
                    else
                        this.position.setX(x);
                }
            }
            if (LEFT_TOUCHED) {
                if (x > 0) {
                    x -= speed;
                    if (!checkCollisionMap(x, y))
                        x += speed;
                    else
                        this.position.setX(x);
                }
            }
            if (UP_TOUCHED) {
                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if (!checkCollisionMap(x, y))
                        y -= speed;
                    else
                        this.position.setY(y);
                }
            }
            if (DOWN_TOUCHED) {
                if (y > 0) {
                    y -= speed;
                    if (!checkCollisionMap(x, y))
                        y += speed;
                    else
                        this.position.setY(y);

                }
            }

            //change player texture
            if (UP_TOUCHED && !DOWN_TOUCHED) {
                setAnimation(UpAnim);
            } else if (DOWN_TOUCHED && !UP_TOUCHED) {
                setAnimation(DownAnim);
            } else if (LEFT_TOUCHED && !RIGHT_TOUCHED) {
                setAnimation(LeftAnim);
            } else if (RIGHT_TOUCHED && !LEFT_TOUCHED) {
                setAnimation(RightAnim);
            } else {
                setAnimation(DownAnim);
            }
        }
    }

    public void render(SpriteBatch sb) {

        setBatch(sb);
        animation.render();

        //draw items held by player
//        if(this.items.contains("sword")) {
//            sb.draw(sword,(float)(x),y - (height/4),50,50);
//        }
        if (this.items.contains("shield")) {

            sb.draw(shield, (float) (x - (width / 1.5)), y - (height / 2), shieldIconSize, shieldIconSize);
        }


        font.getData().setScale(0.5f, 0.5f);
        font.draw(sb, this.name, this.position.getX() - 30, this.position.getY() + 60);

        if (this.isDead()) {
            font.getData().setScale(1f, 1f);
            String message = "Respawn in: " + (respawnCounter - time.currentTime() + 3);
            font.draw(sb, message, this.position.getX() - 100, this.position.getY() + 200);
            animation.render();
        }
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
        if (this.health != 9) {
            this.health++;
        }
    }

    // ---------------------------player functionality

    public void removeShield() {
        if (!this.items.contains("shield")) {
            return;
        }

        if ((time.currentTime()) - initialisedShieldTime == 10) {
            this.items.remove("shield");

        }
    }

    public void removeEnchantment() {
        if (!this.items.contains("gearEnchantment")) {
            return;
        }
        if ((time.currentTime()) - initialisedEnchantmentTime == 10) {
            this.items.remove("gearEnchantment");

        }
    }

    public void attack() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (this.items.contains("sword") && !this.isDead()) {
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
        } else {
            sword = swordNotAttack;
            swordSwipeRight.elapsedTime = 0;
            swordSwipeLeft.elapsedTime = 0;
            swordSwipeUp.elapsedTime = 0;
            swordSwipeDown.elapsedTime = 0;
        }
    }

    public boolean isDead() {
        if (this.health <= 0) {
            return true;

        } else
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

    public boolean checkCollisionMap(float possibleX, float possibleY) { // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX;
        float yWorld = possibleY;

        boolean collisionWithMap = false;

        //Check corners of player to check for collision
        //check corners T = top, B = bottom, R = right, L = left
        boolean TLbool = isCellBlocked(xWorld - (width / 2), yWorld + (height / 2));
        boolean TRbool = isCellBlocked(xWorld + (width / 2), yWorld + (height / 2));
        boolean BLbool = isCellBlocked(xWorld - (width / 2), yWorld - (height / 2));
        boolean BRbool = isCellBlocked(xWorld + (width / 2), yWorld - (height / 2));

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

        width = walkUp.getWidth() / 2;
        height = walkUp.getHeight() / 2;
        coinSize = coinPick.getHeight() / 2;

        frames = walkRight;
        RightAnim = new AnimationTool(width, height, this, walkRight, true);
        RightAnim.create();

        frames = walkLeft;
        LeftAnim = new AnimationTool(width, height, this, walkLeft, true);
        LeftAnim.create();

        frames = walkUp;
        UpAnim = new AnimationTool(width, height, this, walkUp, true);
        UpAnim.create();

        frames = walkDown;
        DownAnim = new AnimationTool(width, height, this, walkDown, true);
        DownAnim.create();

        frames = playerDying;
        DyingAnim = new AnimationTool(width, height, this, playerDying, false);
        DyingAnim.create();
        //Create animations - make the frames but don't render them

        frames = swipeRight;
        swordSwipeRight = new AnimationTool(100, 100, this, swipeRight, false);
        swordSwipeRight.xOffset = 70;
        swordSwipeRight.yOffset = 0;

        swordSwipeRight.create();

        frames = swipeLeft;
        swordSwipeLeft = new AnimationTool(100, 100, this, swipeLeft, false);
        swordSwipeLeft.xOffset = -70;
        swordSwipeLeft.yOffset = 0;
        swordSwipeLeft.create();

        frames = swipeUp;
        swordSwipeUp = new AnimationTool(100, 100, this, swipeUp, false);
        swordSwipeUp.xOffset = 0;
        swordSwipeUp.yOffset = 70;
        swordSwipeUp.create();

        frames = swipeDown;
        swordSwipeDown = new AnimationTool(100, 100, this, swipeDown, false);
        swordSwipeDown.xOffset = 0;
        swordSwipeDown.yOffset = -70;
        swordSwipeDown.create();

        swipeAnim = new AnimationTool(100, 100, this, swipeDown, false);
        swipeAnim.create();

        animation = new AnimationTool(width, height, this, walkUp, true);

        animation.create();
        setAnimation(UpAnim);
    }

    public void loadPlayerTextures() {

        switch (colour) {
            case "blue":
                walkRight = Assets.manager.get(Assets.walkRightBlue, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeftBlue, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUpBlue, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDownBlue, Texture.class);
                break;
            case "green":
                walkRight = Assets.manager.get(Assets.walkRightGreen, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeftGreen, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUpGreen, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDownGreen, Texture.class);
                break;
            case "pink":
                walkRight = Assets.manager.get(Assets.walkRightPink, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeftPink, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUpPink, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDownPink, Texture.class);
                break;
            case "orange":
                walkRight = Assets.manager.get(Assets.walkRightOrange, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeftOrange, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUpOrange, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDownOrange, Texture.class);
                break;
            case "lilac":
                walkRight = Assets.manager.get(Assets.walkRightLilac, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeftLilac, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUpLilac, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDownLilac, Texture.class);
                break;
            case "yellow":
                walkRight = Assets.manager.get(Assets.walkRightYellow, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeftYellow, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUpYellow, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDownYellow, Texture.class);
                break;
            default:
                walkRight = Assets.manager.get(Assets.walkRight, Texture.class);
                walkLeft = Assets.manager.get(Assets.walkLeft, Texture.class);
                walkUp = Assets.manager.get(Assets.walkUp, Texture.class);
                walkDown = Assets.manager.get(Assets.walkDown, Texture.class);
        }

        coinPick = Assets.manager.get(Assets.coinPick, Texture.class);
        swordAttack = Assets.manager.get(Assets.swordAttack, Texture.class);
        swordNotAttack = Assets.manager.get(Assets.swordNotAttack, Texture.class);
        shield = Assets.manager.get(Assets.shield, Texture.class);
        swipeRight = Assets.manager.get(Assets.swipeRight, Texture.class);
        swipeLeft = Assets.manager.get(Assets.swipeLeft, Texture.class);
        swipeUp = Assets.manager.get(Assets.swipeUp, Texture.class);
        swipeDown = Assets.manager.get(Assets.swipeDown, Texture.class);
        playerDying = Assets.manager.get(Assets.playerDying, Texture.class);
        font = Assets.manager.get(Assets.font, BitmapFont.class);

        sword = swordNotAttack;
    }

    public void attackP(Player playerA, float time) {
        if (playerAttackTime - time > 0.3 || !startPAttack) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (this.items.contains("sword") && !playerA.items.contains("shield")) {
//              System.out.println("Player is attacking");
                    isAttacking = true;
                    sword = swordAttack;
                    playerA.decreaseHealth(1);
                    if (playerA.health == 0) {
                        this.coins += playerA.coins;
                        playerA.death();
                    }

                }
            } else sword = swordNotAttack;
        }
        this.playerAttackTime = time;
        startPAttack = true;
    }

    public void attackAI(AIPlayer playerA, float time) {
        if (aiAttackTime - time > 0.3 || !startAIAttack) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                    System.out.println("Player as attacking me");
                    isAttacking = true;
                    sword = swordAttack;
                    playerA.decreaseHealth(1);
                    if (playerA.health == 0) {
                        System.out.println("I am about to die");
                        this.coins += playerA.coins;
                        playerA.death();
                    }
                }
            }
        }
        this.aiAttackTime = time;
        startAIAttack = true;
    }

    public void dispose() {
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
