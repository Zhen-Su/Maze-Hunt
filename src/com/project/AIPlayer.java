package com.project;
import java.lang.Math;
import java.util.ArrayList;
public class AIPlayer extends Player {

  public AIPlayer() {
    super("AI");
    super.coins = 5;
    int randomx = (int)(Math.random() * ((100 - 0) +1));
    int randomy = (int)(Math.random() * ((100 - 0) +1));
    super.position = new Pair(randomx, randomy);
  }
  @Override
  public String toString() {
    return super.toString();
  }
  public int direction(ArrayList<Integer> openDoor) {
    int direction = (int)(Math.random() * (((openDoor.size())) + 1));
    return direction;

  }
  @Override
  public void death() {
    //some command to remove player
    System.out.println("AI Player is dead");
  }
}
