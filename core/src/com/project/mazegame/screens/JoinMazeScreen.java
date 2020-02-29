package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.project.mazegame.MazeGame;


public class JoinMazeScreen implements Screen {

    private MazeGame game;

    public String ip;
    public String username;

    private boolean hasEnterIP = false;
    private boolean hasEnterUsername =false;

    public JoinMazeScreen(MazeGame game) {
        this.game = game;
        this.playerEnterIp();
    }

    private void playerEnterIp(){
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                Gdx.app.log("IP:",text);
                ip=text;
                hasEnterIP=true;
                if(hasEnterIP){
                    playerEnterUsername();
                }
            }
            @Override
            public void canceled() {
                Gdx.app.log("MutiPlayerScreen: ","Player cancel,GOODBYE!");
                //backToMenuScreen();
            }
        },"ENTER IP", "", "Your IP Please");
    }

    private void playerEnterUsername (){
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                Gdx.app.log("Username:",text);
                username=text;
                hasEnterUsername=true;
            }
            @Override
            public void canceled() {
                Gdx.app.log("MutiPlayerScreen: ","Player cancel,GOODBYE!");

            }
        },"ENTER USERNAME", "", "Your username Please");
    }

    //----------------------------------------------------------------------------------------------
    //Override method
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(hasEnterUsername){
            game.setScreen(new MultiPlayerGameScreen(game,username,ip));
        }
        this.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
