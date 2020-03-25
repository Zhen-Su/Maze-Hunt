package com.project.mazegame.objects;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.mazegame.networking.Messagess.MoveMessage;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.PlayersType;
import com.project.mazegame.tools.Timer;

import java.util.ArrayList;


public class MultiPlayer extends Player {

    private MultiPlayerGameScreen gameClient;
    public boolean bL, bU, bR, bD;
    private Direction dir;
    public static boolean debug = false;

    //constructors=================================================================================
    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, MultiPlayerGameScreen gameClient, Direction dir, String colour, PlayersType playersType) {
        super();
        if (debug) System.out.println("My Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.name = username;
        this.colour = colour;
        this.gameClient = gameClient;
        this.dir = dir;
        this.playersType=playersType;
        this.co = new Collect(this);

        initialPosition();
//        this.x = this.position.getX();
//        this.y = this.position.getY();
        loadPlayerTextures();

        createAnimations();

        if (debug) System.out.println("My Multiplayer instance construction done!");
    }

    public MultiPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir, String colour,PlayersType playersType) {
        super();
        if (debug) System.out.println("Other Multiplayer instance is constructing...");
        this.collisionLayer = collisionLayer;
        this.name = username;
        this.colour = colour;
        this.playersType=playersType;
        co = new Collect(this);

        this.x = x;
        this.y = y;
        this.position = new Coordinate(x, y);
        this.position.setX(x);
        this.position.setX(y);
        System.out.println("other player's position: (" + this.position.getX() + "," + this.position.getY() + ")");

        loadPlayerTextures();
        this.gameClient = gameClient;
        this.dir = dir;
        createAnimations();

        if (debug) System.out.println("Other Multiplayer instance construction done!");
    }

    //Getter&Setter=================================================================================
    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }
    //==============================================================================================

    /**
     * draw player in the screen, if player dead then only draw the death animation.
     *
     * @param sb
     */
    public void render(SpriteBatch sb) {

        setBatch(sb);
        if (this.isDead()) {
            font.getData().setScale(1f, 1f);
            String message = "Respawn in: " + (respawnCounter - time.currentTime() + 3);
            font.draw(sb, message, this.position.getX() - 100, this.position.getY() + 200);
            this.animation.render();
        } else {
            switch (dir) {
                case U:
                    setAnimation(UpAnim);
                    break;
                case D:
                    setAnimation(DownAnim);
                    break;
                case R:
                    setAnimation(RightAnim);
                    break;
                case L:
                    setAnimation(LeftAnim);
                    break;
                case STOP:
                    setAnimation(DownAnim);
                    break;
            }
            //render animation
            this.animation.render();
            //update player's position
            updateMotion();
            //draw sword and shield
            if (this.items.contains("sword"))
                sb.draw(sword, (float) (x), y - (height / 4), 50, 50);

            if (this.items.contains("shield"))
                sb.draw(shield, (float) (x - (width / 1.5)), y - (height / 2), shieldIconSize, shieldIconSize);

            if (this.items.contains("gearEnchantment"))
                sb.draw(gameClient.enchantedGlow, this.position.getX() - gameClient.enchantedGlow.getWidth() / 2, this.position.getY() - gameClient.enchantedGlow.getHeight() / 2, gameClient.enchantedGlow.getWidth(), gameClient.enchantedGlow.getHeight());

            //draw player's name
            font.getData().setScale(0.5f, 0.5f);
            font.draw(sb, this.name, this.position.getX() - 30, this.position.getY() + 60);
        }
    }

    /**
     * update player's state in a regular time(delta).
     *
     * @param delta
     */
    public void update(float delta, int mode, ArrayList<Item> items, float worldTime) {
        removeShield();
        removeEnchantment();
        time.updateTimer(delta);

        if (this.isDead()) {
            if (respawnCounter == 0) {
                respawnCounter = time.currentTime();
            }

            //After 3 second, then call death()
            if (time.currentTime() - respawnCounter == 3) {
                this.death();
                MoveMessage message = new MoveMessage(this.gameClient.getMultiPlayer().getID(), this.position.getX(), this.position.getY(), dir);
                gameClient.getNc().send(message);
            }
            setAnimation(DyingAnim);
        }
    }

    /**
     * Handle key release event
     *
     * @param keycode
     * @return
     */
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
                bR = true;
                break;
            case Input.Keys.LEFT:
                bL = true;
                break;
            case Input.Keys.UP:
                bU = true;
                break;
            case Input.Keys.DOWN:
                bD = true;
                break;
        }
        locateDirection();
        return true;
    }

    /**
     * Handle key press event
     *
     * @param keycode
     * @return
     */
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.RIGHT:
                bR = false;
                break;
            case Input.Keys.LEFT:
                bL = false;
                break;
            case Input.Keys.UP:
                bU = false;
                break;
            case Input.Keys.DOWN:
                bD = false;
                break;
        }
        locateDirection();
        return true;
    }

    /**
     * Update player's position
     */
    public void updateMotion() {

        this.position.setX(x);
        this.position.setY(y);

//        // update the move to as they contantly get updated in the render method
//        moveTo.setX(x);
//        moveTo.setY(y);

        switch (dir) {
            case R:
                if (x < (collisionLayer.getWidth() * collisionLayer.getTileWidth()) - width) {
                    x += speed;
                    //check player isn't in a wall
                    if (!checkCollisionMap(x, y)) { //if it's in a wall, move player back
                        x -= speed;
                    } else
                        this.position.setX(x);
                }
                break;
            case L:
                if (x > 0) {
                    x -= speed;
                    if (!checkCollisionMap(x, y)) {
                        x += speed;
                    } else
                        this.position.setX(x);
                }
                break;
            case U:
                if (y < (collisionLayer.getHeight() * collisionLayer.getTileHeight()) - height) {
                    y += speed;
                    if (!checkCollisionMap(x, y)) {
                        y -= speed;
                    } else
                        this.position.setY(y);
                }
                break;
            case D:
                if (y > 0) {
                    y -= speed;
                    if (!checkCollisionMap(x, y)) {
                        y += speed;
                    } else
                        this.position.setY(y);
                }
                break;
            case STOP:
                break;
        }

//        // update the move to as they contantly get updated in the render method
//        moveTo.setX(x);
//        moveTo.setY(y);

    }

    /**
     * If the direction is changed, send a move message to game server immediately.
     * boolean bL, bU, bR, bD is only used to decide when should send message
     */
    private void locateDirection() {
        Direction oldDir = this.dir;

        if (bR && !bL && !bD && !bU) {
            dir = Direction.R;
        } else if (bL && !bR && !bU && !bD) {
            dir = Direction.L;
        } else if (bU && !bD && !bL && !bR) {
            dir = Direction.U;
        } else if (bD && !bU && !bR && !bL) {
            dir = Direction.D;
        } else if (!bL && !bD && !bR && !bU) {
            dir = Direction.STOP;
        }

        if (dir != oldDir) {
            MoveMessage message = new MoveMessage(ID, this.position.getX(), this.position.getY(), dir);
            gameClient.getNc().send(message);
        }
    }

    public void initialPosition() {
        int maxX = collisionLayer.getWidth();
        int maxY = collisionLayer.getHeight();

        int ranx = (int) ((Math.random() * (maxX)));
        int rany = (int) ((Math.random() * (maxY)));

        this.position.setX(ranx * (int) collisionLayer.getTileWidth() + 50);
        this.position.setY(rany * (int) collisionLayer.getTileHeight() + 50);

        if (isCellBlocked((float) position.getX(), (float) position.getY())) {
            initialPosition();
        }

        this.x = this.position.getX();
        this.y = this.position.getY();
    }

    public boolean checkCollisionMap(float possibleX, float possibleY) { // true = good to move | false = can't move there
        //Overall x and y of player
        float xWorld = possibleX;
        float yWorld = possibleY;

        boolean collisionWithMap = false;

        //Check corners of player to check for collision
        //check corners T = top, B = bottom, R = right, L = left
        boolean TLbool = isCellBlocked(xWorld - (width / 2), yWorld + (height / 2));
        boolean TRbool = isCellBlocked(xWorld + (width / 2), yWorld + (height / 2));
        boolean BLbool = isCellBlocked(xWorld - (width / 2), yWorld - (height / 2));
        boolean BRbool = isCellBlocked(xWorld + (width / 2), yWorld - (height / 2));

        collisionWithMap = TLbool || TRbool || BLbool || BRbool;

        //If there is a collision
        if (collisionWithMap) return false;
        else return true;
    }

    public boolean isCellBlocked(float x, float y) {

        Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                & cell.getTile().getProperties().containsKey("isWall");
    }

    public void decreaseHealth(int number) {
        this.health -= number;
    }

    public void generateHealth() {
        if (this.health != 9) {
            this.health++;
        }
    }

    public void increaseSwordXP(int XP) {
        this.swordXP += XP;
    }

    public void increaseShieldXP(int XP) {
        this.shieldXP += XP;
    }

    public boolean isDead() {
        if (this.health <= 0) {
            return true;
        } else
            return false;
    }

    public void death() {
        this.initialPosition();
        this.x = this.position.getX();
        this.y = this.position.getY();
        setAnimation(DownAnim);
        this.coins = 0;
        this.health = 5;
        this.items.clear();
        this.respawnCounter = 0;
    }

//    public void playerHitPlayer(Player hit) {
//        // write boolean to check sword
//
//        if (this.items.contains("sword") && !hit.items.contains("shield")) {
//            // will need to do if have item then that can be called
//            // then decrease the helath based on that
//            // could have a damage do attribute and various attributes which change throught the generateMapItems
//            hit.decreaseHealth(this.swordDamage);
//            if (hit.health == 0) {
//                this.swordDamage++;
//                this.coins += hit.coins;
//                hit.death();
//            }
//        }
//        //need to add shield stuffr
//    }

    public void removeShield() {
        if (!this.items.contains("shield")) {
            return;
        }
        if ((time.currentTime()) - initialisedShieldTime == 10) {
            int indexOfItem = items.indexOf("shield");
            if(debug) System.out.println("Before remove: " + items);
            if(debug) System.out.println("Index of shield:" + indexOfItem);
            this.items.remove("shield");
            if(debug) System.out.println("After remove: " + items);
        }
    }

    public void removeEnchantment() {
        if (!this.items.contains("gearEnchantment")) {
            return;
        }
        if ((time.currentTime()) - initialisedEnchantmentTime == 10) {
            int indexOfItem = items.indexOf("gearEnchantment");
            if(debug) System.out.println("Before remove: " + items);
            if(debug) System.out.println("Index of gearEnchantment:" + indexOfItem);
            this.items.remove("gearEnchantment");
            if(debug) System.out.println("After remove: " + items);
        }
    }

    public int getHealth() {
        return health;
    }

    public void dispose() {
    }

}
