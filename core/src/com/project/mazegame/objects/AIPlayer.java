package com.project.mazegame.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.PlayersType;
import java.util.ArrayList;



// each time ai moves needs to send message

// In constructor can litterally just call AITakingOver and will genearate however many AIs wanted

/**
 * <h1> AI Player</h1>
 * Class is ai for game handles any ai involved. It extends the player class and overrides the update method
 *
 * @Author James Bartlett
 *
 */
public class AIPlayer extends Player {

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
    protected String direct = null;
    protected Coordinate lastp;
    protected Coordinate preve;

    public AIPlayer(){
        super();
    }

    public AIPlayer(TiledMapTileLayer collisionLayer, String name, int ID, String colour, Direction dir, PlayersType playersType) {
        super(collisionLayer, name, ID, colour,playersType);
        this.dir=dir;
        this.updateCount = false;
        this.attackAIStart = false;
        this.attackPStart = false;
        this.visited = new ArrayList<>();
        this.visited.add(super.position);
        this.direct = "Up";
        this.preve = null;
        this.lastp = null;
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

    /**
     * <h1>Update</h1>
     * Overrides the player update method to move the player based on the mode
     * mode 1 is a random movment selects a random space next to it and goes to that space
     * mode 2 is a only return if have to where moves in a straight line priroritising left, up, right down
     * mode 3 is random mouse which looks at the junctions availabel and picks a junction to travel to but alwasy makes sure not to back track
     *
     * @param delta the detla from the gamescreen
     * @param mode the mode for the player
     * @param time The time to control movment
     */
    @Override
    public void update (float delta , int mode, float time) {

//        // operate the delay if dead
//        if (super.haveyoudied) {
//            System.out.println("I have gone here " + this.getID());
//            if (deathTime - time > 5) {
//                haveyoudied = false;
//            }
//        }
        if (this.isDead()) {
            if (this.respawnCounter == 0) this.respawnCounter = this.time.currentTime();

            if (this.time.currentTime() - this.respawnCounter == 3) this.death(time);
        } else {

            if (initialisedTime - time > 0.2f || !updateCount && !haveyoudied) {
                if (mode == 1) {

                    // takes random coorediante it can mvoe to
                    Coordinate old = new Coordinate(position.getX(), position.getY());
                    // contantsnatly throwing exeption possibly becasue not linked to player
                    // will need to do something with the speed
                    this.position.setX((int) this.x);
                    this.position.setY((int) this.y);
                    Coordinate moveToTake = direction(avaibleMoves(this.x, this.y));
                    this.x = (int) moveToTake.getX();
                    this.y = (int) moveToTake.getY();

                    super.moveTo = moveToTake;
                    this.change(old, moveToTake);

                } else if (mode == 3) {
                    // this algorithm has focused movemnt.
                    // What it does is it looks at how many junctions there are. If there is one junction it goes back the same way
                    // if there are two junctions it picks the junction it hasn't comefrom
                    // if there are three junctions pciks a random junction that it hasn't been down
                    // if there are four junctions picks random junction otu of the ones it hasn't been dwon
                    Coordinate old = new Coordinate(position.getX(), position.getY());

                    int tempx = x;
                    int tempy = y;
                    this.position.setX(x);
                    this.position.setY(y);

                    ArrayList<Coordinate> junctions = avaibleMoves(x, y);
                    System.out.println(junctions);
                    int randSize = junctions.size() - 1;

                    if (junctions.size() == 1) {
                        tempx = junctions.get(0).getX();
                        tempy = junctions.get(0).getY();
                    } else if (junctions.size() == 2) {
                        if (preve != null && lastp != null) {
                            ArrayList<Coordinate> rem = customRemove(lastp, junctions);
                            int index = (int) (Math.random() * (((rem.size() - 1) - 0) + 1)) + 0;
                            tempx = rem.get(index).getX();
                            tempy = rem.get(index).getY();
                        } else {

                            int index = (int) (Math.random() * ((randSize - 0) + 1)) + 0;
                            tempx = junctions.get(index).getX();
                            tempy = junctions.get(index).getY();
                        }
                    } else if (junctions.size() == 3) {
                        if (preve != null && lastp != null) {
                            ArrayList<Coordinate> rem = customRemove(lastp, junctions);
                            int index = (int) (Math.random() * (((rem.size() - 1) - 0) + 1)) + 0;
                            tempx = rem.get(index).getX();
                            tempy = rem.get(index).getY();

                        } else {
                            int index = (int) (Math.random() * ((randSize - 0) + 1)) + 0;
                            tempx = junctions.get(index).getX();
                            tempy = junctions.get(index).getY();
                        }

                    } else if (junctions.size() == 4) {
                        if (preve != null && lastp != null) {
                            ArrayList<Coordinate> rem = customRemove(lastp, junctions);
                            int index = (int) (Math.random() * (((rem.size() - 1) - 0) + 1)) + 0;
                            tempx = rem.get(index).getX();
                            tempy = rem.get(index).getY();

                        } else {
                            int index = (int) (Math.random() * ((randSize - 0) + 1)) + 0;
                            tempx = junctions.get(index).getX();
                            tempy = junctions.get(index).getY();

                        }
                    }
                    Coordinate nextMove = new Coordinate(tempx, tempy);
                    System.out.println(nextMove.toString());
                    this.x = tempx;
                    this.y = tempy;
                    this.moveTo = nextMove;
                    this.change(old, nextMove);
                    this.lastp = this.preve;
                    this.preve = nextMove;

                } else if (mode == 2) {
                    Coordinate old = new Coordinate(position.getX(), position.getY());
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
                    this.x = tempx;
                    this.y = tempy;

                    Coordinate next = new Coordinate(tempx, tempy);
                    System.out.println(next.toString());
                    // sets the correct direciton
                    this.change(old, next);
                    this.moveTo = next;

                }
                this.initialisedTime = time;
                updateCount = true;

            }
        }
    }



    /**
     * <h1> Custom remove</h1>
     * <p>
     *     Normally with the array list remove method it remvoes things however it looks for an object match
     *     With the cooridnate looking to be removed it is not the same object so wrote custom remove method to remove coordinates from the list
     * </p>
     * @param rem coordinate to remove
     * @param list list of coordinates to remove the coordinate from
     * @return output list
     */
    private ArrayList<Coordinate> customRemove(Coordinate rem, ArrayList<Coordinate> list) {
        ArrayList<Coordinate> outputlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!rem.same(list.get(i))) {
                outputlist.add(list.get(i));
            }
        }
        return outputlist;
    }

    /**
     * <h1>chosenMove</h1>
     * Checks if the player can move in the chosen direction
     * @param xt x value which is being looked at
     * @param yt y value which is being looked at
     * @param direction direction to go in
     * @return boolean depending on whether the x and y are available to move to
     */
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

    /**
     * <h> Change</h>
     * Chagnges the textture of the player based on where it moves
     * @param old position
     * @param update new positon
     *
     */
    private void change(Coordinate old, Coordinate update) {
        if (old.getX() < update.getX() && old.getY() == update.getY()) {
            this.dir = Direction.R;
//            System.out.println(this.dir);
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
//        System.out.println("Direction not changed");
//        System.out.println(this.dir);

        System.out.println(this.dir);

    }

    /**
     * Available moves
     * Method to work out the surrounding places which arent moves
     * @param x current x coordinate of player
     * @param y current y coordinate of player
     * @return avaiable list of moves for player
     */
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

    /**
     * direction
     * based on list of moves picks a random one
     * @param openDoor moves availabe
     * @return place to move to
     */
    private Coordinate direction(ArrayList<Coordinate> openDoor) {
        if (openDoor.size() <= 0) {
            return null;
        }

        int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
        return openDoor.get(randomTake);
    }

    /**
     * attack
     * given a player it attacks them
     * @param playerA the player being attacked
     * @param time the time for attack count
     */
    @Override
    public void attackP(Player playerA, float time) {

        // only difference with this and the player methods is doens't need space to be pressed

        if (this.items.contains("sword") && !playerA.items.contains("shield")) {
            if (super.attackcount <= 8) {
                super.attackcount++;
            } else {
                super.isAttacking = true;
                sword = swordAttack;
                playerA.decreaseHealth(1 + super.getGearCount());
                if (playerA.health == 0) {
                    this.coins += playerA.coins;
//                        playerA.death(time);
                    playerA.haveyoudied = true;
                }
                super.attackcount = 0;
            }
        }

        this.attackPlayerTime = time;
        this.attackPStart = true;
    }

    /**
     * attackAI
     * attacks ai player same as other attack method
     * @param playerA playe to be attacked
     * @param time to manage no spammning
     * @return ai player who is being attacked
     */
    @Override
    // same as bove method jsut attackign another ai instead
    public AIPlayer attackAI(AIPlayer playerA, float time) {
        System.out.println("I am executing");

        if (this.items.contains("sword") && !playerA.items.contains("shield")) {
            if (super.attackcount <= 8) {
                attackcount++;
            } else {
                System.out.println("Has gone here");
                super.isAttacking = true;
                sword = swordAttack;
                playerA.decreaseHealth(1 + super.getGearCount());
                if (playerA.health == 0) {
                    this.coins += playerA.coins;
//                        playerA.death(time);
                    playerA.haveyoudied = true;
                    return playerA;
                }
                attackcount = 0;
            }
        }

        this.attackAITime = time;
        this.attackAIStart = true;
        return playerA;
    }

    /**
     * Sets the direction of player
     * @param d
     */
    public void setDir(Direction d) {
        this.dir = d;
    }


}
