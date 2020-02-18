package com.badlogic.gdx.utils.Timer;
import java.util.Timer; 
import java.util.TimerTask;
import java.lang.Object;
import java.util.ArrayList;


public class CountDown {

	boolean collected = false;
	ArrayList<Item> items = new ArrayList();
	Timer timer = new Timer();
	int time = 60000;
	


	public CountDown(boolean collected, ArrayList<Item> items) {
		this.collected = collected;
		this.items = items;
		if (collected) {
			time += 30000;		
		}

	}



	TimerTask task = new TimerTask() {
		public void run() {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getType() == "shield") {
					items.remove(items.get(i));
				}
			} 

		}
	};

	public void start() {
		timer.scheduleAtFixedRate(task, time, 0);
	}

	public void main() {
		start();
	}
}

// public class Timer extends Collect {
// 	static Timer instance = new Timer();
// 	int period = 60000;
// 	Item item = new Item(" ", (0,0));
	

// 	private void shieldCalled(Item item) {
// 		this.item = item;
// 		if(gearEnchantment(item)) {
// 		period += 1.5 * period;
// 	}
// 		instance.schedule(new run(), period);
// 	}

// 	public void run(Item item) {
// 		this.item = item;
// 		if (gotShield == true) {
// 			items.remove(item);
// 		}
		
// 		// need to figure out where i need to declare the item
// 	}
	
// }

/*public class CountDown {
public Timer timer;

	public CountDown() { 
		timer = new Timer(); 
		timer.schedule(new DisplayCountdown(), 0, 1000); 
	}
}

class DisplayCountdown extends TimerTask { 
	int seconds;

}*/