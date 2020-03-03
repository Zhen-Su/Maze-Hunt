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
        update(startX,startY);
    }

    public void update(int pX, int pY ) {
    	//if origin is in the middle, then will work out since anything left will be negative
    	//System.out.println(pX + " , " + pY);
        cam.position.x = pX ;
        cam.position.y = pY;
       // System.out.println(cam.position.x + " , " + cam.position.y);
        cam.update();
    }

    public void reset() {
        cam.viewportWidth = MazeGame.WIDTH;
        cam.viewportHeight = MazeGame.HEIGHT;
    }
}