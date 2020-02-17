package com.project.mazegame;
import java.util.ArrayList;

import com.project.mazegame.objects.AIPlayer;
public class test {
  public static void main(String[] args) {
    int hi = 1;
    Player player1 = new Player("James", 123);
    System.out.println(player1.position.toString());
    System.out.println(player1.name);
    System.out.println(player1.coins);
    System.out.println(player1.health);
    player1.decreaseHealth();
    System.out.println(player1.health);
    player1.decreaseHealth(7);
    player1.generateHealth();
    System.out.println(player1.health);
    player1.pickUpCoins(5);
    System.out.println(player1.coins);
    // player1.pickUpItem("Sword");
    // player1.pickUpItem("Compass");
    System.out.println(player1.toString());
    player1.death();
    System.out.println(player1.toString());
    AIPlayer All = new AIPlayer();
    System.out.println(All.name);
    System.out.println(All.coins);
    System.out.println(All.toString());
    ArrayList<Integer> direction = new ArrayList<>();
    direction.add(1);
    direction.add(2);
    direction.add(3);
    // System.out.println(All.direction(direction));

    Player death = new Player("Joe", 123);
    player1.playerHitPlayer(death);
    System.out.println(death.toString());
  }
}
