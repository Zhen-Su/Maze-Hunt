package com.project.mazegame.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
    public static final String JoinMaze = "UI/Titles/JoinMaze.png";
    public static final String audioOn = "UI/MenuButtons/audioOn.png";
    public static final String audioOff = "UI/MenuButtons/audioOff.png";
    public static final String audioSFXOff = "UI/MenuButtons/audioSFXOff.png";
    public static final String audioSFXOn = "UI/MenuButtons/audioSFXOn.png";
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
    public static final String map2Icon = "Maps/Map2Icon.png";
    public static final String map3Icon = "Maps/Map3Icon.png";
    public static final String GAMEOVER = "UI/Titles/GAMEOVER!.png";
    public static final String BackToMenuButton = "UI/MenuButtons/BackToMenuButton.png";
    public static final String backToMenuButtonPressed = "UI/MenuButtons/backToMenuButtonPressed.png";
    public static final String Leaderboard = "UI/Backgrounds/Leaderboard.png";
    public static final String LeaderboardBigger = "UI/Backgrounds/LeaderboardBigger.png";
    public static final String LeaderboardButton = "UI/MenuButtons/LeaderboardButton.png";
    public static final String LeaderboardButtonPressed = "UI/MenuButtons/LeaderboardButtonPressed.png";

    public static final String walkRightBlue = "Player/walkRightBlue.png";
    public static final String walkLeftBlue ="Player/walkLeftBlue.png";
    public static final String walkUpBlue = "Player/walkUpBlue.png";
    public static final String walkDownBlue="Player/walkDownBlue.png";
    public static final String walkRightGreen="Player/walkRightGreen.png";
    public static final String walkLeftGreen= "Player/walkLeftGreen.png";
    public static final String walkUpGreen="Player/walkUpGreen.png";
    public static final String walkDownGreen="Player/walkDownGreen.png";
    public static final String walkRightPink="Player/walkRightPink.png";
    public static final String walkLeftPink ="Player/walkLeftPink.png";
    public static final String walkUpPink="Player/walkUpPink.png";
    public static final String walkDownPink="Player/walkDownPink.png";
    public static final String walkRightOrange="Player/walkRightOrange.png";
    public static final String walkLeftOrange="Player/walkLeftOrange.png";
    public static final String walkUpOrange="Player/walkUpOrange.png";
    public static final String walkDownOrange="Player/walkDownOrange.png";
    public static final String walkRightLilac="Player/walkRightLilac.png";
    public static final String walkLeftLilac="Player/walkLeftLilac.png";
    public static final String walkUpLilac="Player/walkUpLilac.png";
    public static final String walkDownLilac="Player/walkDownLilac.png";
    public static final String walkRightYellow="Player/walkRightYellow.png";
    public static final String walkLeftYellow="Player/walkLeftYellow.png";
    public static final String walkUpYellow="Player/walkUpYellow.png";
    public static final String walkDownYellow="Player/walkDownYellow.png";
    public static final String walkRight="Player/walkRight.png";
    public static final String walkLeft="Player/walkLeft.png";
    public static final String walkUp="Player/walkUp.png";
    public static final String walkDown="Player/walkDown.png";

    public static final String coinPick="Collectibles/coinAnimation.png";
    public static final String swordAttack="Collectibles/swordAttack.png";
    public static final String swordNotAttack="Collectibles/sword2.png";
    public static final String swipeRight="Player/swipeRight.png";
    public static final String swipeLeft="Player/swipeLeft.png";
    public static final String swipeUp="Player/swipeUp.png";
    public static final String swipeDown="Player/swipeDown.png";
    public static final String playerDying="Player/playerDying.png";
    public static final String font = "myFont.fnt";
    public static final String enterBox = "UI/EnterBox.png";

    public static final String playerRed = ("Player/playerRed.png");
    public static final String playerBlue = ("Player/playerBlue.png");
    public static final String playerGreen = ("Player/playerGreen.png");
    public static final String playerLilac = ("Player/playerLilac.png");
    public static final String playerOrange = ("Player/playerOrange.png") ;
    public static final String playerPink = ("Player/playerPink.png");
    public static final String playerYellow = ("Player/playerYellow.png");

    public static final String waitingForPlayers = "UI/Titles/Waitingforplayers....png";
    public static final String endAnimation =  "Player/endAnimation.png";

    public static final String playButton =  "UI/MenuButtons/playButton.png";
    public static final String playButtonPressed =  "UI/MenuButtons/playButtonPressed.png";

    public static final String menuBgm = "sounds/menuBgm.mp3"; // BGM for main menu
    public static final String mainBgm = "sounds/mainbgm.mp3"; // BGM for game screens
    public static final String logoBgm ="sounds/logoBgm.mp3";  //BGM for logo screens
    public static final String endBgm ="sounds/endBgm.mp3";  //BGM for end screens
    public static final String hitSFX = "sounds/sfx/hit.mp3"; // SFX when getting hit
    public static final String atkSFX = "sounds/sfx/atk.mp3"; // SFX when attacking
    public static final String addHealthSFX = "sounds/sfx/addHealth.mp3"; // SFX when health is added (a.k.a consuming potion)
    public static final String pickupCoinSFX = "sounds/sfx/pickupCoin.mp3"; // SFX when coin is picked up
    public static final String shieldSFX = "sounds/sfx/shield.mp3"; // SFX when getting hit with shield on
    public static final String stepSFX = "sounds/sfx/step.mp3"; // SFX when player moving
    public static final String chooseButtonSFX = "sounds/sfx/chooseButton.mp3";
    public static final String gearEnchantment = "sounds/sfx/gearEnchantment.mp3";
    public static final String poison = "sounds/sfx/poison.mp3";
    public static final String attacked = "sounds/sfx/attacked.mp3";
    public static final String killed = "sounds/sfx/killed.mp3";



    public Assets (){
        load();
    }


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

        manager.load(JoinMaze, Texture.class);

        manager.load(StartNewMazeButton,Texture.class);
        manager.load(StartNewMazeButtonPressed,Texture.class);
        manager.load(MazeHunt,Texture.class);

        manager.load(audioOn,Texture.class);
        manager.load(audioOff,Texture.class);

        manager.load(audioSFXOn, Texture.class);
        manager.load(audioSFXOff, Texture.class);

        manager.load(menuBackground,Texture.class);
        manager.load(enterBox,Texture.class);


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
        manager.load(LeaderboardBigger,Texture.class);
        manager.load(LeaderboardButton,Texture.class);
        manager.load(LeaderboardButtonPressed,Texture.class);

        //player's texture
        manager.load(walkRightBlue,Texture.class);
        manager.load(walkLeftBlue,Texture.class);
        manager.load(walkUpBlue,Texture.class);
        manager.load(walkDownBlue,Texture.class);
        manager.load(walkRightGreen,Texture.class);
        manager.load(walkLeftGreen,Texture.class);
        manager.load(walkUpGreen,Texture.class);
        manager.load(walkDownGreen,Texture.class);
        manager.load(walkRightPink,Texture.class);
        manager.load(walkLeftPink,Texture.class);
        manager.load(walkUpPink,Texture.class);
        manager.load(walkDownPink,Texture.class);
        manager.load(walkRightOrange,Texture.class);
        manager.load(walkLeftOrange,Texture.class);
        manager.load(walkUpOrange,Texture.class);
        manager.load(walkDownOrange,Texture.class);
        manager.load(walkRightLilac,Texture.class);
        manager.load(walkLeftLilac,Texture.class);
        manager.load(walkUpLilac,Texture.class);
        manager.load(walkDownLilac,Texture.class);
        manager.load(walkRightYellow,Texture.class);
        manager.load(walkLeftYellow,Texture.class);
        manager.load(walkUpYellow,Texture.class);
        manager.load(walkDownYellow,Texture.class);
        manager.load(walkRight,Texture.class);
        manager.load(walkLeft,Texture.class);
        manager.load(walkUp,Texture.class);
        manager.load(walkDown,Texture.class);

        manager.load(coinPick,Texture.class);
        manager.load(swordAttack,Texture.class);
        manager.load(swordNotAttack,Texture.class);
        manager.load(swipeRight,Texture.class);
        manager.load(swipeLeft,Texture.class);
        manager.load(swipeDown,Texture.class);
        manager.load(swipeUp,Texture.class);
        manager.load(playerDying,Texture.class);

        manager.load(font, BitmapFont.class);
        manager.load(waitingForPlayers , Texture.class);

        manager.load(playerRed , Texture.class);
        manager.load(playerBlue , Texture.class);
        manager.load(playerGreen , Texture.class);
        manager.load(playerLilac , Texture.class);
        manager.load(playerOrange , Texture.class);
        manager.load(playerPink , Texture.class);
        manager.load(playerYellow , Texture.class);
        manager.load(playButton , Texture.class);
        manager.load(playButtonPressed , Texture.class);

        manager.load(endAnimation , Texture.class);

        manager.load(menuBgm, Music.class);
        manager.load(mainBgm, Music.class);
        manager.load(endBgm, Music.class);
        manager.load(logoBgm, Music.class);
        manager.load(hitSFX, Sound.class);
        manager.load(atkSFX, Sound.class);
        manager.load(addHealthSFX, Sound.class);
        manager.load(pickupCoinSFX, Sound.class);
        manager.load(shieldSFX, Sound.class);
        manager.load(stepSFX, Sound.class);
        manager.load(chooseButtonSFX,Sound.class);
        manager.load(gearEnchantment,Sound.class);
        manager.load(poison,Sound.class);
        manager.load(attacked,Sound.class);
        manager.load(killed,Sound.class);



        manager.finishLoading();


    }
    public static void dispose() {
        manager.dispose();
    }
}
