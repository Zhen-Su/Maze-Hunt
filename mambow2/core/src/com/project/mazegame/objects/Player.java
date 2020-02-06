package com.project.mazegame.objects;

import static com.project.mazegame.tools.Variables.*;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;


public class Player {
	public float x, y;
    private Texture player, player_up, player_middle, player_down;
    private float speed = 6;
    private float width, height;
    
    private TiledMapTileLayer collisionLayer;

    public Player(TiledMapTileLayer collisionLayer) {
    	this.collisionLayer = collisionLayer;
        x = VIEWPORT_WIDTH / 2;
        y = VIEWPORT_HEIGHT / 2;
       
        loadPlayerTextures();
        
        width = player_middle.getWidth(); //--------------need to /2 
        height = player_middle.getHeight(); 
        System.out.println(width + height);
    }
     
    public void update (float delta){
    	// update player movement
    	 // update player movement
    	
    	
    
        if (RIGHT_TOUCHED) {
     
            	x += speed * delta; 
            	SCROLLTRACKER_X += speed;
            	
            	//check player
            	if(!checkCollisionMap(x  , y  )) { // horizontally
            		//move player back if needed
            		System.out.println("hit right wall");
            		x -= speed * delta; 
                	SCROLLTRACKER_X -= speed;
            		
         
            	}
            //}
        }
        if (LEFT_TOUCHED) {
            if (x > 0) {
            	x -= speed * delta; 
            	SCROLLTRACKER_X -= speed;
            	
            	if(!checkCollisionMap(x ,y  )) {
            		System.out.println("hit left wall");
            		x += speed * delta; 
                	SCROLLTRACKER_X += speed;

            	}
            }
        }
        if (UP_TOUCHED) {
            if (y < VIEWPORT_HEIGHT - height) {
                y += speed * delta;
                SCROLLTRACKER_Y += speed;
                
                if(!checkCollisionMap(x  , y )) {
                	System.out.println("hit top wall");
                	y -= speed * delta;
                    SCROLLTRACKER_Y -= speed;
                	
                }
                
            }
        }
        if (DOWN_TOUCHED) {
            if (y > 0) {
                y -= speed * delta;
                SCROLLTRACKER_Y -= speed;
                
                if(!checkCollisionMap(x, y  )) {
                	System.out.println("hit bottom wall");
                	y += speed * delta;
                    SCROLLTRACKER_Y += speed;
                	
                }
                
            }
        }
        
        // set ship texture:
        if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
            player = player_up;
        } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
        	player = player_down;
        } else {
        	player = player_middle;
        }
        
        //checkCollisionMap(x,y);
        
    }
    
     
    public void render (SpriteBatch sb){
        sb.draw(player,x- (width/2),y - (height/2));
    }
     
    public void loadPlayerTextures(){
    	
        player_up = new Texture("playerRedBackCrop.png");
        player_middle = new Texture("playerRedFrontCrop.png");
        player_down = new Texture("playerRedFrontCrop.png");
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

      System.out.println("debug: " + collisionLayer.getTileWidth());
    	Cell cell = collisionLayer.getCell(
            (int) (x / collisionLayer.getTileWidth()),
            (int) (y / collisionLayer.getTileHeight()));

    	return cell != null && cell.getTile() != null
            & cell.getTile().getProperties().containsKey("isWall");
//        return false;
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
    
}
