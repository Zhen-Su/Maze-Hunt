package com.project.mazegame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import java.util.ArrayList;

public class AIPlayer extends Player{
    // constructor for ai player takes in same things as player so that it can use all attribues from parent
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name, ID);
    }
    // method which checks all valid moves may need to be fixed
    public ArrayList<Pair> avaibleMoves(float x, float y) {
        int move = 1;
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
    @Override
    public void update(float delta) {
        while(true) {
            Pair moveToTake = direction(avaibleMoves(x, y));
            System.out.println(moveToTake.toString());
            x = moveToTake.getX();
            y = moveToTake.getY();
        }
    }
    // overrides methos to make sure they are in the ai class
    @Override
    public void render (SpriteBatch sb) {super.render(sb); }

    @Override
    public void loadPlayerTextures() {super.loadPlayerTextures();}

    @Override
    public boolean checkCollisionMap(float possibleX, float possibleY) {return super.checkCollisionMap(possibleX, possibleY);}

    @Override
    public boolean isCellBlocked(float x, float y) {return super.isCellBlocked(x, y);}

    @Override
    public boolean IsCellCoin(float x, float y) {return super.IsCellCoin(x, y);}
}
