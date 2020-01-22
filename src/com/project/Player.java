package com.project;
import java.util.ArrayList;
// import java.awt.geom.Point2D;
public class Player {
  public int health;
  public int coins;
  public String name;
  public ArrayList<String> items;
  public Pair position;
  // Point position;

  public Player(String name) {
    this.health = 5;
    this.coins = 0;
    this.name = name;
    this.items = new ArrayList<>();
    // this.position = new Point(0, 0);
    this.position = new Pair(1, 2);
  }
  public void decreaseHealth() {

    this.health--;
    if (this.health == 0) {
      this.death();
    }
  }
  public void generateHealth() {
    if(health != 5) {
    this.health++;
    }
  }
  public void pickUpCoins(int coinPick) {
    this.coins += coinPick;
  }
  public void death() {
    this.health = 5;
    this.coins = 0;
    this.items = new ArrayList<>();
  }
  public void pickUpItem(String item) {
    this.items.add(item);
  }
  public void decreaseHealth(int number) {
    this.health -= number;
    if(health <= 0) {
      this.death();
    }
  }
  public String toString() {
    return "Name: " + this.name + " Health: " + this.health + " Coins: " + this.coins + " Items " + this.items + " Postion: " + position.toString();
  }

}
