package com.project.mazegame.tools;
import com.badlogic.gdx.ApplicationAdapter;
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
   
   public AnimationTool(int width, int height, Player player , Texture img, boolean loop) {
	   this.loop = loop;
	   this.img = img;
	   this.width = width;
	   this.height = height;
	   this.player = player;
	   this.xOffset = 0;
	   this.yOffset = 0;
   }
   
   
   @Override
   public void create (){
      batch = player.getSpriteBatch();
      this.img = player.getFrames();

      TextureRegion[][] tmpFrames = TextureRegion.split(img,width,height);
      animationFrames = new TextureRegion[4];
      int index = 0;
      for (int i = 0; i < 2; i++){
         for(int j = 0; j < 2; j++) {
            animationFrames[index++] = tmpFrames[i][j];
         }
      }
      this.animation = new Animation(1f/6f,this.animationFrames);
   }
   
   @Override
   public void render () {
	   	  //draws the animation based on the frames for this animation
	   	  elapsedTime += Gdx.graphics.getDeltaTime();
	   	  batch = this.player.getSpriteBatch();
	      batch.draw((TextureRegion)this.animation.getKeyFrame(elapsedTime,this.loop), player.position.getX() + this.xOffset - this.width/2,player.position.getY() - this.height/2 + this.yOffset);
//	      if (this.loop == false) {
//	    	  System.out.println(elapsedTime % 1);
//	    	 // if(elapsedTime % 1 < 0.02  ) System.out.println(elapsedTime);
//	      }
	      
	     
   }
 
}