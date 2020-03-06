package com.project.mazegame.tools;
import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.*;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Variables.*;

import com.project.mazegame.screens.GameScreen;
import java.lang.Math;
import java.lang.Integer;
import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.tools.Coordinate;
//import com.project.mazegame.tools.Cell;
//import com.project.mazegame.Pair;
import com.project.mazegame.tools.Variables;


/**
 * MultiPlayer use that class, it can split with solo module
 */
public class MultiCollect {
    public Coordinate position = new Coordinate();
    public Item item = new Item(" ", position);

    //    MultiPlayerGameScreen test;
    ArrayList<Item> mapItems;
    ArrayList<String> items;
    MultiPlayer multiplayer;
    public int indexOfItem;
    public ArrayList<Coordinate> positions;
    public MultiCollect (MazeGame game ,MultiPlayer player,MultiPlayerGameScreen gameClient) {
//        test = new MultiPlayerGameScreen(game,multiplayer.getName(),test.getNc().getServerIP());
        this.multiplayer=player;
        mapItems = gameClient.mapItems;
        items = player.items;
        //System.out.println("myMultiplayer x:"+player.position.getX());
        //System.out.println("myMultiplayer y:"+player.position.getY());
    }


    //ArrayList<Item> items = test.items;
    //if the player picks up an item, remove it from the map and return the item collected
    public Item pickedUp(Item item) {
        this.item = item;
        mapItems.remove(item);
        items.add(item.getType());
        return item;
    }

    public int getIndexOfItem(){

        return indexOfItem;
    }

    public Item nearestItem(MultiPlayer player) {
        Coordinate position = new Coordinate();
        position = mapItems.get(0).getPosition();

//		Item nearestItem = new Item(" ", position);
        Item nearestItem = mapItems.get(0);
        //System.out.println("items: " + mapItems.size());
        for (int i = 0; i < mapItems.size(); i++) {

            int tempX = mapItems.get(i).getPosition().getX();
            int tempY = mapItems.get(i).getPosition().getY();

//			int tempDist =player.position.getX() + player.position.getY() - tempX - tempY;
//			System.out.println("player.position x:"+player.getPosition().getX());
            int tempDist = andinsEuclidian(player.position.getX(), tempX, player.position.getY(), tempY);
            //System.out.println("temp Dist: " + tempDist);
//			int shortDist = player.position.getX() + player.position.getY() - nearestItem.getPosition().getX() - nearestItem.getPosition().getY();
            int shortDist = andinsEuclidian(player.position.getX(), nearestItem.getPosition().getX(), player.position.getY(), nearestItem.getPosition().getY());
            //System.out.println("shortDist: " + shortDist);

            if (tempDist < shortDist) {
                nearestItem = mapItems.get(i);
                indexOfItem=i;
            }
        }
        // System.out.println(nearestItem.getPosition().getX() + " , " + nearestItem.getPosition().getY());
        return nearestItem;
    }

    private int andinsEuclidian(int x1, int x2, int y1, int y2) {
        int sqrEucl = ( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) );
//		System.out.println()
        return sqrEucl;
    }

    //if the player is on the same coordinates as an item then pick it up and depending on what item it is, do the corresponding function
	/*public void main() {

		Player player = new Player(collisionLayer"James", 123);
		if (player.position == player.nearestItem().position) {

			Item item = pickedUp(player);
			Player player1 = new Player("James", 123);
			Player player2 = new Player("James", 123);
			if (!items.contains(item)) {
				if (item.getType() == "shield") {
					shield(item, player1);
				}
				if (item.getType() == "sword") {
					sword(item, player1, player2);
				}
				if (item.getType() == "compass") {
					compass(item);
				}
				if (item.getType() == "healingPotion") {
					healingPotion(item, player1);
				}
				if (item.getType() == "damagingPotion") {
					damagingPotion(item, player1);
				}
				if (item.getType() == "gearEnchantment") {
					gearEnchantment(item);
				}
			} else {
				items.remove(item);
			}
		}
	}
*/

    public void shield(Item item, MultiPlayer player1) {

        ArrayList<String> items = player1.items;
        // this.timer = timer;
        // this.seconds = 60;
        // temp solutions
        int timer = 20;
        int seconds = 60;
        System.out.println("player health " +player1.health);
        int startHealth = player1.health;
		/*if (gearEnchantment(item)) {
			seconds += 30;
		}*/


//		while (timer != 0) {
//			System.out.println("timer");
//			if (player1.health != startHealth) {
//				player1.health = startHealth;
//			}
//
//				//change that in the decreaseHealth class in Player
//
//
//			//display shield icon on screen
//
//		}

        //items.remove(item);

    }

    public void coin(MultiPlayer player1) {
        player1.coins ++;
        //ArrayList<String> items = player1.items;
    }

    public void sword(Item item, MultiPlayer player1, MultiPlayer player2) {
        ArrayList<String> items = player1.items;
        int swordPower = 1;
        if (gearEnchantment(item, player1)) {
            swordPower += 1;
        }

//		if (player2.health == 0) { //going to come back to level
//			swordPower += 1;
//		}
        player1.swordDamage++;
        //display sword icon on screen
    }

    public void compass(Item item) {
        //ArrayList<String> items = player1.items;
        //pick nearest player, follow it
        //display compass
    }

    public void healingPotion(MultiPlayer player1) {
        //ArrayList<String> items = player1.items;
        player1.generateHealth();
        player1.generateHealth();

        items.remove("healingPotion");
    }

    public void damagingPotion(Item item, MultiPlayer player1) {
        ArrayList<String> items = player1.items;
        player1.decreaseHealth(1);
        player1.decreaseHealth(1);
        items.remove("damagingPotion");
    }

    public boolean gearEnchantment(Item item, MultiPlayer player1) {
        ArrayList<String> items = player1.items;
        boolean collected = true;
        items.remove("gearEnchantment");
        return collected;


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
