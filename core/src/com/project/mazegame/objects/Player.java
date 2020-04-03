package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.*;

/**
 * Contains the player attributes and carries out the functionality of the player and their animations
 * @author Charlotte
 * @author James
 * @author Andin
 * @author Selma
 */

public class Player {

    public int x, y;
    public MazeGame game;
    public PlayersType playersType;
    protected float speed;
    protected String colour;
    protected String name;
    public ArrayList<String> items;
    public Coordinate position;
    protected int width, height, coinSize;
    public int coins;
    public int health;
    protected int ID;
    protected Direction dir;
    public int swordDamage;
    protected int swordXP;
    protected int shieldXP;
    public int respawnCounter;
    public Coordinate moveTo;
    protected TiledMapTileLayer collisionLayer;
    public Collect co;

    //AI variables
    protected int attackcount;
    protected float aiAttackTime;
    protected float playerAttackTime;
    protected boolean startPAttack;
    protected boolean startAIAttack;
    public boolean isAttacking = false;
    protected boolean pressSpace;

    //Textures
    protected BitmapFont font;
    protected Texture frames, walkRight, walkLeft, walkUp, walkDown, coinPick, swipeRight, swipeLeft, swipeUp, swipeDown, playerDying;
    protected Texture player, sword, swordAttack, swordNotAttack, shield;
    private Texture enchantedGlow;

    //Animations
    protected AnimationTool RightAnim;
    protected AnimationTool LeftAnim;
    protected AnimationTool UpAnim;
    protected AnimationTool DownAnim;
    protected AnimationTool animation;
    protected AnimationTool DyingAnim;
    protected AnimationTool coinAnimation, swordSwipeRight, swordSwipeLeft, swordSwipeUp, swordSwipeDown, swipeAnim;

    private SpriteBatch batch;
    protected Timer time;

    public float initialisedShieldTime;
    public float initialisedPotionTime;
    public float initialisedEnchantmentTime;

    protected boolean haveyoudied;
    protected float deathTime;

    public static int shieldIconSize = 50;


    public Player() {
        this.health = 5;
        this.coins = 0;
        this.items = new ArrayList<>();
        this.position = new Coordinate(x, y);
        this.swordDamage = 0;
        this.respawnCounter = 0;
        this.haveyoudied = false;
        this.speed = 6;
        swordXP = 0;
        shieldXP = 0;
        this.moveTo = new Coordinate(this.position.getX(), this.position.getY());
        time = new Timer();
        this.startPAttack = false;
        this.startAIAttack = false;
        this.dir = Direction.STOP;
    }
    /**
     *
     * @param collisionLayer The layer of the map .tmx file that holds the positions of the walls
     * @param name The chosen name of the player
     * @param ID The player ID number
     * @param colour The chosen colour of the player
     * @param playersType The type of player, single player or multi player
     */
    public Player(TiledMapTileLayer collisionLayer, String name, int ID, String colour, PlayersType playersType) {
        this();
        this.name = name;
        this.ID = ID;
        this.collisionLayer = collisionLayer;
        this.colour = colour;
        this.playersType = playersType;
        this.co = new Collect(this);
        initialPosition();
        loadPlayerTextures();
        createAnimations();
    }
    /**
     *
     * @param collisionLayer The layer of the map .tmx file that holds the positions of the walls
     * @param username The chosen name of the player
     */
    public Player(TiledMapTileLayer collisionLayer, String username) {
    }
    /**
     *
     * @param collisionLayer The layer of the map .tmx file that holds the positions of the walls
     * @param username The chosen name of the player
     * @param x the x position
     * @param y the y position
     * @param dir the direction of the player
     */
    public Player(TiledMapTileLayer collisionLayer, String username, int x, int y, Direction dir) {
    }


    //Getter&Setter=================================================================================
    /**
     * gets the player id
     * @return int
     */
    public int getID() {
        return ID;
    }

    /**
     * sets the player id
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    /**
     * gets the player direction
     * @return player direction
     */
    public Direction getDir() {
        return dir;
    }
    /**
     * sets the player direction
     * @param dir
     */

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    /**
     * gets the players x position
     * @return int x
     */
    public int getX() {
        return x;
    }
    /**
     * sets the players x position
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * gets the players y position
     * @return int y
     */
    public int getY() {
        return y;
    }
    /**
     * sets the players y position
     *@param y
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * gets the player name
     * @return String name
     */
    public String getName() {
        return name;
    }
    /**
     * sets the player name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * sets the player animation
     * @param direction
     */
    public void setAnimation(AnimationTool direction) {
        animation = direction;
    }
    /**
     * sets the sword animation
     * @param direction
     */
    public void setSwordAnimation(AnimationTool direction) {
        swipeAnim = direction;
    }
    /**
     * sets the sprite batch
     * @param sb
     */
    public void setBatch(SpriteBatch sb) {
        this.batch = sb;
    }
    /**
     * gets the sprite batch
     * @return SpriteBatch
     */
    public SpriteBatch getSpriteBatch() {
        return this.batch;
    }
    /**
     * gets the texture frames
     * @return frames
     */
    public Texture getFrames() {
        return frames;
    }
    /**
     * gets the sword xp
     * @return int swordxp
     */
    public int getSwordXP() {
        return this.swordXP;
    }
    /**
     * gets the shield xp
     * @return shield xp
     */
    public int getShieldXP() {
        return this.shieldXP;
    }
    /**
     * gets the player health
     * @return int health
     */
    public int getHealth() {
        return health;
    }
    /**
     * gets the current time
     * @return float time
     */
    public float getTime() {
        return this.time.currentTime();
    }
    /**
     * gets the player colour
     * @return String colour
     */
    public String getColour() {
        return colour;
    }
    /**
     * sets the player colour
     * @param colour
     */
    public void setColour(String colour) {
        this.colour = colour;
    }
    /**
     * gets the collect object
     * @return collect
     */
    public Collect getCo() {
        return co;
    }
    /**
     * gets the mazgame
     * @return game
     */
    public MazeGame getGame() {
        return game;
    }
    /**
     * sets the mazegame
     * @param game
     */
    public void setGame(MazeGame game) {
        this.game = game;
    }

    //==============================================================================================

    /**
     * Chooses the initial player position at the start of the game, and when they respawn.
     * It recursively loops until a position has been chosen that isn't a wall.
     */
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

    /**
     * Updates the player position a their texture/animation
     * @param delta
     * @param mode
     * @param worldTime
     */
    public void update(float delta, int mode, float worldTime) {
        removeShield();
        removeEnchantment();
        time.updateTimer(delta);

        // update player movement
        this.position.setX(x);
        this.position.setY(y);

        if (this.isDead()) {
            if (this.respawnCounter == 0) this.respawnCounter = time.currentTime();

            if (time.currentTime() - this.respawnCounter == 3) this.death(worldTime);
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
                this.dir = Direction.R;
            }
            if (LEFT_TOUCHED) {
                if (x > 0) {
                    x -= speed;
                    if (!checkCollisionMap(x, y))
                        x += speed;
                    else
                        this.position.setX(x);
                }
                this.dir = Direction.L;
            }
            if (UP_TOUCHED) {
                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if (!checkCollisionMap(x, y))
                        y -= speed;
                    else
                        this.position.setY(y);
                }
                this.dir = Direction.U;
            }
            if (DOWN_TOUCHED) {
                if (y > 0) {
                    y -= speed;
                    if (!checkCollisionMap(x, y))
                        y += speed;
                    else
                        this.position.setY(y);

                }
                this.dir = Direction.D;
            }
        }

        // update the move to as they contantly get updated in the render method
        moveTo.setX(x);
        moveTo.setY(y);
    }

    /**
     * draws the player and their name above their head as well as any items they are carrying
     * @param sb
     */

    public void render(SpriteBatch sb) {
        setBatch(sb);

        if (this.isDead()) {
            setAnimation(DyingAnim);
            font.getData().setScale(1f, 1f);
            String message = "Respawn in: " + (respawnCounter - time.currentTime() + 3);
            font.draw(sb, message, this.position.getX() - 100, this.position.getY() + 200);
//            animation.render();
        } else {
            //need to change AI animation here
            switch (this.dir) {
                case U:
                    setAnimation(UpAnim);
                    break;
                case D:
                    setAnimation(DownAnim);
                    break;
                case R:
                    setAnimation(RightAnim);
                    break;
                case L:
                    setAnimation(LeftAnim);
                    break;
                case STOP:
                    setAnimation(DownAnim);
                    break;
            }
        }

        if (this.items.contains("gearEnchantment"))
            sb.draw(enchantedGlow, this.position.getX() - enchantedGlow.getWidth() / 2, this.position.getY() - enchantedGlow.getHeight() / 2, enchantedGlow.getWidth(), enchantedGlow.getHeight());

        animation.render();

        if (this.items.contains("sword"))
            sb.draw(sword, (float) (x), y - (height / 4), 50, 50);

        if (this.items.contains("shield"))
            sb.draw(shield, (float) (x - (width / 1.5)), y - (height / 2), shieldIconSize, shieldIconSize);



        font.getData().setScale(0.5f, 0.5f);
        font.draw(sb, this.name, this.position.getX() - 30, this.position.getY() + 60);
    }

    //-----------------functions
    /**
     * increases the sword xp
     * @param xp
     */
    public void increaseSwordXP(int XP) {
        this.swordXP += XP;
    }
    /**
     * increasers shield xp
     * @param xp
     */
    public void increaseShieldXP(int XP) {
        this.shieldXP += XP;
    }
    /**
     * decrease player health
     * @param number
     */
    public void decreaseHealth(int number) {
        this.health -= number;
    }
    /**
     * generates player health
     */
    public void generateHealth() {
        if (this.health != 9) {
            this.health++;
        }
    }

    // ---------------------------player functionality
    /**
     * After 10 seconds the shield is removed from player items
     */
    public void removeShield() {
        if (!this.items.contains("shield")) {
            return;
        }

        if ((time.currentTime()) - initialisedShieldTime == 10) {
            this.items.remove("shield");

        }
    }
    /**
     * After 10 seconds the gear enchantment is removed from player items
     */
    public void removeEnchantment() {
        if (!this.items.contains("gearEnchantment")) {
            return;
        }
        if ((time.currentTime()) - initialisedEnchantmentTime == 10) {
            this.items.remove("gearEnchantment");

        }
    }

    public Coordinate getPosition() {
        return new Coordinate(this.x, this.y);
    }

    /**
     * Sets the sword swipe animation.
     * If the player is holding a sword and is alive they can attack.
     */

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
                game.audio.atk();
                isAttacking = false;
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

    /**
     * determines whether a player is dead
     * @return true if player is currently dead
     */
    public boolean isDead() {
        if (this.health <= 0) {
            haveyoudied = true;
            return true;
        } else
            return false;
    }
    /**
     * once player has been dead for 3 seconds this method is called to allow the player to respawn
     * @param time the current time
     */
    public void death(float time) {
        this.initialPosition();
        setAnimation(DownAnim);
        this.coins = 0;
        this.health = 5;
        this.items.clear();
        this.moveTo = new Coordinate(this.position.getX(), this.position.getY());
        this.respawnCounter = 0;
        this.deathTime = time;
        this.haveyoudied = true;

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
//      if (aiAttackTime - time > 0.3 || !startAIAttack) {
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

                    if (playerA.health == 0) {
                        this.coins += 1;
                        System.out.println("I am about to die");
                        this.coins += playerA.coins;
                        playerA.death(time);

                    }
                    attackcount = 0;
                }
            }
        }

        startAIAttack = true;
//      }

        return playerA;
    }


    /**
     *  counts the amount of gear enchantments
     * @return the number of gearEnchantments
     */

    protected int getGearCount() {
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals("gearEnchantment"))
                count++;
        }
        return count;
    }


    /**
     * Checks whether the player can move into a certain position
     * @param possibleX
     * @param possibleY
     * @return true if player can move there | false if they cannot
     */

    public boolean checkCollisionMap(float possibleX, float possibleY) {
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

    /**
     * used by checkCollisionMap
     * @param x
     * @param y
     * @return true if there is a wall at given coordinates
     */
    public boolean isCellBlocked(float x, float y) {

        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isWall");
    }

    /**
     * creating animations
     */
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
    /**
     * Retrieving player textures from asset manager
     */

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
        enchantedGlow = Assets.manager.get(Assets.ENCHANTED, Texture.class);

        sword = swordNotAttack;
    }


    public void dispose() {
//        walkDown.dispose();
//        walkLeft.dispose();
//        walkRight.dispose();
//        walkUp.dispose();
        // player.dispose();

    }

}
