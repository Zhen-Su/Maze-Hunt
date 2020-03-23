package com.project.mazegame.objects;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.PlayerThread;
import com.project.mazegame.tools.PlayersType;
import com.project.mazegame.tools.Timer;

import java.util.ArrayList;

/**
 * This MultiAIPlayer will use
 */
public class MultiAIPlayer extends AIPlayer {

    private MultiPlayerGameScreen gameClient;
    private boolean updateCount;
    private float initialisedTime;
    private static final int movenumber = 40;
    public static boolean debug = true;

    public MultiAIPlayer(TiledMapTileLayer collisionLayer, String username, int ID, MultiPlayerGameScreen gameClient, String colour, Direction dir, PlayersType playersType) {
        super(collisionLayer,username, ID, colour,dir,playersType);
        this.gameClient=gameClient;
        if(debug) System.out.println("AI Multilayer construction done! ");
    }

    public MultiAIPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir, String colour,PlayersType playersType) {
        super();
        this.dir=dir;
        this.aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        this.collisionLayer = collisionLayer;
        this.name = username;
        this.colour = colour;
        this.playersType=playersType;
        co = new Collect(this);
        this.gameClient = gameClient;
        this.x = x;
        this.y = y;
        this.position = new Coordinate(x, y);

        loadPlayerTextures();
        createAnimations();
    }

    //=====================================Setter&Getter=============================================

    public NetClient getAiNetClient() { return aiNetClient; }

    public void setAiNetClient(NetClient aiNetClient) { this.aiNetClient = aiNetClient; }

    //==============================================================================================

    public void update(float delta, int mode, ArrayList<Item> items, float time) {
//        aiThread.run();
        // operate the delay if dead
        if (super.haveyoudied) {
            System.out.println("I have gone here " + this.getID());
            if (deathTime - time > 5) {
                haveyoudied = false;
                System.out.println(deathTime - time);
            }
        }
        if (initialisedTime - time > 0.3 || !updateCount && !haveyoudied) {
            if (mode == 1) {
                // takes random coordinate it can move to
                Coordinate old = super.position;
                // contantsnatly throwing exception possibly because not linked to player
                // will need to do something with the speed
                Coordinate moveToTake = direction(avaibleMoves(x, y));
                System.out.println("The ai player is moving " + moveToTake.toString());
                this.position.setX(moveToTake.getX());
                this.position.setY(moveToTake.getY());
                this.change(old, moveToTake);

            } else if (mode == 2) {
//                System.out.println(this);
//                System.out.println(lets);
//                System.out.println("Map items are  " +  items);
//                System.out.println("A sample item is " + items.get(0));
                // step 1 find the nearest item to the player
                Item nearestI = nearest(this, items);
                System.out.println(nearestI.toString());
                Coordinate near = new Coordinate(nearestI.getX(), nearestI.getY());
                ArrayList<Coordinate> moves = avaibleMoves(super.x, super.y);
                Coordinate bested = bestMove(near, moves);
                super.x = bested.getX();
                super.y = bested.getY();
                this.change(near, bested);
                Coordinate secondMoveToTake = direction(avaibleMoves(x, y));
                System.out.println("Move is moing here " + secondMoveToTake.toString());
//                super.x = (int) secondMoveToTake.getX();
//                super.y = (int) secondMoveToTake.getY();
                this.position.setY(secondMoveToTake.getY());
                this.position.setX(secondMoveToTake.getX());
//                this.logx = super.x;
//                this.logy = super.y;
                this.change(bested, secondMoveToTake);


                // ultimate goal is coins
            } else if (mode == 3) {
                int tempx = super.x;
                int tempy = super.y;
                Coordinate prevStore = new Coordinate(super.x, super.y);
                if (checkCollisionMap(x, this.up())) {
                    System.out.println("THe player should be doing something");

                    tempy = (int) this.up();
                    Coordinate newM = new Coordinate(super.x, super.y);
                    this.change(prevStore, newM);
                } else if (checkCollisionMap(this.left(), super.y)) {
                    tempx = (int) this.left();

                    Coordinate newM = new Coordinate(super.x, super.y);
                    this.change(prevStore, newM);
                } else if (checkCollisionMap(this.right(), super.y)) {
                    tempx = (int) this.right();

                    Coordinate newM = new Coordinate(super.x, super.y);
                    this.change(prevStore, newM);
                } else if (checkCollisionMap(super.x, this.down())) {

                    tempy = (int) this.down();
                    Coordinate newM = new Coordinate(super.x, super.y);
                }
                System.out.println("Player should be moving " + super.x + " " + super.y);
                this.position.setX(tempx);
                this.position.setY(tempy);
            }
            this.initialisedTime = time;
            updateCount = true;

        }
    }

    private float left() {
        return this.x -= movenumber;
    }

    private float right() {
        return this.x += movenumber;
    }

    private float up() {
        return this.y += movenumber;
    }

    private float down() {
        return this.y -= movenumber;
    }

    private double calcu(int px, int py, int ix, int iy) {
        double xdif = ix - px;
        double ydif = iy - py;
        return Math.sqrt(Math.pow(xdif, 2) + Math.pow(ydif, 2));
    }

    private Item nearest(MultiAIPlayer player, ArrayList<Item> items) {
        int px = player.x;
        int py = player.y;
        Item bestItem = items.get(0);
        int ix = bestItem.getX();
        int iy = bestItem.getY();
        double cShortDist = calcu(px, py, ix, iy);
        for (int i = 0; i < items.size(); i++) {
            double compDist = calcu(px, py, items.get(i).getX(), items.get(i).getY());
            if (compDist < cShortDist) {
                cShortDist = compDist;
                bestItem = items.get(i);
            }
        }
        return bestItem;
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

    private void change(Coordinate old, Coordinate update) {
        if (old.getX() < update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.R;
            super.frames = walkRight;
            super.animation.setFrames(RightAnim.getFrames());
        } else if (old.getX() > update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.L;
            super.frames = walkLeft;
            super.animation.setFrames(LeftAnim.getFrames());

        } else if (old.getX() == update.getX() && old.getY() < update.getY()) {
            this.dir = Direction.U;
            super.frames = walkDown;
            super.animation.setFrames(DownAnim.getFrames());
        } else if (old.getX() == update.getX() && old.getY() > update.getY()) {
            this.dir = Direction.D;
            super.frames = walkUp;
            super.animation.setFrames(UpAnim.getFrames());
        }

    }

    private Coordinate direction(ArrayList<Coordinate> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }
        int randomTake = (int) (Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }

    private ArrayList<Coordinate> avaibleMoves(int x, int y) {
        int move = movenumber;
        ArrayList<Coordinate> moves = new ArrayList<>();
        if (checkCollisionMap((x + move), y)) {
            moves.add(new Coordinate((x + move), y));
        }
        if (checkCollisionMap((x - move), y)) {
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


}
