package com.project.mazegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.screens.GameScreen;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.PlayerThread;
import com.project.mazegame.tools.Pair;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jdk.nashorn.internal.objects.annotations.Where;
import sun.awt.X11.XPropertyEvent;

// each time ai moves needs to send message

// In constructor can litterally just call AITakingOver and will genearate however many AIs wanted
public class AIPlayer extends Player {
    public ArrayList<AIPlayer> ais;
    private final int spawnNumber = 0;
    private Collect co;
    protected Direction dir;
    private TiledMapTileLayer collisionLayer;
    private Thread aiThread;
    private float initialisedTime;
    private boolean updateCount;
    private float attackPlayerTime;
    private float attackAITime;
    private boolean attackPStart;
    private boolean attackAIStart;
    private ArrayList<Coordinate> visited;
    private int trackindex;
    private static final int movenumber = 30;
    private String premov;
    public int logx;
    public int logy;
    private int olditemcount;
    private String direct = null;
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name = "Super AI", ID);
//        super.loadPlayerTextures();
        this.collisionLayer = collisionLayer;
        initialPosition();
        aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        this.visited = new ArrayList<>();
        this.visited.add(super.position);
        this.trackindex = 0;
        this.olditemcount = 0;
        this.direct = "Up";
//        aiThread.start();
//        this.ais = AITakingOver(numberOfThem, collisionLayer, co);

    }
    public void setInitialisedTime(float time) {this.initialisedTime = time;}

    public AIPlayer() {super();}
    public Direction getDir() {
        return this.dir;
    }
    public ArrayList<AIPlayer> AITakingOver(int number) {
        ArrayList<AIPlayer> players = new ArrayList<>();



        for (int i = 0; i < number; i++) {
            if (i == 0) {
                players.add(new AIPlayer(this.collisionLayer, "AI0", 000));
            } else {
                AIPlayer prev = players.get(i-1);
//                String newName = incrementString(prev.getName());
                int newID1 = prev.getID();
                int newID2 = newID1++;
//                System.out.println("A new ai is created");
                players.add(new AIPlayer(this.collisionLayer, "albert", newID2));

            }
        }
        return players;
    }
    private static String incrementString(String currentString) {
//        System.out.println("Still works");
//        System.out.println(currentString);
        String extractAI = currentString.substring(0, 2);
        if (!extractAI.equals("AI")) {
            try {
                throw new Exception("Not valid name start");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String extractNumber = currentString.substring(2);
            int extracted = Integer.parseInt(extractNumber);
            extracted++;
            return "AI" + String.valueOf(extracted);
        }
        return null;
    }
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
                // refresh posion
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

    private Coordinate bestMoveTrace(ArrayList<Coordinate> listgood) {
        Coordinate returno = null;
        for (int i = 0; i < listgood.size(); i++) {
            if (!visited.contains(listgood.get(i))) {
                return listgood.get(i);
            }
        }
        return returno;
    }
    // just a string checking method
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

    private double calcu(int px, int py, int ix, int iy) {
        double xdif = ix - px;
        double ydif = iy - py;
        return Math.sqrt(Math.pow(xdif, 2) + Math.pow(ydif, 2));
    }
    private static boolean containsCo(Coordinate look, ArrayList<Coordinate> list) {
        if (list.isEmpty()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Coordinate.same(look, list.get(i))) {
                return true;
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

    private ArrayList<Coordinate> sortForBest(Coordinate target, ArrayList<Coordinate> potMove) {
        // work the eculidians
        ArrayList<Integer> euclids = new ArrayList<>();
        for (int i = 0; i < potMove.size(); i++) {
            euclids.add(Collect.andinsEuclidian(target.getX(), potMove.get(i).getX(), target.getY(), potMove.get(i).getY()));
        }
       ArrayList<Pair> pairs = new ArrayList<>();
       for (int i = 0; i < euclids.size(); i++) {
           pairs.add(new Pair(i, 0));
       }
       ArrayList<Integer> before = new ArrayList<>();
       for (int i = 0; i < euclids.size(); i++) {
           before.add(euclids.get(i));
       }
       Collections.sort(euclids);
       for (int i = 0; i < before.size(); i++) {
           int beforelook = before.get(i);

           for (int j = 0; j < euclids.size(); j++) {
               if (beforelook == euclids.get(j) && pairs.get(i).getY() == 0) {
                   pairs.get(i).setY(j);
               }
           }
       }
       ArrayList<Coordinate> outputs = new ArrayList<>();

       for (int i = 0; i < pairs.size(); i++) {
           outputs.add(null);
       }
       for (int i = 0; i < pairs.size(); i++) {
           Pair examine = pairs.get(i);
           Coordinate tobeadded = potMove.get((int)examine.getX());
           Coordinate rem = outputs.remove((int) examine.getY());
           outputs.add((int) examine.getY(), tobeadded);
       }

        return outputs;


    }
    private void change(Coordinate old, Coordinate update) {
        if (old.getX() < update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.R;
//            System.out.println(this.dir);
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
//        System.out.println("Direction not changed");
        System.out.println(this.dir);

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

    private Coordinate direction(ArrayList<Coordinate> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }

        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
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

    private float left() {return this.x -= movenumber;}
    private float right() {return this.x += movenumber;}
    private float up() {return this.y += movenumber;}
    private float down() {return this.y -= movenumber;}
    @Override
    public void attackP(Player playerA, float time) {
        System.out.println("I am executing");
        // only difference with this and the player methods is doens't need space to be pressed
        if (attackPlayerTime - time > 0.3 || !attackPStart) {
            if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                System.out.println("Going here");
                super.isAttacking = true;
                sword = swordAttack;
                playerA.decreaseHealth(1 + super.getGearCount());
                if (playerA.health == 0) {
                    this.coins += playerA.coins;
                    playerA.death(time);
                }
            }
        }
        this.attackPlayerTime = time;
        this.attackPStart = true;
    }
    @Override
    // same as bove method jsut attackign another ai instead
    public AIPlayer attackAI(AIPlayer playerA, float time) {
        System.out.println("I am executing");
        if (attackAITime - time > 0.3 || !attackAIStart) {
            if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                System.out.println("Has gone here");
                super.isAttacking = true;
                sword = swordAttack;
                playerA.decreaseHealth(1 + super.getGearCount());
                if (playerA.health == 0) {
                    this.coins += playerA.coins;
                    playerA.death(time);
                    return playerA;
                }
            }
        }
        this.attackAITime = time;
        this.attackAIStart = true;
        return playerA;
    }

    public void setDir(Direction d) {
        this.dir = d;
    }


}
