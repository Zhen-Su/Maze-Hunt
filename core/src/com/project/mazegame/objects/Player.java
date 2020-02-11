package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;


public class Player {
    public float x, y;
    private Texture player, player_up, player_middle, player_down, sword,shield;
    private float speed = 6;
    private float width, height;
    private int lives = 5;
    private boolean hasSword = true;
    private boolean hasShield = true;
    private TiledMapTileLayer collisionLayer, coinLayer;
    private MapLayer objLayer;

    public Player(TiledMapTileLayer collisionLayer) {
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
            SCROLLTRACKER_X += speed;
            //check player isn't in a wall
            if(!checkCollisionMap(x, y)) {
                //move player back if needed
                System.out.println("hit right wall");
                SCROLLTRACKER_X -= speed;
            }

        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
                SCROLLTRACKER_X -= speed;
                if(!checkCollisionMap(x,y)) {
                    System.out.println("hit left wall");
                    SCROLLTRACKER_X += speed;
                }
            }
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
                SCROLLTRACKER_Y += speed;
                if(!checkCollisionMap(x, y)) {
                    System.out.println("hit top wall");
                    SCROLLTRACKER_Y -= speed;
                }
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
                SCROLLTRACKER_Y -= speed;
                if(!checkCollisionMap(x, y  )) {
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
    public int getLives() {
        return lives;
    }

}
