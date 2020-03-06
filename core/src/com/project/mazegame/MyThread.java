package com.project.mazegame;
public class MyThread implements Runnable {
  String name;
  Thread r;
  public MyThread (String thread) {
    name = thread;
    r = new Thread(this, name);
    r.start();
  }
  public void run() {
    try {
      for(int i = 5; i > 0; i--) {
        System.out.println(name + ":" + r);
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      System.out.println(name + "Interuppted");

    }
    System.out.println(name + "exiting");
  }
}
