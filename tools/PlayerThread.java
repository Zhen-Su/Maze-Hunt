package com.project.mazegame.tools;

public class PlayerThread implements Runnable{
    public void run() {
        try {
            System.out.println("Thread currently running " + Thread.currentThread().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
