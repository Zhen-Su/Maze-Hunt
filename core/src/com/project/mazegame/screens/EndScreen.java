package com.project.mazegame.screens;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.CSVStuff;
import com.project.mazegame.tools.Variables;

//import com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;
public class EndScreen implements Screen {

    private static final int LB_WIDTH = 350;
    private static final int LB_HEIGHT = 200;

    private static final int MB_WIDTH = 250;
    private static final int MB_HEIGHT = 100;

    private static final int SB_HEIGHT = 80;
    private static final int SB_WIDTH = 50;
    private static final int PLAY_Y = 100;
    
    private BitmapFont font;

    private MazeGame game;
    Texture backGround;
    Texture title;
    private Music bgm;
    Texture playButtonActive;
    Texture playButtonInactive;
    Texture leaderboard;
    ArrayList<String> output;

    public EndScreen(MazeGame game) {
        this.game = game;
        backGround = Assets.manager.get(Assets.menuBackground, Texture.class);
        title =  Assets.manager.get(Assets.GAMEOVER, Texture.class);
        playButtonActive = Assets.manager.get(Assets.BackToMenuButton, Texture.class);
        playButtonInactive = Assets.manager.get(Assets.backToMenuButtonPressed, Texture.class);
        leaderboard = Assets.manager.get(Assets.Leaderboard, Texture.class);
        
        
        bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds\\menuBgm.mp3"));
        bgm.setLooping(true);
        bgm.play();
        
        output = CSVStuff.readCSVFile("coinCSV");
        
        for(int i = 0 ; i <= output.size(); i ++) {
        	
        	
        	System.out.println("End" + output);
        }
        font = new BitmapFont((Gdx.files.internal("myFont.fnt")));
        font.setColor(Color.BLACK);
        font.getData().setScale(1f);
       
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(backGround,0,0,1000,1000);
        game.batch.draw(title,0,700,1000 , 300);
        game.batch.draw(leaderboard,250,200, 500 , 500);
        
        for(int i = 0 ;  i < output.size(); i ++) {
        	
            font.draw(game.batch,output.get(i) , 450 ,500 - (i*50));
        }
        
        

        int drawX = xMid("MB");
        if (isHovering(drawX, PLAY_Y, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(playButtonActive, drawX, PLAY_Y,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.isTouched()) {
                bgm.stop();
                game.setScreen(new MenuScreen(game));
            }
        } else {
            game.batch.draw(playButtonInactive, drawX, PLAY_Y,MB_WIDTH, MB_HEIGHT);
        }

        game.batch.end();
    }

    private boolean isHovering(int X, int  Y, int WIDTH, int HEIGHT) {
        if (Gdx.input.getX() < (X + WIDTH) && Gdx.input.getX() > X && MazeGame.HEIGHT - Gdx.input.getY() > Y && MazeGame.HEIGHT - Gdx.input.getY() < Y + HEIGHT)
            return true;
        return false;
    }

    private int xMid(String buttonSize) {
        switch (buttonSize) {
            case "LB":
                return MazeGame.WIDTH / 2 - LB_WIDTH / 2;
            case "MB":
                return MazeGame.WIDTH / 2 - MB_WIDTH / 2;
            case "SB":
                return MazeGame.WIDTH / 2 - SB_WIDTH / 2;
            default:
                return -10;
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
