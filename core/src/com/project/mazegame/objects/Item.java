package com.project.mazegame.objects;

//import com.project.mazegame.Pair;
import com.project.mazegame.tools.ItemCell;

public class Item {
	private String type;
	private int x;
	private int y;
	private ItemCell itemPosition = new ItemCell ();

	public Item(String type, ItemCell itemPosition) {
		this.type = type;
		this.itemPosition = itemPosition;
	}


	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setPosition(ItemCell itemPosition) {
		this.itemPosition = itemPosition;
	}

	public ItemCell getPosition() {
		return itemPosition;
	}

	//public int getX() {
	//	return x;
	//}

	//public int getY() {
	//	return y;
	//}


}
