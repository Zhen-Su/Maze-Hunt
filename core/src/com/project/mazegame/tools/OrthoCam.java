package com.project.mazegame.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.project.mazegame.MazeGame;

import static com.project.mazegame.tools.Variables.SCROLLTRACKER_X;
import static com.project.mazegame.tools.Variables.SCROLLTRACKER_Y;

public class OrthoCam {
    public OrthographicCamera cam;

    public OrthoCam(MazeGame game, boolean yDown, int width, int height, float startX, float startY) {
        cam = new OrthographicCamera();
        cam.setToOrtho(yDown, width, height);
        game.batch.setProjectionMatrix(cam.combined);
        update(startX,startY);
    }

    public void update(float pX, float pY) {
        cam.position.x = pX + SCROLLTRACKER_X;
        cam.position.y = pY + SCROLLTRACKER_Y;

        cam.update();
    }

    public void reset() {
        cam.viewportWidth = MazeGame.WIDTH;
        cam.viewportHeight = MazeGame.HEIGHT;
    }
}
