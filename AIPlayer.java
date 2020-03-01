package com.project.mazegame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;

import java.util.ArrayList;

public class AIPlayer extends Player {

    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name, ID);
    }

    @Override
    public void update (float delta , int mode, Collect lets) {
        int sleep = 100;
        if (mode == 1) {
            Coordinate old = super.position;
        this.position.setX((int) x);
        this.position.setY((int) y);
        // contantsnatly throwing exeption possibly becasue not linked to player
        // will need to do something with the speed
        Coordinate moveToTake = direction(avaibleMoves(x, y));
        System.out.println(moveToTake.toString());
        super.x = (int) moveToTake.getX();
        super.y = (int) moveToTake.getY();
        super.player = change(old, moveToTake);
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } else if (mode == 2) {
            Item nearest = lets.nearestItem(this);
            Coordinate near = new Coordinate(nearest.x, nearest.y);
            ArrayList<Coordinate> moves = avaibleMoves(super.x, super.y);
            Coordinate bested = bestMove(near, moves);
            super.x = bested.getX();
            super.y = bested.getY();
            super.player = change(near, bested);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private Texture change(Coordinate old, Coordinate update) {
        if (old.getX() < update.getX() && old.getY() == update.getY()) {
            return player_right;
        } else if (old.getX() > update.getX() && old.getY() == update.getY()) {
            return player_left;
        } else if (old.getX() == update.getX() && old.getY() < update.getY()) {
            return player_up;
        } else if (old.getX() == update.getX() && old.getY() > update.getY()) {
            return player_down;
        } else {
            return player;
        }

    }
    private ArrayList<Coordinate> avaibleMoves(int x, int y) {
        int move = 40;
        ArrayList<Coordinate> moves = new ArrayList<>();
        if (checkCollisionMap((x + move), y) ){
            moves.add(new Coordinate((x + move), y));
        }
        if (checkCollisionMap((x -move), y)) {
            moves.add(new Coordinate((x - move), y));
        }
        if (checkCollisionMap(x, (y + move))) {
            moves.add(new Coordinate(x, (y + move)));
        }
        if (checkCollisionMap(x, (y - move))) {
            moves.add(new Coordinate(x, (y - move)));
        }
        return moves;
    }

    private Coordinate direction(ArrayList<Coordinate> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }

        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }
    private Coordinate bestMove(Coordinate target, ArrayList<Coordinate> onesToUse) {
        Coordinate best = onesToUse.get(0);
        for (int i = 0; i < onesToUse.size(); i++) {
            if (targets(target, onesToUse.get(i), best)) {
                best = onesToUse.get(i);
            }
        }
        return best;
    }
    private Boolean targets(Coordinate target, Coordinate other, Coordinate compare) {
        return Math.abs(target.getX() - other.getX()) < Math.abs(target.getX() - compare.getX()) || Math.abs(target.getY() - other.getY()) < Math.abs(target.getY() - compare.getY());


    }
}
