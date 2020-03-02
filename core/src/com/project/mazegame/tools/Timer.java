package com.project.mazegame.tools;

public class Timer {
    private float counter;
    private int time;

    public Timer() {
        time = 0;
    }

    public void updateTimer(float delta) {
        counter += delta;
        if (counter >= 1) {
            time++;
            counter = 0;
        }
    }

    public int currentTime() {
        return (int) time;
    }

}