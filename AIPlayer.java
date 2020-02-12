package com.project.mazegame.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

public class AIPlayer extends Player{
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name, ID);
//        super.coins = 5;

    }
    public ArrayList<Pair> avaibleMoves(float x, float y) {
        ArrayList<Pair> moves = new ArrayList<>();
        if (checkCollisionMap((x + 100), y) ){
            moves.add(new Pair((x + 100), y));
        }
        if (checkCollisionMap((x -100), y)) {
            moves.add(new Pair((x - 100), y));
        }
        if (checkCollisionMap(x, (y + 100))) {
            moves.add(new Pair(x, (y + 100)));
        }
        if (checkCollisionMap(x, (y - 100))) {
            moves.add(new Pair(x, (y - 100)));
        }
        return moves;
    }
    public Pair direction(ArrayList<Pair> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }
        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(openDoor.indexOf(randomTake));
    }
    public void runPlayer() {
        while(true) {
            Pair moveToTake = direction(avaibleMoves(super.x, super.y));
            super.x = moveToTake.getX();
            super.y = moveToTake.getY();
        }
    }

}
