package com.project.mazegame.tools;

/**
 *Code written by: Andin
 **/
public class Timer {
	private float counter;
	private int time;

	/**
	 * Call constructer to initialize the object
	 * The constructor will initialize the time to zero
	 */
	public Timer() {
		time = 0;
	}

	/**
	 * must be called inside the render method
	 * @param delta to keep track of time
	 *             To check time, call currentTime();
	 */
	public void updateTimer(float delta) {
		counter += delta;
		if (counter >= 1) {
			time++;
			counter = 0;
		}
	}

	/**
	 * Will return the current time from when the object is initialized, given that the updateTimer(float delta) method is called inside the render method.
	 * The time return is in seconds.
	 * @return
	 */
	public int currentTime() {
		return (int) time;
	}

}
