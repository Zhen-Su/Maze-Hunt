package com.project.mazegame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioHandler {
    private Music menuBGM;
    private Music gameBGM;
    private Sound attackSound;
    private Sound hitSound;
    private Sound shieldSound;
    private Sound stepSound;
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
        menuBGM = Gdx.audio.newMusic(Gdx.files.internal("sounds\\menuBgm.mp3"));
//        gameBGM = Gdx.audio.newMusic(Gdx.files.internal("sounds\\gameBgm.mp3"));


        /**
         * SFX made by Andin
         */
//        attackSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\atk.mp3"));
//        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\hit.mp3"));
//        shieldSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\shield.mp3"));
//        stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\step.mp3"));
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
