package com.project.mazegame.tools;

/**
 * <h1> Coordinate Class </h1>
 * Class for handling x and y coordinate system
 * @author James Bartlett
 *
 */
public class Coordinate {
	private int x;
	private int y;

	/**
	 * makes a coordinate
	 * @param x
	 * @param y
	 */
	public Coordinate (int x,int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * makes coordinates at (0,0)
	 */
	public Coordinate () {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * getX
	 * gets the x part of the coordinate
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * gets the y part of the coordinate
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * changes the x by adding it
	 * @param x
	 */
	public void changeX(int x) {
		this.x += x;
	}

	/**
	 * changes the y by adding it
	 * @param y
	 */
	public void changeY(int y) {
		this.y += y;
	}

	/**
	 * sets the x to whatever you want
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
		
	}

	/**
	 * rsets the y to whatever you want
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
		
	}

	/**
	 * displays the coordinate as a string
	 * @return string of coordinate
	 */
	public String toString() {
		return "(" + x + " , " + y + ")" ;
	}

	/**
	 * checks if the given coordinate is on the same space
	 * @param p takes in the coordinate
	 * @return
	 */
	public boolean same(Coordinate p) {
		return this.x == p.x && this.y == p.y;
	}

}