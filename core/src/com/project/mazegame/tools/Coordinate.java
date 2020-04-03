package com.project.mazegame.tools;
/**
 * Class to represent two numbers together
 * @author James
 * @author Charlotte
 */
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
    /**
     * gets the x part of the coordinate
     * @return int x
     */
	public int getX() {
		return x;
	}

    /**
     * gets y part of the coordinate
     * @return int y
     */
	public int getY() {
		return y;
	}
    /**
     * changes the x coordinate by adding it
     * @param x
     */
	public void changeX(int x) {
		this.x += x;
	}
    /**
     * changes the y coordinate by adding it
     * @param y
     */
	public void changeY(int y) {
		this.y += y;
	}
	/**
	 * sets the x coordinate
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;

	}
	/**
	 * sets the y coordinate
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;

	}
    /**
     * prints the coordinate as a string
     * @return string
     */
	public String toString() {
		return "(" + x + " , " + y + ")" ;
	}
	/**
	 * checks two coordinates are the same
	 * @param p
	 * @return boolean
	 */
	public boolean same(Coordinate p) {
		return this.x == p.x && this.y == p.y;
	}
	/**
	 * checks two coordiantes are the same
	 * @param p
	 * @param x
	 * @return boolean
	 */
	public static boolean same(Coordinate p, Coordinate x) {
		return x.x == p.x && x.y == p.y;
	}
}
