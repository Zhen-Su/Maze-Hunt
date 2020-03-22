package com.project.mazegame.objects;

import com.project.mazegame.networking.Client.NetClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIGameClient {

    private MultiAIPlayer aiPlayer;
    private NetClient netClient;
    private List<Player> players = new ArrayList<>();
    public HashMap<Integer, Integer> playersIdIndexList = new HashMap<>();

    public AIGameClient(MultiAIPlayer aiPlayer, NetClient netClient){
        this.aiPlayer=aiPlayer;
        this.netClient=netClient;
    }

    //===================================Getter&Setter==============================================
    public MultiAIPlayer getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(MultiAIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
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

    //==============================================================================================

}
