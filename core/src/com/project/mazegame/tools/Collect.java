package com.project.mazegame.tools;
import com.project.mazegame.objects.*;
import com.project.mazegame.screens.GameScreen;
import java.lang.Math;
import java.lang.Integer;
import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.tools.ItemCell;
//import com.project.mazegame.tools.Cell;
//import com.project.mazegame.Pair;
import com.project.mazegame.tools.Variables;

public class Collect {
	public ItemCell position = new ItemCell();
	public Item item = new Item(" ", position);
	public ArrayList<Item> items;
	
	public ArrayList<ItemCell> positions;
	private ArrayList<Item> mapItems = Variables.mapItems;
	
	//if the player picks up an item, remove it from the map and return the item collected
	public Item pickedUp(Item item) {
		this.item = item;
		mapItems.remove(item);

		return item;
	}

	public Item nearestItem(Player player) {
		ItemCell position = new ItemCell();
		Item nearestItem = new Item(" ", position);

		for (int i = 0; i < mapItems.size(); i++) {
			int tempX = mapItems.get(i).getPosition().getX();
			int tempY = mapItems.get(i).getPosition().getY();

			int tempDist = player.getX() + player.getY() - tempX - tempY;
			int shortDist = player.getX() + player.getY() - nearestItem.getPosition().getX() - nearestItem.getPosition().getY();

			if (tempDist < shortDist) {
				nearestItem = mapItems.get(i);
			}
		}
		return nearestItem;
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

	public void shield(Item item, Player player1) {
		// this.timer = timer;
		// this.seconds = 60;
		// temp solutions
		int timer = 20;
		int seconds = 60;
		int startHealth = player1.health;
		if (gearEnchantment(item)) {
			seconds += 30;
		}


		while (timer != 0) {
			if (player1.health != startHealth) {
				player1.health = startHealth;
			}

				//change that in the decreaseHealth class in Player


			//display shield icon on screen

		}

		items.remove(item);

	}

	public void coin() {

	}

	public void sword(Item item, Player player1, Player player2) {
		int swordPower = 1;
		if (gearEnchantment(item)) {
			swordPower += 1;
		}

		if (player2.health == 0) { //going to come back to level
			swordPower += 1;
		}
		player1.swordDamage++;
		//display sword icon on screen
	}

	public void compass(Item item) {
		//pick nearest player, follow it
		//display compass
	}

	public void healingPotion(Item item, Player player1) {
		player1.generateHealth();
		player1.generateHealth();
		items.remove(item);
	}

	public void damagingPotion(Item item, Player player1) {
		player1.decreaseHealth(1);
		player1.decreaseHealth(1);
		items.remove(item);
	}

	public boolean gearEnchantment(Item item) {
		boolean collected = true;
		items.remove(item);
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
