import java.lang.Math;
import java.lang.Integer;
import java.util.ArrayList;

public class Collect {
	public Cell position = new Cell();
	public Item item = new Item(" ", position);
	public ArrayList<Item> items;
	public ArrayList<Item> mapItems;
	public ArrayList<Cell> positions;

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
		int maxMaps = 5;
		int maxPotions = 10;
		int maxX = 1000;
		int maxY = 1000;
		Boolean gotShield = false;

		for (int i = 0; i < maxShields; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("shield", position);
			// make arrayList positions with all positions of items
			if (!positions.contains(position) && !player.isCellBlocked(position)) {
				mapItems.add(item);
				positions.add(position);
			}
			// do this for all


		}

		for (int i = 0; i < maxCoins; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("coin", position);
			if (!positions.contains(position) && !isCellBlocked(position)) {
				mapItems.add(item);
				positions.add(position);
			}

		}

		for (int i = 0; i < maxSwords; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("sword", position);
			if (!positions.contains(position) && !isCellBlocked(position)) {
				mapItems.add(item);
				positions.add(position);
			}
		}

		for (int i = 0; i < maxMaps; i++) {
			position.setX((int)(Math.random() * (maxX + 1)));
			position.setY((int)(Math.random() * (maxY + 1)));
			Item item = new Item("map", position);
			if (!positions.contains(position) && !isCellBlocked(position)) {
				mapItems.add(item);
				positions.add(position);
			}
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

			if (!positions.contains(position) && !isCellBlocked(position)) {
				mapItems.add(item);
				positions.add(position);
			}
		}
	}

	public Item nearestItem(Player player) {
		Cell position = new Cell(1000, 1000);
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
		if ((player.getPosititon().getX() > nearestItem(player).getPosition().getX() - 50) && (player.getPosititon().getX() < nearestItem(player).getPosition().getX() + 50) && (player.getPosititon().getY() > nearestItem(player).getPosition().getY() - 50) && (player.getPosititon().getY() < nearestItem(player).getPosition().getY() + 50)) {
			//INCLUDE COINS!!!

			if (!items.contains(item)) {
				Item item = pickedUp(player);
				if (item.getType() == "shield") {
					shield(item);
				}
				if (item.getType() == "sword") {
					sword(item);
				}
				if (item.getType() == "map") {
					map(item);
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

		gotShield = true;
		int time = 60000;
		if (collected) {
			time += 30000;
		}

		TimerTask task = new TimerTask() {
			public void run() {
				items.remove("shield");

			}
		};

		CountDown countDown =  new CountDown(task, time);
		




		
				//change that in the decreaseHealth class in Player
			

			//display shield icon on screen

	}

	public void coin() {
		// player already has a coin method

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

	public void map(Item item) {
		//shows where you and other players are on the map but doesn't show walls
		//display map
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
		boolean collected = false;
		if (items.contains(item)) {
			collected = true;
		}
		return collected;

		
	}


}








/*Recap : what is left to code
	- try to find better way to implement a timer -> see library
	- a mapItem ArrayList (l.39) that contains all the items present on the map - DONE
	- a position() method to determine both a player and an item's coordinates - DONE for item
	- a nearestItem() method that returns the item with the closest coordinates to the player. This would need to continuously update, as the player will move around - DONE
	- a nearestPlayer() method that returns the player with the closest coordinates to the player
	- do research on how to program a with a moving target
	- display methods to display certain layers on the screen */
















