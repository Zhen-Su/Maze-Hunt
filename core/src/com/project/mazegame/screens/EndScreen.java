package com.project.mazegame.screens;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.tools.Timer;
import com.project.mazegame.MazeGame;
import com.project.mazegame.networking.Database.AddData;
import com.project.mazegame.objects.Player;
import com.project.mazegame.tools.AnimationTool;
import com.project.mazegame.tools.Assets;
import com.project.mazegame.tools.CSVStuff;
import com.project.mazegame.tools.Variables;

//import com.project.mazegame.tools.Variables.VIEWPORT_WIDTH;
public class EndScreen implements Screen {
    private AddData add;


    private static final int LB_WIDTH = 350;
    private static final int LB_HEIGHT = 200;

    private static final int MB_WIDTH = 220;
    private static final int MB_HEIGHT = 70;

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
    Texture frames;
    AnimationTool end;
    Timer time = new Timer();
    String winningPlayer;
    AnimationTool UpAnim;

    Player playerWin, winner;

    int initialY = 300;
    boolean ifmulti;



    EndScreen(MazeGame game , Player winner, boolean ifmulti) {
        this.game = game;
        this.winner = winner;
        this.ifmulti = ifmulti;



        playerWin = new Player(this.winner.getCollisionLayer(), this.winner.getName(),this.winner.getID(),this.winner.getColour(),this.winner.playersType);

        backGround = Assets.manager.get(Assets.menuBackground, Texture.class);
        title =  Assets.manager.get(Assets.GAMEOVER, Texture.class);
        playButtonActive = Assets.manager.get(Assets.BackToMenuButton, Texture.class);
        playButtonInactive = Assets.manager.get(Assets.backToMenuButtonPressed, Texture.class);
        leaderboard = Assets.manager.get(Assets.Leaderboard, Texture.class);


        bgm = Gdx.audio.newMusic(Gdx.files.internal("sounds\\menuBgm.mp3"));
        bgm.setLooping(true);
        bgm.play();

        output = CSVStuff.readCSVFile("coinCSV");
        AddData add = new AddData();
        if (ifmulti)
        {
            add.update(output);
        }
        for(int i = 0 ; i <= output.size(); i ++) {

            System.out.println("End" + output);
        }

        ///assuming that player with most coins is top of list
        winningPlayer = output.get(0).split("=")[0];


        font = (Assets.manager.get(Assets.font));
        font.setColor(Color.BLACK);
        font.getData().setScale(1f);
        frames = Assets.manager.get(Assets.endAnimation , Texture.class);


        end = new AnimationTool(1000, 1000, frames, false);
        end.setImg(frames);
        end.setBatch(game.batch);

        end.rows = 5;
        end.columns = 5;
        end.setImg(frames);
        end.create();



    }



    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        time.updateTimer(delta);
        game.batch.begin();

        game.batch.draw(backGround,0,0,1000,1000);
        game.batch.draw(title,0,700,1000 , 300);
        game.batch.draw(leaderboard,150,100, 800 , 600);

        for(int i = 0 ;  i < output.size(); i ++) {

            font.draw(game.batch,output.get(i) , 450 ,450 - (i*50));
        }




        int drawX = xMid("MB");
        if (isHovering(drawX, PLAY_Y- 50, MB_WIDTH, MB_HEIGHT)) {
            game.batch.draw(playButtonActive, drawX, PLAY_Y - 50 ,MB_WIDTH, MB_HEIGHT);
            if (Gdx.input.isTouched()) {
                bgm.stop();
                game.setScreen(new MenuScreen(game));
            }
        } else {
            game.batch.draw(playButtonInactive, drawX, PLAY_Y -50,MB_WIDTH, MB_HEIGHT);
        }
        if ( time.currentTime() <= 3 ) {

            end.setImg(frames);
            end.setBatch(game.batch);

            end.render();


            playerWin.setAnimation(UpAnim);
            playerWin.update(delta,0,null,0);
            playerWin.position.setX(550);
            if(initialY <= 730)
                playerWin.position.setY(initialY += 3 );
            else playerWin.position.setY(initialY  );
            playerWin.render(game.batch);
            System.out.println(playerWin.position.getX() + " , " + playerWin.position.getY());



            font.draw(game.batch,  winningPlayer + " escapes the maze!", 200, 900);




        }

        game.batch.end();
    }

    private Texture getColour(String colour) {
        Assets.manager.finishLoading();
        switch (colour) {
            case "blue":
                return Assets.manager.get(Assets.walkUpBlue, Texture.class);

            case "green":
                return Assets.manager.get(Assets.walkUpGreen, Texture.class);

            case "pink":
                return Assets.manager.get(Assets.walkUpPink, Texture.class);

            case "orange":
                return Assets.manager.get(Assets.walkUpOrange, Texture.class);

            case "lilac":
                return Assets.manager.get(Assets.walkUpLilac, Texture.class);

            case "yellow":
                return Assets.manager.get(Assets.walkUpYellow, Texture.class);

            default:
                return Assets.manager.get(Assets.walkUp, Texture.class);
        }
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
