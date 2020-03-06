package com.project;
public class Item {
	private String type;
	private int x;
	private int y;
	private Pair itemPosition = new Pair (x,y);

	public Item(String type, Pair itemPosition) {
		this.type = type;
		this.itemPosition = itemPosition;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setPosition(Pair itemPosition) {
		this.itemPosition = itemPosition;
	}

	public Pair getPosition() {
		return itemPosition;
	}

	//public int getX() {
	//	return x;
	//}

	//public int getY() {
	//	return y;
	//}


}
