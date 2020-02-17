package com.project.mazegame.tools;

import java.util.ArrayList;

import com.project.mazegame.objects.Item;

public class Variables {
	public static int V_WIDTH = 800;
    public static int V_HEIGHT = 400;
    public static int VIEWPORT_WIDTH =1000;// 500 ;
    public static int VIEWPORT_HEIGHT = 1000;// 600;
	  
	  public static boolean UP_TOUCHED;
	  public static boolean DOWN_TOUCHED;
	  public static boolean LEFT_TOUCHED;
	  public static boolean RIGHT_TOUCHED;
	  
	  public static float CAMERA_X;
	  public static float CAMERA_Y;
	  
	  public static ArrayList<Item> mapItems;
}
