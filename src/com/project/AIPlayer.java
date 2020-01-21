package com.project;
import java.lang.Math;
import java.util.ArrayList;
public class AIPlayer extends Player {

  public AIPlayer() {
    super("AI");
    super.coins = 5;
  }
  @Override
  public String toString() {
    return super.toString();
  }
  public int direction(ArrayList<Integer> openDoor) {
    int direction = (int)(Math.random() * (((openDoor.size())) + 1));
    return direction;

  }
}
