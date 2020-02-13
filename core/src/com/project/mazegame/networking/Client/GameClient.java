package com.project.mazegame.networking.Client;

import com.project.mazegame.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class GameClient {

    private Player player;
    private NetClient nc;
    private List<Player> players = new ArrayList<>();

    public Player getPlayer() {
        return player;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public NetClient getNc() {
        return nc;
    }

    public void setNc(NetClient nc) {
        this.nc = nc;
    }





}
