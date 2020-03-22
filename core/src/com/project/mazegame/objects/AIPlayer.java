package com.project.mazegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Client.NetClient;
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
    private String colour;
    private AIGameClient aiGameClient;


    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID, String colour,Direction dir) {
        super(collisionLayer, name, ID, colour);
        this.colour = colour;
        this.collisionLayer = collisionLayer;
        initialPosition();
        this.dir=dir;
        aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        System.out.println("An AI player construct done..");
    }

    public void setInitialisedTime(float time) {
        this.initialisedTime = time;
    }


    public Direction getDir() {
        return this.dir;
    }

    public void setAiGameClient(AIGameClient aiGameClient) { this.aiGameClient = aiGameClient; }

    public ArrayList<AIPlayer> AITakingOver(int number) {
        ArrayList<AIPlayer> players = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            players.add(new AIPlayer(this.collisionLayer, "AI"+i, i, colour,Direction.STOP));

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

    public void update(float delta, int mode, ArrayList<Item> items, float time) {
//        aiThread.run();
        if (initialisedTime - time > 0.3 || !updateCount) {
            if (mode == 1) {
                Coordinate old = super.position;
                this.position.setX((int) x);
                this.position.setY((int) y);
                // contantsnatly throwing exeption possibly becasue not linked to player
                // will need to do something with the speed
                Coordinate moveToTake = direction(avaibleMoves(x, y));
//        System.out.println("The ai player is moving "+ moveToTake.toString());
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
        return null;
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

    private Coordinate direction(ArrayList<Coordinate> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }

        int randomTake = (int) (Math.random() * ((openDoor.size() - 1) + 1));
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
        if (attackPlayerTime - time > 0.3 || !attackPStart) {
            if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                super.isAttacking = true;
                sword = swordAttack;
                playerA.decreaseHealth(1);
                if (playerA.health == 0) {
                    this.coins += playerA.coins;
                    playerA.death();
                }
            }
        }
        this.attackPlayerTime = time;
        this.attackPStart = true;
    }

    @Override
    public void attackAI(AIPlayer playerA, float time) {
        if (attackAITime - time > 0.3 || !attackAIStart) {
            if (this.items.contains("sword") && !playerA.items.contains("shield")) {
                super.isAttacking = true;
                sword = swordAttack;
                playerA.decreaseHealth(1);
                if (playerA.health == 0) {
                    this.coins += playerA.coins;
                    playerA.death();
                }
            }
        }
        this.attackAITime = time;
        this.attackAIStart = true;
    }

    public void setDir(Direction d) {
        this.dir = d;
    }


}
