package com.project.mazegame.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.Pair;
import com.project.mazegame.tools.PlayerThread;
import com.project.mazegame.tools.PlayersType;

import java.util.ArrayList;
import java.util.Collections;

// each time ai moves needs to send message

// In constructor can literally just call AITakingOver and will genearate however many AIs wanted
public class AIPlayer extends Player {

    protected Thread aiThread;
    protected float initialisedTime;
    protected boolean updateCount;
    protected float attackPlayerTime;
    protected float attackAITime;
    protected boolean attackPStart;
    protected boolean attackAIStart;
    protected NetClient aiNetClient;
    protected static final int movenumber = 40;
    protected ArrayList<Coordinate> visited;
    protected String premov = "null";

    public AIPlayer(){super();}
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID, String colour, Direction dir, PlayersType playersType) {
        super(collisionLayer, name, ID, colour,playersType);
        this.dir=dir;
        this.aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        this.visited = new ArrayList<>();
        this.visited.add(super.position);
    }

    //=====================================Setter&Getter=============================================

    public void setInitialisedTime(float time) {
        this.initialisedTime = time;
    }

    public Direction getDir() {
        return this.dir;
    }

    public NetClient getAiNetClient() { return aiNetClient; }

    public void setAiNetClient(NetClient aiNetClient) { this.aiNetClient = aiNetClient; }

    //==============================================================================================

    public ArrayList<AIPlayer> AITakingOver(int number) {
        ArrayList<AIPlayer> players = new ArrayList<>();

            for (int i = 0; i < number; i++) {
                players.add(new AIPlayer(this.collisionLayer, "AI" + i, i, colour, Direction.STOP,PlayersType.single));
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
                // takes random coordinate it can move to
                Coordinate old = super.position;
                // contantsnatly throwing exception possibly because not linked to player
                // will need to do something with the speed
                this.position.setX((int) x);
                this.position.setY((int) y);
                Coordinate moveToTake = direction(avaibleMoves(x, y));
                System.out.println("The ai player is moving "+ moveToTake.toString());
                super.x = moveToTake.getX();
                super.y = moveToTake.getY();

                this.change(old, moveToTake);

            } else if (mode == 2) {
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

                if (checkCollisionMap(x, this.up()) && !premov.equals("Up")) {
                    System.out.println("THe player should be doing something");

                    tempy = (int) this.up();
                    Coordinate newM = new Coordinate(super.x, super.y);
                    premov = "Up";
                    this.change(prevStore, newM);
                } else if (checkCollisionMap(this.left(), y) && !premov.equals("Left")) {
                    tempx = (int) this.left();

                    Coordinate newM = new Coordinate(super.x, super.y);
                    this.change(prevStore, newM);
                    premov = "Left";
                } else if (checkCollisionMap(this.right(), y) && !premov.equals("Right")) {
                    tempx = (int) this.right();

                    Coordinate newM = new Coordinate(super.x, super.y);
                    this.change(prevStore, newM);
                    premov = "Right";
                }else if (checkCollisionMap(x, this.down()) && !premov.equals("Down")) {

                    tempy = (int) this.down();
                    Coordinate newM = new Coordinate(super.x, super.y);
                    premov = "Down";
                }
                System.out.println("Player should be moving " + tempx + " " + tempy);
                this.position.setX(tempx);
                this.position.setY(tempy);


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
//        System.out.println(this.dir);

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