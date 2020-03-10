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
   float elapsedTime;
   String fileName;
   int width,height;
   TextureRegion[] animationFrames;
   Player player;
   boolean loop;
   
   private MazeGame game;
   
   public AnimationTool(int width, int height, Player player , Texture img, boolean loop) {
	   this.loop = loop;
	   this.img = img;
	   this.width = width;
	   this.height = height;
	   this.player = player;
	   
   }
   
   
   @Override
   public void create (){
	  System.out.println("ani created");
      batch = player.getSpriteBatch();
      img = player.getFrames();

      TextureRegion[][] tmpFrames = TextureRegion.split(img,width,height);

      animationFrames = new TextureRegion[4];
      int index = 0;

      for (int i = 0; i < 2; i++){
         for(int j = 0; j < 2; j++) {
            animationFrames[index++] = tmpFrames[j][i];
         }
      }
      
      animation = new Animation(1f/6f,animationFrames);
      System.out.println(img.toString());
      
   }
   
   @Override
   public void render () {
	  
	   	  elapsedTime += Gdx.graphics.getDeltaTime();
	   	  batch = player.getSpriteBatch();
	   	  
	      batch.draw((TextureRegion)animation.getKeyFrame(elapsedTime,this.loop),player.position.getX() - width/2,player.position.getY() - height/2);
	     
   }
 
}