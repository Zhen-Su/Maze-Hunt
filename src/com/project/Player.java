package com.project;
import java.util.ArrayList;
// import java.awt.KeyEvent;
// import java.awt.geom.Point2D;
public class Player {
  public boolean hasCoin;
  public boolean hasCompass;
  public boolean hasDamagingPotion;
  public boolean hasHealingPotion;
  public boolean hasShield;
  public boolean hasSword;

  public int coins;
  public int health;
  public int ID;
  public int swordDamage;

  public String name;
  public ArrayList<String> items;
  public Pair position;


  public Player(String name, int ID;) {
    this.health = 5;
    this.coins = 0;
    this.name = name;
    this.items = new ArrayList<>();
    // this.position = new Point(0, 0);
    this.position = new Pair(1, 2);
    this.swordDamage = 0;
    this.ID = ID;
  }
  public int getID() {
    return this.ID;
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
    System.out.println("Player has died respawning now");
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

  public void playerHitPlayer(Player hit) {
    // write boolean to check sword

    if (this.hasSword && !hit.hasShield) {
      // will need to do if have item then that can be called
      // then decrease the helath based on that
      // could have a damage do attribute and various attributes which change throught the generateMapItems
      hit.decreaseHealth(this.swordDamage);
      if (hit.health == 0) {
        this.swordDamage++;
        this.coins += hit.coins;
        hit.death();
      }
    }
    //need to add shield stuffr
  }

  public void playerKillAI(AIPlayer AI) {
    if (AI.health == 0) {
    this.pickUpCoins(5);
  } else {
    AI.decreaseHealth();
  }

  public void move(Pair coord) {
    this.position = coord;
  }

  public void changeXAndY(int x, int y) {
    this.poistion.getX() += x;
    this.position.getY() += y;
  }

  public boolean sameSpot(Player h) {
    return this.position.same(h.position);
  }



}
