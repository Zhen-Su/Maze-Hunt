package com.project.mazegame.tools;
import java.util.Timer;
import java.util.TimerTask;

public class Timer {
public Timer timer;

	public Timer() {
		timer = new Timer();
		timer.schedule(new DisplayCountdown(), 0, 1000);
	}
}

class DisplayCountdown extends TimerTask {
	int seconds;

}
