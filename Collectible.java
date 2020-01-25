import java.util.Timer; 
import java.util.TimerTask;
import java.lang.Math;
import java.lang.Integer;

public class CountDown {
public Timer timer;

	public CountDown() { 
		timer = new Timer(); 
		timer.schedule(new DisplayCountdown(), 0, 1000); 
	}
}

class DisplayCountdown extends TimerTask { 
	int seconds;

}

public class Item {
	private String type;
	private int x;
	private int y;
	private Pair itemPosition = new Pair (x,y);

	public Item(String type, Pair itemPosition) {
		this.type = type;
		this.itemPosition = itemPosition;
	}


	public void setType(String type) {
		this.type = type;
	}

	public void setPosition(Pair itemPosition) {
		this.itemPosition = itemPosition;
	}

	public Pair getPosition() {
		return itemPosition;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String getType() {
		return type;
	}

}

public class Collect {
	public Item item = new Item(' ', (0, 0));
	public ArrayList<Item> items;
	public Pair position;
	public ArrayList<Item> mapItems;

	//if the player picks up an item, remove it from the map and return the item collected
	public Item pickedUp(Item item) {

		this.item = item;
		mapItems.remove(item);

		return item;

	}

	public void generateMapItems() {
		public int maxShields = 3;
		public int maxCoins = 40;
		public int maxSwords = 5;
		public int maxCompasses = 5;
		public int maxPotions = 10;
		public int maxX;
		public int maxY;

		for (int i = 0; i < maxShields; i++) {
			int xValue = (Math.random() * (maxX + 1));
			int yValue = (Math.random() * (maxY + 1));
			Item item = new Item("shield", (xValue, yValue));
			mapItems.add(item);

		}

		for (int i = 0; i < maxCoins; i++) {
			int xValue = (Math.random() * (maxX + 1));
			int yValue = (Math.random() * (maxY + 1));
			Item item = new Item("coin", (xValue, yValue));
			mapItems.add(item);

		}

		for (int i = 0; i < maxSwords; i++) {
			int xValue = (Math.random() * (maxX + 1));
			int yValue = (Math.random() * (maxY + 1));
			Item item = new Item("sword", (xValue, yValue));
			mapItems.add(item);

		}

		for (int i = 0; i < maxCompasses; i++) {
			int xValue = (Math.random() * (maxX + 1));
			int yValue = (Math.random() * (maxY + 1));
			Item item = new Item("compass", (xValue, yValue));
			mapItems.add(item);

		}

		for (int i = 0; i < maxPotions; i++) {
			int xValue = (Math.random() * (maxX + 1));
			int yValue = (Math.random() * (maxY + 1));
			int whatPotion = (Math.random() * 4);

			if (whatPotion == 1) {
				Item item = new Item("healingPotion", (xValue, yValue));
			} else if (whatPotion == 2) {
				Item item = new Item("damagingPotion", (xValue, yValue));
			} else {
				Item item = new Item("gearEnchantment", (xValue, yValue));
			}

			mapItems.add(item);

		}

	}

	public Item nearestItem(Player player) {
		Item nearestItem = new Item(' ', (MAX_VALUE, MAX_VALUE));

		for (int i = 0; i < mapItems.size(); i++) {
			int tempX = mapItems.get(i).getX();
			int tempY = mapItems.get(i).getY();

			int tempDist = player.getX() + player.getY() - tempX - tempY;
			int shortDist = player.getX() + player.getY() - nearestItem.getX() - nearestItem.getY();

			if (tempDist < shortDist) {
				nearestItem = mapItems.get(i);
			}
		}
	}

	//if the player is on the same coordinates as an item then pick it up and depending on what item it is, do the corresponding function
	public void main() {

		Player player = new Player(/*attributes*/)
		if (player.getPosititon() == player.nearestItem().getPosition()) {

			Item item = pickedUp(player);

			if (!items.contains(item)) {
				if (item.getType() == "shield")
					shield();
				if (item.getType() == "sword")
					sword();
				if (item.getType() == "compass")
					compass();
				if (item.getType() == "healingPotion")
					healingPotion();
				if (item.getType() == "damagingPotion")
					damagingPotion();
				if (item.getType() == "gearEnchantment")
					gearEnchantment();
			} else {
				items.remove(item);
			}
		}
	}


	public void shield() {
		this.timer = timer;
		this.seconds = 60;
		if (gearEnchantment()) 
			this.seconds += 30;


		while (timer != 0) {
			if (decreaseHealth()) 
				generateHealth();
				//change that in the decreaseHealth class in Player
			

			//display shield icon on screen

		}

		items.remove(shield);

	}

	public void coin() {

	}

	public void sword() {
		int swordPower = 1;
		if (gearEnchantment())
			swordPower += 1;

		if (attacks())
			swordPower += 1;

		//display sword icon on screen
	}

	public void compass() {
		//pick nearest player, follow it
		//display compass
	}

	public void healingPotion() {
		generateHealth();
		generateHealth();
		items.remove(healingPotion);
	}

	public void damagingPotion() {
		decreaseHealth();
		decreaseHealth();
		items.remove(damagingPotion);
	}

	public boolean gearEnchantment() {
		boolean collected = false;
		if (pickedUp() == gearEnchantment)
			collected = true;
		items.remove(gearEnchantment);
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
	- display methods to display certain layers on the screen
















