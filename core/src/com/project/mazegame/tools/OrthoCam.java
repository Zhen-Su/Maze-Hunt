package com.project.mazegame.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.project.mazegame.MazeGame;

import static com.project.mazegame.tools.Variables.*;


public class OrthoCam {
    public OrthographicCamera cam;

    public OrthoCam(MazeGame game, boolean yDown, int width, int height, float startX, float startY) {
        cam = new OrthographicCamera();
        cam.setToOrtho(yDown, width, height);
        game.batch.setProjectionMatrix(cam.combined);
        update(startX,startY);
    }

    public void update(float pX, float pY) {
        cam.position.x = pX + CAMERA_X;
        cam.position.y = pY + CAMERA_Y;

        cam.update();
    }

    public void reset() {
        cam.viewportWidth = MazeGame.WIDTH;
        cam.viewportHeight = MazeGame.HEIGHT;
    }
}
