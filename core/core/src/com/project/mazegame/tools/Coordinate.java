package com.project.mazegame.tools;
public class Coordinate {
	private int x;
	private int y;
	public Coordinate (int x,int y) {
		this.x = x;
		this.y = y;
	}
	public Coordinate () {
		this.x = 0;
		this.y = 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) { this.x = x; }

	public void setY(int y) { this.y = y; }

	public void changeX(int x) {
		this.x += x;
	}

	public void changeY(int y) {
		this.y += y;
	}

	public String toString() {
		return "(" + x + " , " + y + ")" ;
	}

	public boolean same(Coordinate p) {
		return this.x == p.x && this.y == p.y;
	}

	public boolean same(Coordinate p, Coordinate x) {
		return x.x == p.x && x.y == p.y;
	}
}