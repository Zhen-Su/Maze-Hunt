package com.project.mazegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Client.GameClient;
import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.networking.Server.GameServer;
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
    private String ip;
    private String username;
    private boolean hasEnterIP=false;
    private Player myPlayer;
    private GameServer gameServer;
    private GameClient gameClient = new GameClient();
    private NetClient netClient = new NetClient(gameClient);


    public MultiPlayerScreen(MazeGame game) {
        this.game = game;
        playerEnterIp();
//        try {
//            //if don't have server, then this player enter own ip,and this player will be a server.
//            System.out.println("Player enter ip is: "+ip);
//            if(ip == InetAddress.getLocalHost().getHostAddress()){
//                GameServer gameServer=new GameServer();
//                gameServer.start();
//                netClient.connect(ip,GameServer.SERVER_TCP_PORT);
//            }else{
//                //if we have a server,then this player enter server ip,then connect to server.
//                netClient.connect(ip,GameServer.SERVER_TCP_PORT);
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.dispose();
        game.setScreen(new MenuScreen(game));
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
            }
        },"ENTER IP", "", "Your IP Please");
    }

    private void playerEnterUsername (){
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                System.out.println("username: "+text);
                username=text;
            }
            @Override
            public void canceled() {
                System.out.println("Player cancel,GOOD_BYE");
            }
        },"ENTER USERNAME", "", "Your username Please");
    }
}
