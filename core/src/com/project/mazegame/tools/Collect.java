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

    public Item nearestItem(Player player) {
//		Coordinate position = new Coordinate();
//		position = mapItems.get(0).getPosition();

//		Item nearestItem = new Item(" ", position);
        Item nearestItem = mapItems.get(0);
        //System.out.println("items: " + mapItems.size());
        for (int i = 0; i < mapItems.size(); i++) {

            int tempX = mapItems.get(i).getPosition().getX();
            int tempY = mapItems.get(i).getPosition().getY();

//			int tempDist =player.position.getX() + player.position.getY() - tempX - tempY;
            int tempDist = andinsEuclidian(player.position.getX(), tempX, player.position.getY(), tempY);
            //System.out.println("temp Dist: " + tempDist);
//			int shortDist = player.position.getX() + player.position.getY() - nearestItem.getPosition().getX() - nearestItem.getPosition().getY();
            int shortDist = andinsEuclidian(player.position.getX(), nearestItem.getPosition().getX(), player.position.getY(), nearestItem.getPosition().getY());
            //System.out.println("shortDist: " + shortDist);

            if (tempDist < shortDist) {
                nearestItem = mapItems.get(i);
                //System.out.println("found shorter!");
            }
        }
        //System.out.println(nearestItem.getPosition().getX() + " , " + nearestItem.getPosition().getY());
        return nearestItem;
    }

    public static int andinsEuclidian(int x1, int x2, int y1, int y2) {
        int sqrEucl = ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
//		System.out.println()
        return sqrEucl;
    }


    public void shield(Item item, Player player1) {


        final ArrayList<String> items = player1.items;
        int seconds = 60;
        int startHealth = player1.health;

        int time = 60000;

    }

    public void coin(Player player1) {
        player1.coins++;
        //ArrayList<String> items = player1.items;
    }

    public void sword(Item item, Player player1) {
        ArrayList<String> items = player1.items;
        int swordPower = player1.getSwordXP();
//		if (player1.items.contains("gearEnchantment")) {
//			swordPower += 5;
//		}

//		if (player2.health == 0) { //going to come back to level
//			swordPower += 1;
//		}
        player1.swordDamage++;
        //display sword icon on screen
    }

    public void minimap(Item item) {
        //ArrayList<String> items = player1.items;
        //pick nearest player, follow it
        //display compass
    }

    public void healingPotion(Player player1) {
        //ArrayList<String> items = player1.items;
        player1.generateHealth();
        player1.generateHealth();

        items.remove("healingPotion");
    }

    public void damagingPotion(Player player1) {
        ArrayList<String> items = player1.items;

        player1.decreaseHealth(10);

//		player1.playerPosioned();

        //items.remove("damagingPotion");
        //player1.loadPlayerTextures();
    }

    public void gearEnchantment(Item item, Player player1) {
        ArrayList<String> items = player1.items;
        //boolean collected = true;
        player1.increaseSwordXP(1);
        player1.increaseShieldXP(1);
        System.out.println("added xp" + player1.getShieldXP() + " , " + player1.getSwordXP());


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
