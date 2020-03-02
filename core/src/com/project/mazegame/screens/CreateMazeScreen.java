package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class CreateMazeScreen implements Screen {

    private MazeGame game;
    public String username;
    private boolean hasEnterUsername =false;

    public CreateMazeScreen(MazeGame game) {
        this.game = game;
        this.playerEnterUsername();
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


    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(hasEnterUsername){
            try {
                Gdx.app.log("Server","I'm a server!");
                Gdx.app.log("Server IP:", InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            new Thread(new GameServer()).start();

            game.setScreen(new MultiPlayerGameScreen(game,username,"127.0.0.1"));

        }
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
        this.dispose();
    }

    @Override
    public void dispose() {

    }
}


