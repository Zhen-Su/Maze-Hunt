package com.project.mazegame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.PlayerThread;
import com.project.mazegame.tools.Pair;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
    private static final int movenumber = 40;
    public int logx;
    public int logy;
    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID) {
        super(collisionLayer, name = "Super AI", ID);
        super.loadPlayerTextures();
        this.collisionLayer = collisionLayer;
        initialPosition();
        aiThread = new Thread(new PlayerThread());
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        this.visited = new ArrayList<>();
        this.visited.add(super.position);
        this.trackindex = 0;
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
                System.out.println("The direction the player is moving in is " + this.dir);


            } else if (mode == 2) {
                Coordinate newMove = null;
                int prevx = position.getX();
                int prevy = position.getY();
                Coordinate bested = new Coordinate(prevx, prevy);
//                System.out.println(this);
//                System.out.println(lets);
//                System.out.println("Map items are  " +  items);
//                System.out.println("A sample item is " + items.get(0));
                // step 1 find the nearest item to the player
                Item nearestI = nearest(this, items);
                ArrayList<Coordinate> availableMoves = avaibleMoves(this.getPosition().getX(), this.getPosition().getX());
                ArrayList<Coordinate> bestMoves = sortForBest(nearestI.getPosition(), availableMoves);
                if (visited.contains(bestMoves.get(bestMoves.size() - 1))) {
                    this.position = visited.get(bestMoves.size() - 1);
                    newMove = this.position;
                } else {
                    Coordinate move = bestMoveTrace(bestMoves);
                    if (move != null) {
                        this.position = move;
                        newMove = this.position;

                    } else {
                        Random random = new Random();
                        int randomIndex = random.nextInt(bestMoves.size() - 1);
                        this.position = bestMoves.get(randomIndex);
                        newMove = this.position;
                    }
                }
//                this.position.setY(secondMoveToTake.getY());
//                this.position.setX(secondMoveToTake.getX());
//                this.logx = super.x;
//                this.logy = super.y;
//                newMove = this.position;
                System.out.println(bested);
                System.out.println(newMove);

                this.change(bested, newMove);


                // ultimate goal is coins
            } else if (mode == 3) {
                int tempx = super.x;
                int tempy = super.y;
                Coordinate prevStore = new Coordinate(super.x, super.y);

                int lefX = (int) this.left();
                int upY = (int) this.up();
                int riX = (int) this.right();
                int doY = (int) this.down();
                Coordinate upC = new Coordinate(x, upY);
                Coordinate lC = new Coordinate(lefX, y);
                Coordinate rC = new Coordinate(riX, y);
                Coordinate doC = new Coordinate(x, doY);
                System.out.println("Is left available " + checkCollisionMap(lC.getX(), lC.getY()));
                System.out.println("Is right available " + checkCollisionMap(rC.getX(), rC.getY()));
                System.out.println("Is up available " + checkCollisionMap(upC.getX(), upC.getY()));
                System.out.println("Is down available " + checkCollisionMap(doC.getX(), doC.getY()));
                if (checkCollisionMap(x, upY) && !containsCo(upC, this.visited)) {
                    tempx = upC.getX();
                    tempy = upC.getY();
                    this.visited.add(upC);

                    this.trackindex++;
                    System.out.println("Up and Index is going up " + this.trackindex);

                } else if (checkCollisionMap(lefX, y) && !containsCo(lC, this.visited)) {
                    tempx = lC.getX();
                    tempy = lC.getY();
                    this.visited.add(lC);
                    this.trackindex++;
                    System.out.println("Lef and Index is going up " + this.trackindex);
                } else if (checkCollisionMap(riX, y) && !containsCo(rC, this.visited)) {
                    tempx = rC.getX();
                    tempy = rC.getY();
                    this.visited.add(rC);
                    this.trackindex++;
                    System.out.println("Right and Index is going up " + this.trackindex);

                } else if (checkCollisionMap(x, doY) && !containsCo(doC, this.visited)) {
                    tempx = rC.getX();
                    tempy = rC.getY();
                    this.visited.add(doC);
                    this.trackindex++;
                    System.out.println("Down and Index is going up " + this.trackindex);
                } else {
                    this.trackindex--;


                    Coordinate move = this.visited.get(trackindex);
                    tempx = move.getX();
                    tempy = move.getY();

//                    int indexThrough = this.trackindex -= 1;
//                    tempx =  this.visited.get(indexThrough).getX();
//                    tempy = this.visited.get(indexThrough).getY();

                    System.out.println("Index is going down " + this.trackindex);

                }
                Coordinate newAnim = new Coordinate(tempx, tempy);
                this.change(prevStore, newAnim);
                System.out.println("Moving here " + newAnim);
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
            System.out.println(this.dir);
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
