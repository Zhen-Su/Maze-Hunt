import java.util.Timer; 
import java.util.TimerTask;

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
	private Pair itemPosition;

	public Item(String type) {
		this.type = type;
	}

	public Pair getItemPosition
	
	public String getType() {
		return category;
	}

}

public class Collect {
	public Item item;
	public ArrayList<String> items;
	public Pair position;

	//if the player picks up an item, remove it from the map and return the item collected
	public Item pickedUp() {

		/*mapItems*/.remove(item);
		this.item = item;

		return item;

	}

	//if the player is on the same coordinates as an item then pick it up and depending on what item it is, do the corresponding function
	public void main() {

		// for 
		if (player.posititon() == nearestItem.position()) {
			// need to code a position() method
			// need to code nearest item 
			Item item = pickedUp();

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
		//display layer that contains compass
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
	- try to find better way to implement a timer
	- a mapItem ArrayList (l.39) that contains all the items present on the map
	- a position() method to determine both a player and an item's coordinates
	- a nearestItem() method that returns the item with the closest coordinates to the player. This would need to continuously update, as the player will move around
	- a nearestPlayer() method that returns the player with the closest coordinates to the player
	- do research on how to program a compass with a moving target
		do we have to make a compass? cool but very hard
		can ask melissa
	- display methods to display certain layers on the screen
















