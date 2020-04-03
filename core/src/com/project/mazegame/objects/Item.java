package com.project.mazegame.objects;

//import com.project.mazegame.Pair;
import com.project.mazegame.tools.Coordinate;

/**
 * The Item object is used to generate the collectibles on the map and for the player to pick them up. Once a collectible has been picked up it is no longer an Item object
 * @author Selma Kander
 *
 */
public class Item {
	private String type;
	private int x;
	private int y;
	private Coordinate itemPosition = new Coordinate ();
	private float forNowIdkTime;
	private float initialisedTime;

	public Item(String type, Coordinate itemPosition) { 
		this.type = type;
		this.itemPosition = itemPosition;
		if (type == "shield") {
			forNowIdkTime = 60;
		}
	}

	public void setInitialisedTime(float time) {
		this.initialisedTime = time;
	}

	public void setType(String type) {
		this.type = type;
	}
	public float getInitialisedTime() {
		return this.initialisedTime;
	}

	public String getType() {
		return type;
	}

	public void setPosition(Coordinate itemPosition) {
		this.itemPosition = itemPosition;
	}

	public Coordinate getPosition() {
		return itemPosition;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}


}
