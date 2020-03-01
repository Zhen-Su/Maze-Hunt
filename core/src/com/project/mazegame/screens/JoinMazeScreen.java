package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.InputHandler;
import com.project.mazegame.tools.OrthoCam;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;

public class JoinMazeScreen implements Screen {

    private MazeGame game;
    // private OrthoCam cam;
//    private Music bgm;

    public String ip;
    public String username;

    Texture backGround;

    private boolean hasEnterIP = false;
    private boolean hasEnterUsername =false;
//    private boolean imServer = false;
//    private boolean imClient = false;

    public JoinMazeScreen(MazeGame game) {
        backGround = new Texture("UI\\MenuButtons\\menuBackground.png");
        this.game = game;
        this.playerEnterIp();
        //  Gdx.app.log("MutiPlayerScreen: ","construction done!");
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
                //this.dispose();
                backToMenuScreen();
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
                backToMenuScreen();
            }
        },"ENTER USERNAME", "", "Your username Please");
    }

    private void backToMenuScreen(){
        System.out.println("back to");
        // cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
        //this.dispose();
        game.setScreen(new MenuScreen(this.game));
        System.out.println("shouldn't see");
    }
    //----------------------------------------------------------------------------------------------
    //Override method

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.325f, 0.343f, 0.604f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(hasEnterUsername){
            //this.dispose();
            game.setScreen(new MultiPlayerGameScreen(game,username,ip));
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

    }

    @Override
    public void dispose() {

    }
}
