package com.project;
public class Pair {
  int x;
  int y;
  public Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public String toString() {
    return "("+x+", " + y +")";
  }
  public void changeX(int x) {
  this.x += x;
}
  public void changeY (int y) {
    this.y += y;
  }
}
