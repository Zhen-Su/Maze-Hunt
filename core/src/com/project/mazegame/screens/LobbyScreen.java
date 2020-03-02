package com.project.mazegame.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class LobbyScreen implements Screen {

    private MazeGame game;
    String username;

    public LobbyScreen(MazeGame game, String username) {
        this.game = game;
        this.username = username;

    }



    //---------------------------------------Override-----------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        /*
        Add loading animation
        if all player is not loaded
            "Waiting for players"
            whilst waiting players
        else
            if data is not fully loaded
                "loading assets"
                loading data, sent through the server
            else
                startMultiPlayerGame();
         */

    }

    private void startMultiPlayerGame() {
        game.setScreen(new MultiPlayerGameScreen(game,username,"127.0.0.1"));
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


