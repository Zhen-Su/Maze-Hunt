package com.badlogic.gdx.utils.Timer;
import java.util.Timer; 
import java.util.TimerTask;
import java.lang.Object;
import java.util.ArrayList;


public class CountDown {

	ArrayList<Item> items = new ArrayList<Item>();
	Timer timer = new Timer();
	int time;
	TimerTask task = new TimerTask();
	


	public CountDown(Timertask task, int time) {
		this.task = task;
		this.time = time;

	}
	
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