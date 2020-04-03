package com.project.mazegame.tools;

import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.*;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Variables.*;

import com.project.mazegame.screens.GameScreen;

import java.lang.Math;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.TimerTask;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.tools.Coordinate;
//import com.project.mazegame.tools.Cell;
//import com.project.mazegame.Pair;
import com.project.mazegame.tools.Variables;

/**
 * The Collect class is used for keeping track of which item is the nearest to the player at all times,
 * picking up that item if the player traverses over it, and dictating what each item does.
 * @author Selma Kander
 *
 */
public class Collect {
    public Coordinate position = new Coordinate();
    public Item item = new Item(" ", position);
    ArrayList<Item> mapItems;
    ArrayList<String> items;

    /**
     * If this player is a instance of Player or AIPlayer, then use mapItems which created in GameScreen
     * If this player is a instance of Multiplayer or MultiAIPlayer, then use mapItems which created  in MultiplayerGameScreen
     * @param player
     */
    public Collect(Player player) {

        switch(player.playersType){
            case single:
                this.mapItems = GameScreen.mapItems;
                break;
            case multi:
                this.mapItems = MultiPlayerGameScreen.mapItems;
                break;
        }
        this.items = player.items;
    }

    //ArrayList<Item> items = test.items;
    //if the player picks up an item, remove it from the map and return the item collected
    public Item pickedUp(Item item) {
        this.item = item;
        mapItems.remove(item);
        items.add(item.getType());
        return item;
    }
/**
 * To find the nearest item, we loop through the array of map items and calculate the Euclidian distance from the player.
 * There is a method for each type of item and when an item is picked up the method which corresponds to the type of the item is called.
 * @param player
 * @return
 */
    public Item nearestItem(Player player) {
        Item nearestItem = mapItems.get(0);
      
        for (int i = 0; i < mapItems.size(); i++) {

            int tempX = mapItems.get(i).getPosition().getX();
            int tempY = mapItems.get(i).getPosition().getY();
            int tempDist = andinsEuclidian(player.position.getX(), tempX, player.position.getY(), tempY);
            int shortDist = andinsEuclidian(player.position.getX(), nearestItem.getPosition().getX(), player.position.getY(), nearestItem.getPosition().getY());
          
            if (tempDist < shortDist) {
                nearestItem = mapItems.get(i);
            }
        }
        return nearestItem;
    }

    public static int andinsEuclidian(int x1, int x2, int y1, int y2) {
        int sqrEucl = ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return sqrEucl;
    }


    public void coin(Player player1) {
        player1.coins++;
    }

    public void sword(Item item, Player player1) {
        player1.swordDamage++;
    }

    public void healingPotion(Player player1) {
        //ArrayList<String> items = player1.items;
        player1.generateHealth();
        player1.generateHealth();

        items.remove("healingPotion");
    }

    public void damagingPotion(Player player1) {
        player1.decreaseHealth(3);
    }

    public void gearEnchantment(Item item, Player player1) {
        player1.increaseSwordXP(1);
        player1.increaseShieldXP(1);
    }


}




/*Recap : what is left to code
	- try to find better way to implement a timer -> see library
	- a mapItem ArrayList (l.39) that contains all the items present on the map - DONE
	- a position() method to determine both a player and an item's coordinates - DONE for item
	- a nearestItem() method that returns the item with the closest coordinates to the player. This would need to continuously update, as the player will move around - DONE
	- a nearestPlayer() method that returns the player with the closest coordinates to the player
	- do research on how to program a compass with a moving target
		do we have to make a compass? cool but very hard
		can ask melissa
	- display methods to display certain layers on the screen */
