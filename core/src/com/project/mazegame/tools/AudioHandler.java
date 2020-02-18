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
    private boolean mutedMusic;
    private int volume;

    public AudioHandler(String theScreen, boolean muted, int volume) {
        this.mutedMusic = muted;
        this.theScreen = theScreen;
        this.volume = volume;
        if (!mutedMusic) {
            switch (theScreen) {
                case "menu":
                    menuBGM = Gdx.audio.newMusic(Gdx.files.internal("sounds\\menuBgm.mp3"));
                    menuBGM.setLooping(true);
                    menuBGM.play();

                    break;
                case "game":
                    gameBGM = Gdx.audio.newMusic(Gdx.files.internal("sounds\\gameBgm.mp3"));
                    gameBGM.setLooping(true);
                    gameBGM.play();

                    attackSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\atk.mp3"));
                    hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\hit.mp3"));
                    shieldSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\shield.mp3"));
                    stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\step.mp3"));

                    break;
                default:
                    break;
            }
        }

    }

    public void stopBGM() {
        switch (theScreen) {
            case "menu":
                menuBGM.stop();
                break;
            case "game":
                gameBGM.stop();
                break;
        }
    }

    public void step() {
        stepSound.play(volume);
    }

    public void atk() {
        attackSound.play(volume);
    }

    public void def() {
        shieldSound.play(volume);
    }

    public void hit(){
        hitSound.play();
    }
}
