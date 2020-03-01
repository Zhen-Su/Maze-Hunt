package com.project.mazegame.objects;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.project.mazegame.tools.Pair;

import java.util.ArrayList;
// will need to separate x and y from ai player
public class AIPlayer extends Player{
    protected Texture aiPlayer, player_up, player_middle, player_down, sword, shield;
    // constructor for ai player takes in same things as player so that it can use all attribues from parent
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name,  ID);
        /*
        this.loadPlayerTextures();
        this.width = player_middle.getWidth();
        this.height = player_middle.getHeight();
        */
//        loadPlayerTextures();
    }

    // method which checks all valid moves may need to be fixed
    public ArrayList<Pair> avaibleMoves(float x, float y) {
        float move = 5;
        ArrayList<Pair> moves = new ArrayList<>();
        if (checkCollisionMap((x + move), y) ){
            moves.add(new Pair((x + move), y));
        }
        if (checkCollisionMap((x -move), y)) {
            moves.add(new Pair((x - move), y));
        }
        if (checkCollisionMap(x, (y + move))) {
            moves.add(new Pair(x, (y + move)));
        }
        if (checkCollisionMap(x, (y - move))) {
            moves.add(new Pair(x, (y - move)));
        }
        return moves;
    }

    // takes in random number and applies it. if no open doors just stays in current position
    public Pair direction(ArrayList<Pair> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }

        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }
    // constantly updateing and taking new x and y
    // possibly need to add some stuff to the update method
    // could write code for layr up and donwn
    // could need to increase and modify speed
    // haven't handled scroll tracker
    @Override
    public void update(float delta) {
        while(true) {
            try {
                // contantsnatly throwing exeption possibly becasue not linked to player
                Pair moveToTake = direction(avaibleMoves(x, y));
                //   System.out.println(moveToTake.toString());
                /*
                if (x == moveToTake.getX() && y < moveToTake.getY()) {
                    SCROLLTRACKER_Y += super.speed;
                } else if (x == moveToTake.getX() && y > moveToTake.getY()) {
                    SCROLLTRACKER_Y -= super.speed;
                } else if (y == moveToTake.getY() && x < moveToTake.getX()) {
                    SCROLLTRACKER_X += super.speed;
                } else if (y == moveToTake.getY() && x > moveToTake.getX()) {
                    SCROLLTRACKER_X -= super.speed;
                }
                */
                this.x = (int)moveToTake.getX();
                this.y = (int)moveToTake.getY();
                Thread.sleep(500);
            } catch (Exception e) {
                //       System.out.println("Something gone wrong");
            }
        }
    }
    // overrides methos to make sure they are in the ai class
    @Override
    public void render (SpriteBatch sb) {super.render(sb); }
    /*
    public void render (SpriteBatch sb){
        sb.draw(aiPlayer,super.x- (width/2),super.y - (height/2));

        if(hasSword) {


            sb.draw(sword,(float)(super.x),super.y - (height/4),50,50);


        }
        if(hasShield) {


            sb.draw(shield,(float) (x- (width/1.5)),y - (height/2),50,50);



        }
        */



    @Override
    public void loadPlayerTextures() {super.loadPlayerTextures();

        player_up = new Texture("playerBlueBack.png");
        player_middle = new Texture("playerBlueFront.png");
        player_down = new Texture("playerBlueFront.png");
        sword = new Texture("sword.png");
        shield = new Texture("shield.png");
    }

    @Override
    public boolean checkCollisionMap(float possibleX, float possibleY) {return super.checkCollisionMap(possibleX, possibleY);}

    @Override
    public boolean isCellBlocked(float x, float y) {return super.isCellBlocked(x, y);}
}
