package com.project.mazegame.objects;

//import com.project.mazegame.Pair;
import com.project.mazegame.tools.Coordinate;

public class Item {
	private String type;
	private int x;
	private int y;
	private Coordinate itemPosition = new Coordinate ();

	public Item(String type, Coordinate itemPosition) {
		this.type = type;
		this.itemPosition = itemPosition;
	}


	public void setType(String type) {
		this.type = type;
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

	//public int getX() {
	//	return x;
	//}

	//public int getY() {
	//	return y;
	//}


}