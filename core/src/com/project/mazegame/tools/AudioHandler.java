package com.project.mazegame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import org.junit.Assert;

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

    public void setSFXoff() {
        sfxOn = false;
    }

    public void setSFXOn() {
        sfxOn = true;
    }

    public void setCurrentScreen(String screen) {
        theScreen = screen;
        if (musicOn) {
            setMusicOn();
        } else {
            setMusicOff();
        }
    }

    public void step() {
        if (sfxOn)
            stepSound.play(volume);
    }

    public void atk() {
        if (sfxOn)
            attackSound.play(volume);
    }

    public void def() {
        if (sfxOn)
            shieldSound.play(volume);
    }

    public void hit(){
        if (sfxOn)
            hitSound.play();
    }

    public boolean isSoundOn() {
        return sfxOn && musicOn;
    }

    public boolean isMusicOn() {
        return musicOn;
    }
}
