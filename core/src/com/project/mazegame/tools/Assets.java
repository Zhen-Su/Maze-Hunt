package com.project.mazegame.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static final AssetManager manager= new AssetManager();

	
	
	public static final String playSoloButton = "UI/MenuButtons/playSoloButton.png";
	public static final String playSoloButtonPressed = "UI/MenuButtons/playSoloButtonPressed.png";
	public static final String exit_button_active = "UI/MenuButtons/exit_button_active.png";
	public static final String exit_button_inactive = "UI/MenuButtons/exit_button_inactive.png";
	public static final String FindMazeButton = "UI/MenuButtons/FindMazeButton.png";
	public static final String FindMazeButtonPressed = "UI/MenuButtons/FindMazeButtonPressed.png";
	public static final String StartNewMazeButton = "UI/MenuButtons/StartNewMazeButton.png";
	public static final String StartNewMazeButtonPressed = "UI/MenuButtons/StartNewMazeButtonPressed.png";
	public static final String MazeHunt = "UI/Titles/MazeHunt.png";
	public static final String audioOn = "UI/MenuButtons/audioOn.png";
	public static final String audioOff = "UI/MenuButtons/audioOff.png";
	public static final String menuBackground = "UI/Backgrounds/menuBackground.png";
	public static final String heart = "Collectibles/heart.png";
	public static final String coin = "Collectibles/coin.png";
	public static final String shield = "Collectibles/shield.png";
	public static final String sword = "Collectibles/sword2.png";
	
	public static final String Potion = "Collectibles/Potion.png";
	public static final String Potion2 = "Collectibles/Potion2.png";
	public static final String Potion3 = "Collectibles/Potion3.png";
	public static final String RolledMap = "Collectibles/RolledMap.png";
	public static final String circularOverlay ="UI/circularOverlay.png";
	public static final String coinAnimation ="Collectibles/coinAnimation.png";
	public static final String minimapOutline = "Maps/minimapOutline.png";
	public static final String ENCHANTED = "Player/ENCHANTED.png";
	public static final String playerOnMap = "Player/playerOnMap.png";
	
	public static final String map1Icon = "Maps/Map1Icon.png";
	public static final String map2Icon = "Maps/Map1Icon.png";
	public static final String map3Icon = "Maps/Map1Icon.png";
	public static final String GAMEOVER = "UI/Titles/GAMEOVER!.png";
	public static final String BackToMenuButton = "UI/MenuButtons/BackToMenuButton.png";
	public static final String backToMenuButtonPressed = "UI/MenuButtons/backToMenuButtonPressed.png";
	public static final String Leaderboard = "UI/Backgrounds/Leaderboard.png";
//	public static final String GAMEOVER = "UI\\Titles\\GAMEOVER!.png";
	
	
	
	public static void load() {
//			
	         //-----------menu
			 System.out.println("loading");
	         manager.load(playSoloButton,Texture.class );
	         manager.load(playSoloButtonPressed,Texture.class);

	         manager.load(exit_button_active,Texture.class);
	         manager.load(exit_button_inactive,Texture.class);

	         manager.load(FindMazeButton,Texture.class);
	         manager.load(FindMazeButtonPressed,Texture.class);

	         manager.load(StartNewMazeButton,Texture.class);
	         manager.load(StartNewMazeButtonPressed,Texture.class);
	         manager.load(MazeHunt,Texture.class);

	         manager.load(audioOn,Texture.class);
	         manager.load(audioOff,Texture.class);

	         manager.load(menuBackground,Texture.class);
	         
	         
//	         manager.load(exit_button_active, Texture.class);
//			 manager.load("UI/MenuButtons/exit_button_inactive.png",Texture.class);
//		         manager.load("UI/MenuButtons/audioOn.png",Texture.class);
//		         manager.load("UI\\MenuButtons\\audioOff.png",Texture.class);
		         manager.load(heart,Texture.class);
		         manager.load(coin,Texture.class);
		         manager.load(sword,Texture.class);
		         manager.load(shield,Texture.class);
		         manager.load(Potion,Texture.class);
		         manager.load(Potion2,Texture.class);
		         manager.load(Potion3,Texture.class);
		         manager.load(RolledMap,Texture.class);
		         
		         manager.load(circularOverlay,Texture.class);
		         manager.load(coinAnimation,Texture.class);
		         manager.load(minimapOutline,Texture.class);
		         manager.load(ENCHANTED,Texture.class);
		         manager.load(playerOnMap,Texture.class);
		         
		         manager.load(map1Icon,Texture.class);
		         manager.load(map2Icon,Texture.class);
		         manager.load(map3Icon,Texture.class);
		         
		         manager.load(GAMEOVER,Texture.class);
		         manager.load(BackToMenuButton,Texture.class);
		         manager.load(backToMenuButtonPressed,Texture.class);
		         manager.load(Leaderboard,Texture.class);
		       
//		         
		         manager.finishLoading();
	         
	         System.out.println("added everything to queue");
	        
	}
	public static void dispose() {
		manager.dispose();
	}
}
