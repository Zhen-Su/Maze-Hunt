package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Server.GameServer;
import com.project.mazegame.objects.Direction;
import com.project.mazegame.objects.MultiPlayer;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.InputHandler;
import com.project.mazegame.tools.OrthoCam;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.project.mazegame.tools.Variables.VIEWPORT_HEIGHT;
import static com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;

public class MultiPlayerScreen implements Screen {

    private MazeGame game;
    private OrthoCam cam;
    private Music bgm;

    public String ip;
    public String username;

    private boolean hasEnterIP = false;
    private boolean hasEnterUsername =false;
//    private boolean imServer = false;
//    private boolean imClient = false;

    public MultiPlayerScreen(MazeGame game) {
        this.game = game;
        this.playerEnterIp();
        System.out.println("construction done!");
    }

    private void playerEnterIp(){
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                System.out.println("ip: "+text);
                ip=text;
                hasEnterIP=true;
                if(hasEnterIP){
                    playerEnterUsername();
                }
            }
            @Override
            public void canceled() {
                System.out.println("Player cancel,GOOD_BYE");
                //backToMenuScreen();
            }
        },"ENTER IP", "", "Your IP Please");
    }

    private void playerEnterUsername (){
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                System.out.println("username: "+text);
                username=text;
                hasEnterUsername=true;
            }
            @Override
            public void canceled() {
                System.out.println("Player cancel,GOOD_BYE");
               // backToMenuScreen();
            }
        },"ENTER USERNAME", "", "Your username Please");
    }

    private void backToMenuScreen(){
       // cam = new OrthoCam(game, false, MazeGame.WIDTH,MazeGame.WIDTH,0,0);
        this.dispose();
        game.setScreen(new MenuScreen(this.game));
    }
    //----------------------------------------------------------------------------------------------
    //Override method

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
