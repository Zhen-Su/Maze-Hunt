package com.project.mazegame.objects;

public class Pair {
    float x;
    float y;
    public Pair(float x, float y) {
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
    public float getX() {
        return this.x;
    }
    public float getY() {
        return this.y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public boolean same(Pair p) {
        return this.x == p.x && this.y == p.y;
    }
}


