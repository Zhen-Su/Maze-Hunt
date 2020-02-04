package com.mygdx.game;
import java.lang.Math;
import java.util.ArrayList;
// to do integrate the collectble and write more classes for that
public class AIPlayer extends Player {

  public AIPlayer() {
    super("AI", 123);

    super.coins = 5;

    int randomx = (int)(Math.random() * ((100 - 0) +1));
    int randomy = (int)(Math.random() * ((100 - 0) +1));
    super.position = new Pair(randomx, randomy);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public Pair direction(ArrayList<Pair> openDoor) {
    int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
    return openDoor.get(openDoor.indexOf(randomTake));
  }

  // will need method to gather all squares which are available and put them in a list
  public void play() {

    while(true /* will be replaced by timmer boolean */) {
      ArrayList<Pair> test = new ArrayList<>();
      test.add(new Pair(1, 2));
      test.add(new Pair(3, 4));
      super.move(direction(test));
      // if condition to pick up item
      // super.pickUpItem("h");
      //if condition to detect if there is a player
      while(true /* somehting to check if player on smae square*/) {
        // super.playerHitPlayer(/* player interacting */);

      }
    }
  }

}
