package com.project.mazegame.objects;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Messagess.DecreaseHealthMessage;
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
        this.direct = "Up";
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
        this.direct = "Up";
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
    public void update(float delta, int mode, ArrayList<Item> items, float time) {
//        aiThread.run();
        // operate the delay if dead
//        if (super.haveyoudied) {
//            System.out.println("I have gone here " + this.getID());
//            if (deathTime - time > 5) {
//                haveyoudied = false;
//                System.out.println(deathTime - time);
//            }
//        }
        removeShield();
        removeEnchantment();
        this.time.updateTimer(delta);

        if (this.isDead()) {

            if (respawnCounter == 0) respawnCounter = this.time.currentTime();

            if (this.time.currentTime() - respawnCounter == 3) this.death(time);


        } else {

            if (initialisedTime - time > 0.3 || !updateCount && !haveyoudied) {
                if (mode == 1) {

                    // takes random coorediante it can mvoe to
                    Coordinate old = super.position;
                    // contantsnatly throwing exeption possibly becasue not linked to player
                    // will need to do something with the speed
                    this.position.setX((int) x);
                    this.position.setY((int) y);
                    Coordinate moveToTake = direction(avaibleMoves(x, y));
                    System.out.println("The ai player is moving " + moveToTake.toString());
                    super.x = (int) moveToTake.getX();
                    super.y = (int) moveToTake.getY();

                    this.change(old, moveToTake);
//                System.out.println("The direction the player is moving in is " + this.dir);


                } else if (mode == 3) {
                    // this algorithm has focused movement.
                    // What it does is it looks at how many junctions there are. If there is one junction it goes back the same way
                    // if there are two junctions it picks the junction it hasn't comefrom
                    // if there are three junctions picks a random junction that it hasn't been down
                    // if there are four junctions picks random junction otu of the ones it hasn't been dwon
                    Coordinate old = super.getPosition();

                    int tempx = x;
                    int tempy = y;
                    this.position.setX(x);
                    this.position.setY(y);

                    ArrayList<Coordinate> junctions = avaibleMoves(x, y);
                    System.out.println(junctions);
                    int randSize = junctions.size() - 1;

                    if (junctions.size() == 1) {
                        System.out.println("Number of juntions " + 1);
                        tempx = junctions.get(0).getX();
                        tempy = junctions.get(0).getY();
//                    this.preve = junctions.get(0);
                    } else if (junctions.size() == 2) {
                        if (preve != null && lastp != null) {
//                        System.out.println("Prev is not null");
                            System.out.println("Number of juntions " + 2);
                            System.out.println("List before "  + junctions);
                            ArrayList<Coordinate> rem = customRemove(lastp, junctions);
                            System.out.println("Preve is " + lastp.toString());

                            System.out.println("List after " + rem);
                            int index = (int) (Math.random() * (((rem.size() - 1) - 0) + 1)) + 0;
                            tempx = rem.get(index).getX();
                            tempy = rem.get(index).getY();
//                        this.preve = junctions.get(0);
                        } else {

                            int index = (int) (Math.random() * ((randSize - 0) + 1)) + 0;
                            tempx = junctions.get(index).getX();
                            tempy = junctions.get(index).getY();
//                        preve = junctions.get(index);
                        }
                    } else if (junctions.size() == 3) {
                        if (preve != null && lastp != null) {
//                        System.out.println("Prev is not null");
                            System.out.println("Number of juntions " + 3);
                            System.out.println("List before "  + junctions);
                            System.out.println("Preve is " + lastp.toString());
                            ArrayList<Coordinate> rem = customRemove(lastp, junctions);
                            System.out.println("List after " + rem);
                            int index = (int) (Math.random() * (((rem.size() - 1) - 0) + 1)) + 0;
                            tempx = rem.get(index).getX();
                            tempy = rem.get(index).getY();
//                        preve = junctions.get(index);

                        } else {
                            int index = (int) (Math.random() * ((randSize - 0) + 1)) + 0;
                            tempx = junctions.get(index).getX();
                            tempy = junctions.get(index).getY();
//                        preve = junctions.get(index);
                        }

                    } else if (junctions.size() == 4) {
                        if (preve != null && lastp != null) {
                            System.out.println("Prev is not null");
                            System.out.println("Number of juntions " + 4);
                            System.out.println("Preve is " + lastp.toString());
                            System.out.println("List before "  + junctions);
                            ArrayList<Coordinate> rem = customRemove(lastp, junctions);
                            System.out.println("List after " + rem);
                            int index = (int) (Math.random() * (((rem.size() - 1) - 0) + 1)) + 0;
                            tempx = rem.get(index).getX();
                            tempy = rem.get(index).getY();
//                        preve = junctions.get(index);

                        } else {
                            int index = (int) (Math.random() * ((randSize - 0) + 1)) + 0;
                            tempx = junctions.get(index).getX();
                            tempy = junctions.get(index).getY();
//                        preve = junctions.get(index);
                        }
                    }
                    Coordinate nextMove = new Coordinate(tempx, tempy);
                    System.out.println(nextMove.toString());
                    super.x = tempx;
                    super.y = tempy;
                    this.change(old, nextMove);
                    this.lastp = this.preve;
                    this.preve = nextMove;

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
                    boolean up = checkCollisionMap(x, y + movenumber);
                    boolean down = checkCollisionMap(x, y - movenumber);
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
                        tempx = x;
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
//                    System.out.println(newt.toString());
                    // sets the correct direciton
                    change(old, newt);

                }
                this.initialisedTime = time;
                updateCount = true;

            }

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
        } else if (direction.equals("Left")) {
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
        for (int i = 0; i < items.size(); i++) {
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

    private ArrayList<Coordinate> customRemove(Coordinate rem, ArrayList<Coordinate> list) {
        ArrayList<Coordinate> outputlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!rem.same(list.get(i))) {
                outputlist.add(list.get(i));
            }
        }
        return outputlist;
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
//            System.out.println("R");
        } else if (old.getX() > update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.L;
            super.frames = walkLeft;
            super.animation.setFrames(LeftAnim.getFrames());
//            System.out.println("L");
        } else if (old.getX() == update.getX() && old.getY() < update.getY()) {
            this.dir = Direction.U;
            super.frames = walkDown;
            super.animation.setFrames(DownAnim.getFrames());
//            System.out.println("U");
        } else if (old.getX() == update.getX() && old.getY() > update.getY()) {
            this.dir = Direction.D;
            super.frames = walkUp;
            super.animation.setFrames(UpAnim.getFrames());
//            System.out.println("D");
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

        int randomTake = (int) (Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }

    private ArrayList<Coordinate> avaibleMoves(int x, int y) {
        int move = movenumber;
        ArrayList<Coordinate> moves = new ArrayList<>();
        if (checkCollisionMap(x + move, y)) {
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

    @Override
    public void attackP(Player playerA, float time) {
        System.out.println("I am executing");
        // only difference with this and the player methods is doens't need space to be pressed
        if (attackPlayerTime - time > 0.3 || !attackPStart) {
            if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                super.isAttacking = true;
                sword = swordAttack;
                attack();
                playerA.decreaseHealth(1 + super.getGearCount());

                int numOfDecrease = 1 + getGearCount();
                playerA.decreaseHealth(numOfDecrease);

                DecreaseHealthMessage decreaseHealthMessage = new DecreaseHealthMessage(ID, playerA.ID, numOfDecrease);
                this.gameClient.getNc().send(decreaseHealthMessage);

                if (playerA.health == 0) {
                    this.coins += playerA.coins;
                }
            }
        }
        this.attackPlayerTime = time;
        this.attackPStart = true;
    }
    @Override
    // same as above method just attacking another ai instead
    public AIPlayer attackAI(AIPlayer playerA, float time) {
        System.out.println("I am executing");
        if (attackAITime - time > 0.3 || !attackAIStart) {
            if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                System.out.println("Has gone here");
                super.isAttacking = true;
                sword = swordAttack;
                attack();
                playerA.decreaseHealth(1 + super.getGearCount());

                int numOfDecrease = 1 + getGearCount();
                playerA.decreaseHealth(1 + getGearCount());
                DecreaseHealthMessage decreaseHealthMessage = new DecreaseHealthMessage(ID, playerA.ID, numOfDecrease);
                this.gameClient.getNc().send(decreaseHealthMessage);

                if (playerA.health == 0) {
                    this.coins += playerA.coins;
                    return playerA;
                }
            }
        }
        this.attackAITime = time;
        this.attackAIStart = true;
        return playerA;
    }


}
