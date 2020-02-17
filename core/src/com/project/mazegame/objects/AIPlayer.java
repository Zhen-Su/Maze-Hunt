package com.project.mazegame.objects;
import java.lang.Math;
import java.util.ArrayList;
import com.project.mazegame.screens.*;
import com.project.mazegame.tools.ItemCell;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
//import com.project.mazegame.Pair;
// to do integrate the collectble and write more classes for that


public class AIPlayer extends Player {
	

public static TiledMapTileLayer collisionLayer;

  public AIPlayer() {
	//add collision layer
    super( collisionLayer,"AI", 123);

    super.coins = 5;

    int randomx = (int)(Math.random() * ((100 - 0) +1));
    int randomy = (int)(Math.random() * ((100 - 0) +1));
    super.position = new ItemCell();
    super.position.setX(randomx);
    super.position.setY(randomy);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public ItemCell direction(ArrayList<ItemCell> openDoor) {
    int randomTake = (int)(Math.random() * ((openDoor.size() - 1) + 1));
    return openDoor.get(openDoor.indexOf(randomTake));
  }

  // will need method to gather all squares which are available and put them in a list
  public void play() {

    while(true /* will be replaced by timmer boolean */) {
      ArrayList<ItemCell> test = new ArrayList<>();
      ItemCell item1 = new ItemCell();
      item1.setX(1);
      item1.setY(2);
      ItemCell item2 = new ItemCell();
      item2.setX(2);
      item2.setY(5);
      
      test.add(item1);
      test.add(item2);
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
