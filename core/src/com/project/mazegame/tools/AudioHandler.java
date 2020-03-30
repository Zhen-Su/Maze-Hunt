package com.project.mazegame.tools;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Class AudioHandler written by: Andin
 * Use: to control music and sound preferences
 */
public class AudioHandler {
    private Music menuBGM;
    private Music gameBGM;
    private Sound attackSound;
    private Sound hitSound;
    private Sound shieldSound;
    private Sound stepSound;
    private Sound pickupCoinSound;
    private Sound addHealthSound;


    private String theScreen;

    private boolean musicOn;
    private boolean sfxOn;

    private int volume;

    /**
     * Constructor imports all sounds loaded on the Assets class
     * Only needs to be called once in the MazeGame class
     * important!: declare AudioHandler after all assets been loaded else it'll give null pointer exceptions
     */
    public AudioHandler() {
        this.musicOn = true;
        this.theScreen = "none";

        /**
         * BGM made by Andin
         */
        menuBGM = Assets.manager.get(Assets.menuBgm, Music.class);
        gameBGM = Assets.manager.get(Assets.mainBgm, Music.class);
        attackSound = Assets.manager.get(Assets.atkSFX, Sound.class);
        hitSound = Assets.manager.get(Assets.hitSFX, Sound.class);
        shieldSound = Assets.manager.get(Assets.shieldSFX, Sound.class);
        stepSound = Assets.manager.get(Assets.stepSFX, Sound.class);
        pickupCoinSound = Assets.manager.get(Assets.pickupCoinSFX, Sound.class);
        addHealthSound = Assets.manager.get(Assets.addHealthSFX, Sound.class);
    }

    /**
     * Turns music off and sets the boolean musicOn to false
     */
    public void setMusicOff() {
        musicOn = false;
        switch (theScreen) {
            case "menu":
                menuBGM.stop();
                break;
            case "game":
                gameBGM.stop();
                break;
        }
    }

    /**
     * Turns music on and sets the boolean musicOn to true
     */
    public void setMusicOn() {
        musicOn = true;
        switch (theScreen) {
            case "menu":
                menuBGM.setLooping(true);
                menuBGM.play();
                break;
            case "game":
                gameBGM.setLooping(true);
                gameBGM.play();
                break;
            default:
                break;
        }
    }

    /**
     * Sets boolean sfxOn to false
     */
    public void setSFXoff() {
        sfxOn = false;
    }

    /**
     * Sets boolean sfxOn to true
     */
    public void setSFXOn() {
        sfxOn = true;
    }

    /**
     * Tells the AudioHandler to know which music to load depending on the state of the screen
     * Sets theScreen string to the current string
     * @param screen current screen
     */
    public void setCurrentScreen(String screen) {
        theScreen = screen;
        if (musicOn) {
            setMusicOn();
        } else {
            setMusicOff();
        }
    }

    /**
     * Sound when moving
     */
    public void step() {
        if (sfxOn)
            stepSound.play(volume);
    }

    /**
     * Sound when attacking
     */
    public void atk() {
        if (sfxOn)
            attackSound.play(volume);
    }

    /**
     * Sound when defending (shield on)
     */
    public void def() {
        if (sfxOn)
            shieldSound.play(volume);
    }

    /**
     * Sound when getting hit by another character
     */
    public void hit(){
        if (sfxOn)
            hitSound.play();
    }

    /**
     * Gets the result of the && operator on sfxOn and musicOn
     * @return Boolean
     */
    public boolean isSoundOn() {
        return sfxOn && musicOn;
    }

    /**
     * Gets the current boolean value of musicOn
     * @return Boolean of musicOn
     */
    public boolean isMusicOn() {
        return musicOn;
    }
}
