package com.project;
public class test {
  public static void main(String[] args) {
    Player player1 = new Player("James");
    System.out.println(player1.position.toString());
    System.out.println(player1.name);
    System.out.println(player1.coins);
    System.out.println(player1.health);
    player1.decreaseHealth();
    System.out.println(player1.health);
    player1.decreaseHealth(2);
    player1.generateHealth();
    System.out.println(player1.health);
    player1.pickUpCoins(5);
    System.out.println(player1.coins);
    player1.pickUpItem("Sword");
    player1.pickUpItem("Compass");
    System.out.println(player1.toString());
    player1.death();
    System.out.println(player1.toString());
    AIPlayer All = new AIPlayer();
    System.out.println(All.name);
    System.out.println(All.coins);
    System.out.println(All.toString());
    System.out.println(All.direction(4));
  }
}
