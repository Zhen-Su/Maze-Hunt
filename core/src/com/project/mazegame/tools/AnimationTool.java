package com.project.mazegame.tools;
import com.badlogic.gdx.ApplicationAdapter;

import com.project.mazegame.objects.MultiPlayer;


import com.project.mazegame.objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.project.mazegame.MazeGame;
import com.project.mazegame.tools.Variables.*;
public class AnimationTool extends ApplicationAdapter {


    SpriteBatch batch;

    Texture img;
    Animation animation;
    public float elapsedTime;
    String fileName;
    int width,height;
    TextureRegion[] animationFrames;
    Player player;
    boolean loop;

    private MazeGame game;
    public int xOffset;
    public int yOffset;
    public int rows;
    public int columns;

    public AnimationTool(int width, int height, Player player , Texture img, boolean loop) {
        this.loop = loop;
        this.img = img;
        this.width = width;
        this.height = height;
        this.player = player;
        this.xOffset = 0;
        this.yOffset = 0;
        this.rows = 2;
        this.columns = 2;
    }

    public AnimationTool(int width, int height, Texture img, boolean loop) {
        this.loop = loop;
        this.img = img;
        this.width = width;
        this.height = height;
        this.player = null;
        this.xOffset = 0;
        this.yOffset = 0;
        this.rows = 2;
        this.columns = 2            ;
    }


    public void setFrames(TextureRegion[] frames) {
        this.animationFrames = frames;
    }

    public TextureRegion[] getFrames() {
        return animationFrames;
    }
    public void setImg(Texture img) {
        this.img = img;
    }
    public void setBatch(SpriteBatch sb) {
        this.batch = sb;
    }


    @Override
    public void create (){
        TextureRegion[][] tmpFrames ;
        if(this.player != null) {
            batch = player.getSpriteBatch();
            this.img = player.getFrames();

            tmpFrames = TextureRegion.split(this.img,this.width,this.height);
            animationFrames = new TextureRegion[4];
        }else {
            //need to set batch and img before calling render
            this.rows = 5;
            this.columns = 5  ;
            System.out.println("here");
            System.out.println(this.width + " , --  " + this.height);
            System.out.println(this.rows + " , --  " + this.columns );
            tmpFrames = TextureRegion.split(this.img,this.width,this.height);
            animationFrames = new TextureRegion[25];
        }

        int index = 0;
        int r = this.rows;
        int c = this.columns;
        for (int i = 0; i < this.rows; i++){
            System.out.println("next row");
            for(int j = 0; j < this.columns; j++) {
                System.out.println("next col");
                animationFrames[index++] = tmpFrames[i][j];
            }
        }
        this.animation = new Animation(1f/6f,this.animationFrames);
    }


    @Override
    public void render () {
        //draws the animation based on the frames for this animation
        System.out.println("got to render");
        elapsedTime += Gdx.graphics.getDeltaTime();
        System.out.println("get here11");
        if (this.player != null) {
            System.out.println("get here");
            this.rows = 2;
            this.columns = 2;
            batch = this.player.getSpriteBatch();
            System.out.println(this.width + " , " + this.height);
            batch.draw((TextureRegion)this.animation.getKeyFrame(elapsedTime,this.loop), player.position.getX() + this.xOffset - this.width/2,player.position.getY() - this.height/2 + this.yOffset);
        }
        else {
            System.out.println(this.width + " , --  " + this.height);
            batch.draw((TextureRegion)this.animation.getKeyFrame(elapsedTime,this.loop), 500 + this.xOffset - this.width/2,500 - this.height/2 + this.yOffset);

        }
    }


}
