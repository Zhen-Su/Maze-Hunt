package com.project.mazegame.objects;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Messagess.MoveMessage;
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
    private String premov = "null";
    public static boolean debug = false;
    private String direct = null;

    public MultiAIPlayer(TiledMapTileLayer collisionLayer, String username, int ID, MultiPlayerGameScreen gameClient, String colour, Direction dir, PlayersType playersType) {
        super(collisionLayer, username, ID, colour, dir, playersType);
        this.gameClient = gameClient;
        this.direct="Up";
        if (debug) System.out.println("AI Multilayer construction done! ");
    }

    public MultiAIPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir, String colour, PlayersType playersType) {
        super();
        this.dir = dir;
        this.aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        this.collisionLayer = collisionLayer;
        this.name = username;
        this.colour = colour;
        this.playersType = playersType;
        co = new Collect(this);
        this.gameClient = gameClient;
        this.x = x;
        this.y = y;
        this.direct="Up";
        this.position = new Coordinate(x, y);

        loadPlayerTextures();
        createAnimations();
    }

    //=====================================Setter&Getter=============================================

    public NetClient getAiNetClient() {
        return aiNetClient;
    }

    public void setAiNetClient(NetClient aiNetClient) {
        this.aiNetClient = aiNetClient;
    }

    //==============================================================================================

    @Override
    public void update (float delta , int mode, ArrayList<Item> items, float time) {
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

                // takes random coorediante it can mvoe to
                Coordinate old = super.position;
                // contantsnatly throwing exeption possibly becasue not linked to player
                // will need to do something with the speed
                this.position.setX((int) x);
                this.position.setY((int) y);
                Coordinate moveToTake = direction(avaibleMoves(x, y));
                System.out.println("The ai player is moving "+ moveToTake.toString());
                super.x = (int) moveToTake.getX();
                super.y = (int) moveToTake.getY();

                this.change(old, moveToTake);
//                System.out.println("The direction the player is moving in is " + this.dir);


            } else if (mode == 3) {
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
            } else if (mode == 2) {
                Coordinate old = super.position;
                int tempx = x;
                int tempy = y;
                // refresh position
                this.position.setX((int) x);
                this.position.setY((int) y);
                // have previous move
                // first grab boolean to see where is possible to move
                boolean up = checkCollisionMap(x, y+movenumber);
                boolean down = checkCollisionMap(x, y-movenumber);
                boolean left = checkCollisionMap(x - movenumber, y);
                boolean right = checkCollisionMap(x + movenumber, y);
                // Then give priroy to direction
                // checks the direcion the player was preivoulsy moving in and if it can move there
                if (chosenMove(x, y, direct)) {
                    if (direct.equals("Up")) {
                        tempx = x;
                        tempy = y + movenumber;
                        this.direct = "Up";
                    } else if (direct.equals("Left")) {
                        tempx = x - movenumber;
                        tempy = y;
                        this.direct = "Left";
                    } else if (direct.equals("Right")) {
                        tempx = x + movenumber;
                        tempy = y;
                        this.direct = "Right";
                    } else if (direct.equals("Down")) {
                        tempx = x;
                        tempy = y - movenumber;
                        this.direct = "Down";
                    }
                    // this is the priory list of moves to take first lef then up the nright then down
                } else if (left) {
                    tempx = x - movenumber;
                    tempy = y;
                    this.direct = "Left";
                } else if (up) {
                    tempx = x ;
                    tempy = y + movenumber;
                    this.direct = "Up";
                } else if (right) {
                    tempx = x + movenumber;
                    tempy = y;
                    this.direct = "Right";
                } else if (down) {
                    tempx = x;
                    tempy = y - movenumber;
                    this.direct = "Down";
                }
                super.x = tempx;
                super.y = tempy;

                Coordinate newt = new Coordinate(tempx, tempy);
                System.out.println(newt.toString());
                // sets the correct direciton
                change(old, newt);

            }
            this.initialisedTime = time;
            updateCount = true;

        }
    }

    private boolean chosenMove(int xt, int yt, String direction) {
        if (direction.equals("Up")) {
            if (checkCollisionMap(xt, yt + movenumber)) {
                return true;
            } else {
                return false;
            }
        } else if (direction.equals("Down")) {
            if (checkCollisionMap(xt, yt - movenumber)) {
                return true;
            } else {
                return false;
            }
        }  else if (direction.equals("Left")) {
            if (checkCollisionMap(xt - movenumber, yt)) {
                return true;
            } else {
                return false;
            }
        } else if (direction.equals("Right")) {
            if (checkCollisionMap(xt + movenumber, yt)) {
                return true;
            } else {
                return false;
            }
        } else {
            try {
                throw new Exception("Not valid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private Item nearest(AIPlayer player, ArrayList<Item> items) {
        int px = player.position.getX();
        int py = player.position.getY();
        int shortdist = Collect.andinsEuclidian(px, items.get(0).getX(), py, items.get(0).getY());
        Item target = items.get(0);
        for (int i =0; i < items.size(); i++) {
            Item potItem = items.get(i);
            int ix = potItem.getX();
            int iy = potItem.getY();
            int newDist = Collect.andinsEuclidian(px, ix, py, iy);
            if (newDist < shortdist) {
                target = potItem;
                shortdist = newDist;
            }
        }
        return target;
    }

    private Coordinate bestMove(Coordinate target, ArrayList<Coordinate> onesToUse) {
        int besteuclid = Collect.andinsEuclidian(target.getX(), onesToUse.get(0).getX(), target.getY(), onesToUse.get(0).getY());
        Coordinate best = onesToUse.get(0);
        for (int i = 0; i < onesToUse.size(); i++) {
            int nextbest = Collect.andinsEuclidian(target.getX(), onesToUse.get(i).getX(), target.getY(), onesToUse.get(i).getY());
            if (nextbest < besteuclid) {
                besteuclid = nextbest;
                best = onesToUse.get(i);
            }

        }
        return best;
    }

    private Boolean targets(Coordinate target, Coordinate other, Coordinate compare) {
        return Math.abs(target.getX() - other.getX()) < Math.abs(target.getX() - compare.getX()) || Math.abs(target.getY() - other.getY()) < Math.abs(target.getY() - compare.getY());
    }


    private void change(Coordinate old, Coordinate update) {
        Direction oldDir = this.dir;

        if (old.getX() < update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.R;
            super.frames = walkRight;
            super.animation.setFrames(RightAnim.getFrames());
            System.out.println("R");
        } else if (old.getX() > update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.L;
            super.frames = walkLeft;
            super.animation.setFrames(LeftAnim.getFrames());
            System.out.println("L");
        } else if (old.getX() == update.getX() && old.getY() < update.getY()) {
            this.dir = Direction.U;
            super.frames = walkDown;
            super.animation.setFrames(DownAnim.getFrames());
            System.out.println("U");
        } else if (old.getX() == update.getX() && old.getY() > update.getY()) {
            this.dir = Direction.D;
            super.frames = walkUp;
            super.animation.setFrames(UpAnim.getFrames());
            System.out.println("D");
        }

//        if (dir != oldDir) {
            MoveMessage message = new MoveMessage(ID, this.position.getX(), this.position.getY(), dir);
            gameClient.getNc().send(message);
//        }
    }

    private Coordinate direction(ArrayList<Coordinate> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }

        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }

    private ArrayList<Coordinate> avaibleMoves(int x, int y) {
        int move = movenumber;
        ArrayList<Coordinate> moves = new ArrayList<>();
        if (checkCollisionMap(x + move, y)) {
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


}
