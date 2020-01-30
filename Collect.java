import java.lang.Math;
import java.lang.Integer;
import java.util.ArrayList;

public class Collect {
	public Pair position = new Pair(0,0);
	public Item item = new Item(" ", position);
	public ArrayList<Item> items;
	public ArrayList<Item> mapItems;

	//if the player picks up an item, remove it from the map and return the item collected
	public Item pickedUp(Item item) {

		this.item = item;
		mapItems.remove(item);

		return item;

	}

	public void generateMapItems() {
		int maxShields = 3;
		int maxCoins = 40;
		int maxSwords = 5;
		int maxCompasses = 5;
		int maxPotions = 10;
		int maxX = 1000;
		int maxY = 1000;

		for (int i = 0; i < maxShields; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("shield", position);
			mapItems.add(item);

		}

		for (int i = 0; i < maxCoins; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("coin", position);
			mapItems.add(item);

		}

		for (int i = 0; i < maxSwords; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("sword", position);
			mapItems.add(item);

		}

		for (int i = 0; i < maxCompasses; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("compass", position);
			mapItems.add(item);

		}

		for (int i = 0; i < maxPotions; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			int whatPotion = (int)(Math.random() * 4);

			if (whatPotion == 1) {
				Item item = new Item("healingPotion", position);
			} else if (whatPotion == 2) {
				Item item = new Item("damagingPotion", position);
			} else {
				Item item = new Item("gearEnchantment", position);
			}

			mapItems.add(item);

		}

	}

	public Item nearestItem(Player player) {
		Pair position = new Pair(1000, 1000);
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
	}

	//if the player is on the same coordinates as an item then pick it up and depending on what item it is, do the corresponding function
	public void main() {

		Player player = new Player(/*attributes*/);
		if (player.getPosititon() == player.nearestItem().getPosition()) {

			Item item = pickedUp(player);

			if (!items.contains(item)) {
				if (item.getType() == "shield") {
					shield(item);
				}
				if (item.getType() == "sword") {
					sword(item);
				}
				if (item.getType() == "compass") {
					compass(item);
				}
				if (item.getType() == "healingPotion") {
					healingPotion(item);
				}
				if (item.getType() == "damagingPotion") {
					damagingPotion(item);
				}
				if (item.getType() == "gearEnchantment") {
					gearEnchantment(item);
				}
			} else {
				items.remove(item);
			}
		}
	}


	public void shield(Item item) {
		this.timer = timer;
		this.seconds = 60;
		if (gearEnchantment(item)) {
			this.seconds += 30;
		}


		while (timer != 0) {
			if (decreaseHealth()) {
				generateHealth();
			}
				//change that in the decreaseHealth class in Player
			

			//display shield icon on screen

		}

		items.remove(item);

	}

	public void coin() {

	}

	public void sword(Item item) {
		int swordPower = 1;
		if (gearEnchantment(item)) {
			swordPower += 1;
		}

		if (attacks()) {
			swordPower += 1;
		}

		//display sword icon on screen
	}

	public void compass(Item item) {
		//pick nearest player, follow it
		//display compass
	}

	public void healingPotion(Item item) {
		generateHealth();
		generateHealth();
		items.remove(item);
	}

	public void damagingPotion(Item item) {
		decreaseHealth();
		decreaseHealth();
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
















