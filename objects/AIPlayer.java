package com.project.mazegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.PlayerThread;

import java.util.ArrayList;

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
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name = "Super AI", ID);
        this.collisionLayer = collisionLayer;
        initialPosition();
        aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
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
                this.position.setX((int) x);
                this.position.setY((int) y);
                // contantsnatly throwing exeption possibly becasue not linked to player
                // will need to do something with the speed
                Coordinate moveToTake = direction(avaibleMoves(x, y));
        System.out.println("The ai player is moving "+ moveToTake.toString());
                super.x = (int) moveToTake.getX();
                super.y = (int) moveToTake.getY();
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
                super.x = (int) secondMoveToTake.getX();
                super.y = (int) secondMoveToTake.getY();
                this.change(bested, secondMoveToTake);


                // ultimate goal is coins
            } else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
//            aiThread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.initialisedTime = time;
            updateCount = true;
        }
    }
    private double calcu(int px, int py, int ix, int iy) {
        double xdif = ix - px;
        double ydif = iy - py;
        return Math.sqrt(Math.pow(xdif, 2) + Math.pow(ydif, 2));
    }
    private Item nearest(AIPlayer player, ArrayList<Item> items) {
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
