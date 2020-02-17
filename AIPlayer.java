package com.project.mazegame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import java.util.ArrayList;

public class AIPlayer extends Player{
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name, ID);
//        super.coins = 5;

    }
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
    public Pair direction(ArrayList<Pair> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }
        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }

    @Override
    public void update(float delta) {
        while(true) {
            Pair moveToTake = direction(avaibleMoves(x, y));
            x = moveToTake.getX();
            y = moveToTake.getY();

        }
    }

    @Override
    public void render (SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void loadPlayerTextures() {
        super.loadPlayerTextures();
    }
    @Override
    public boolean checkCollisionMap(float possibleX, float possibleY) {
        return super.checkCollisionMap(possibleX, possibleY);
    }

    @Override
    public boolean isCellBlocked(float x, float y) {return super.isCellBlocked(x, y);}
    @Override
    public boolean IsCellCoin(float x, float y) {return super.IsCellCoin(x, y);}
}
