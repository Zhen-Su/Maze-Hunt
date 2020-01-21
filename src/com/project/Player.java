package com.project;
import java.util.ArrayList;
public class Player {
  int health;
  int coins;
  String name;
  ArrayList<String> items;

  public Player(String name) {
    this.health = 5;
    this.coins = 0;
    this.name = name;
    this.items = new ArrayList<>();
  }
  public void decreaseHealth() {
    this.health--;
  }
  public void generateHealth() {
    this.health++;
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
  }
  public String toString() {
    return "Name: " + this.name + " Health: " + this.health + " Coins: " + this.coins + " Items " + this.items;
  }

}
