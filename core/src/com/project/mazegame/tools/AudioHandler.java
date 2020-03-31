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
    private Music logoBGM;
    private Sound attackSound;
    private Sound hitSound;
    private Sound shieldSound;
    private Sound stepSound;
    private Sound pickupCoinSound;
    private Sound addHealthSound;
    private Sound chooseButton;
    private Sound gearEnchantment;
    private Sound poison;
    private Music endBgm;

    private String theScreen;

    private boolean musicOn;
    private boolean sfxOn;

    private int volume=50;

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
        logoBGM = Assets.manager.get(Assets.logoBgm,Music.class);
        endBgm = Assets.manager.get(Assets.endBgm,Music.class);

        attackSound = Assets.manager.get(Assets.atkSFX, Sound.class);
        hitSound = Assets.manager.get(Assets.hitSFX, Sound.class);
        shieldSound = Assets.manager.get(Assets.shieldSFX, Sound.class);
        stepSound = Assets.manager.get(Assets.stepSFX, Sound.class);
        pickupCoinSound = Assets.manager.get(Assets.pickupCoinSFX, Sound.class);
        addHealthSound = Assets.manager.get(Assets.addHealthSFX, Sound.class);
        chooseButton = Assets.manager.get(Assets.chooseButtonSFX,Sound.class);
        gearEnchantment=Assets.manager.get(Assets.gearEnchantment,Sound.class);
        poison=Assets.manager.get(Assets.poison,Sound.class);

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
            case "logo":
                logoBGM.stop();
                break;
            case "end":
                endBgm.stop();
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
            case "logo":
                logoBGM.setLooping(true);
                logoBGM.play();
                logoBGM.setLooping(false);
            case "end":
                endBgm.stop();
                endBgm.setLooping(false);
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

    public void poison(){
        if (sfxOn)
            poison.play(volume);
    }

    public void gearEnchantment(){
        if (sfxOn)
            gearEnchantment.play(volume);
    }

    public void pickupCoin(){
        if (sfxOn)
            pickupCoinSound.play(volume);
    }

    public void choose(){
        if(sfxOn) {
//            chooseButton.play(volume);
            long id = chooseButton.play(1.0f);
            chooseButton.setLooping(id,false);
        }
    }

    public void addHealth(){
        if(sfxOn){
            addHealthSound.play(volume);
        }
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
