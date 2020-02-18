package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.tools.Pair;


import java.util.ArrayList;
//TODO Look into super class and work on rendering ai
//TODO integrate collectibles
//TODO look into more ai and player methods


public class Player {
    public float x, y;
    protected Texture player, player_up, player_middle, player_down, sword,shield;
    protected float speed = 6;
    protected float width, height;
    protected int lives = 5;
    protected boolean hasSword = true;
    protected boolean hasShield = true;
    protected TiledMapTileLayer collisionLayer, coinLayer;
    protected MapLayer objLayer;


    public boolean hasCompass;
    public boolean hasDamagingPotion;
    public boolean hasHealingPotion;

    public int coins;
    private int ID;
    public int swordDamage;

    public String name;
    public ArrayList<String> items;



    public Player(TiledMapTileLayer collisionLayer, String name, int ID) {
        this.collisionLayer = collisionLayer;
//        Pair gen = genSpace(60, 0, 60, 0);
//        x = gen.getX();
//        y = gen.getY();
        // will need to adnust so cam is still central
        x = VIEWPORT_WIDTH / 2;
        y = VIEWPORT_HEIGHT / 2;

        loadPlayerTextures();

        width = player_middle.getWidth();
        height = player_middle.getHeight();

        this.coins = 0;
        this.name = name;
        this.items = new ArrayList<String>();
        this.swordDamage = 0;
        this.ID = ID;

    }
    // modify and override

    public void update (float delta){
        // update player movement
	// will look to see if there is some way of implementing that in ai class	
        if (RIGHT_TOUCHED) {
            //try move player right
            SCROLLTRACKER_X += speed;
            //check player isn't in a wall
            if(!checkCollisionMap(x, y)) {
                //move player back if needed
                System.out.println(x);
                System.out.println(y);
                System.out.println("hit right wall");
                SCROLLTRACKER_X -= speed;
            }

        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
                System.out.println(x);
                SCROLLTRACKER_X -= speed;
                if(!checkCollisionMap(x,y)) {
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println("hit left wall");
                    SCROLLTRACKER_X += speed;
                }
            }
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
                SCROLLTRACKER_Y += speed;
                if(!checkCollisionMap(x, y)) {
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println("hit top wall");
                    SCROLLTRACKER_Y -= speed;
                }
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
                SCROLLTRACKER_Y -= speed;
                if(!checkCollisionMap(x, y  )) {
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println("hit bottom wall");
                    SCROLLTRACKER_Y += speed;
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
    //modify and overiride
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

    public Pair genSpace(float maxX, float minX, float maxY, float minY) {
        float x = (float)Math.random() * (maxX - minX + 1) + minX;
        float y = (float)Math.random() * (maxY - minY + 1) + minY;
        while(!checkCollisionMap(x, y)) {
            x = (float)Math.random() * (maxX - minX + 1) + minX;
            y = (float)Math.random() * (maxY - minY + 1) + minY;
        }

        return new Pair(x, y);
    }

    public boolean checkCollisionMap(float possibleX , float possibleY){ // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX + SCROLLTRACKER_X;
        float yWorld = possibleY + SCROLLTRACKER_Y;

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

        //System.out.println("debug: " + collisionLayer.getTileWidth());
        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isWall");
//        return false;
    }

    public boolean IsCellCoin(float x, float y) {
        Cell cell = coinLayer.getCell(
                (int) (x / coinLayer.getTileWidth()),
                (int) (y / coinLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isCoin");
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

    public void decreaseHealth() {
        this.lives--;

        if (this.lives == 0) {
            this.death();
        }
    }

    public void pickUpCoins(int coinPick) {
        this.coins += coinPick;
    }
    public void pickUpCoins() {
        this.coins++;
    }

    public void death() {
        System.out.println("Player has died respawning now");
        this.lives = 5;
        this.coins = 0;
        Pair newSpace = genSpace(100, 0, 100, 0);
        this.x = newSpace.getX();
        this.y = newSpace.getY();

        this.hasCompass = false;
        this.hasDamagingPotion = false;
        this.hasHealingPotion = false;
        this.hasShield = false;
        this.hasSword = false;

        this.items = new ArrayList<>();
    }
    /*
    public void pickUpItem(Item itemPicked) throws Exception {
        Collect co = new Collect(); // will need to be changed maybe put the collect class in parameters
        switch(itemPicked.getType()) {
            case "Coin":
                this.pickUpCoins();
                break;
            case "Shield":

                hasShield = true;
//        co.shield(itemPicked, this);
                hasShield = false;
                break;
            case "Sword":
                hasSword = true;
                Player player2 = new Player("Hi", 234);
                co.sword(itemPicked, this, player2);

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
    */
    public void decreaseHealth(int number) {
        if(!hasShield) {
            this.lives -= number;

            if (lives <= 0) {
                this.death();
            }
        }
    }

    public boolean sameSpot(Player h) {
        return (this.x == h.x) && (this.y == h.y);
    }
    /*
    public boolean itemOnSquare(Item item) {
        return this.position.same(item.getPosition());
    }
    */

    public int getLives() {
        return lives;
    }
    public int getID() {return this.ID;}

    public void playerHitPlayer(Player hit) {


        if (this.hasSword && !hit.hasShield) {

            hit.decreaseHealth(this.swordDamage);
            if (hit.lives == 0) {
                this.swordDamage++;
                this.coins += hit.coins;
                hit.death();
            }
        }
        //need to add shield stuffr
    }

}
