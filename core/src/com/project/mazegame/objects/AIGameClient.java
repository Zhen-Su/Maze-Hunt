package com.project.mazegame.objects;

import com.project.mazegame.networking.Client.NetClient;
import com.project.mazegame.tools.Collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIGameClient {

    private AIPlayer aiPlayer;
    private Collect collect;
    private NetClient netClient;

    private List<Player> players = new ArrayList<>();
    public HashMap<Integer, Integer> playersIdIndexList = new HashMap<>();

    public AIPlayer getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    public Collect getCollect() {
        return collect;
    }

    public void setCollect(Collect collect) {
        this.collect = collect;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }



    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public AIGameClient(AIPlayer aiPlayer,Collect collect, NetClient netClient){
        this.aiPlayer=aiPlayer;

        this.collect=collect;
        this.netClient=netClient;
    }

}
