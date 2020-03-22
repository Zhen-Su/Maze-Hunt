package com.project.mazegame.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.project.mazegame.screens.MultiPlayerGameScreen;
import com.project.mazegame.tools.Collect;
import com.project.mazegame.tools.Coordinate;
import com.project.mazegame.tools.Timer;

import java.util.ArrayList;

public class MultiAIPlayer extends Player {

    private MultiPlayerGameScreen gameClient;
    public static boolean debug = true;

    public MultiAIPlayer(TiledMapTileLayer collisionLayer,String username, int ID, MultiPlayerGameScreen gameClient, String colour, Direction dir) {
        super(collisionLayer,username, ID, colour);
        this.gameClient=gameClient;
        this.dir=dir;
        if(debug) System.out.println("AI Multilayer construction done! ");
    }

    public MultiAIPlayer(TiledMapTileLayer collisionLayer, String username, int x, int y, MultiPlayerGameScreen gameClient, Direction dir, String colour) {
        super();
        this.collisionLayer = collisionLayer;
        this.health = 5;
        this.coins = 0;
        this.name = username;
        this.items = new ArrayList<>();
        this.colour = colour;
        swordXP = 0;
        shieldXP = 0;
        this.font = gameClient.bitmapFont;
        time = new Timer();
        co = new Collect(this);

        this.x = x;
        this.y = y;
        this.position = new Coordinate(x, y);

        loadPlayerTextures();
        this.gameClient = gameClient;
        this.dir = dir;
        createAnimations();
    }


}
