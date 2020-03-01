package com.project.mazegame.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.project.mazegame.MazeGame;
import com.project.mazegame.objects.Player;

import static com.project.mazegame.tools.Variables.*;



public class OrthoCam {
    public OrthographicCamera cam;
  
    public OrthoCam(MazeGame game, boolean yDown, int width, int height, int startX, int startY ) {
        cam = new OrthographicCamera();
        cam.setToOrtho(yDown, width, height);
        game.batch.setProjectionMatrix(cam.combined);
        System.out.println("first update");
        update(startX,startY,game);
    }

    public void update(int pX, int pY,MazeGame game ) {
    	//if origin is in the middle, then will work out since anything left will be negative
    	//System.out.println(pX + " , " + pY);
        cam.position.set(pX, pY, 0);
//        System.out.println(cam.position.x + " , " + cam.position.y);
//        cam.lookAt(pX, pY, 1);
        // cam.position.y = pY;
        //   System.out.println(cam.position.x + " , " + cam.position.y);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

    }

    public void reset() {
        cam.viewportWidth = MazeGame.WIDTH;
        cam.viewportHeight = MazeGame.HEIGHT;
    }
}