package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Server.GameServer;


public class CreateMazeScreen implements Screen{

    private MazeGame game;

    public String ip = "127.0.0.1";
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

    @Override
    public void show()
    {

    }



    @Override
    public void render(float delta)
    {
        if(hasEnterUsername){
            game.setScreen(new MultiPlayerGameScreen(game,username,ip));
            new GameServer().start();

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
