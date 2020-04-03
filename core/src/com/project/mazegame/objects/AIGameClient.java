package com.project.mazegame.objects;

import com.project.mazegame.networking.Client.NetClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Every AI player in multi-player mode will have an instance of AIGameClient
 * @Yueyi wang
 * @Zhen su
 */
public class AIGameClient {

    private AIPlayer aiPlayer;
    private NetClient netClient;
    private List<Player> players = new ArrayList<>();
    public HashMap<Integer, Integer> playersIdIndexList = new HashMap<>();

    public AIGameClient(AIPlayer aiPlayer, NetClient netClient){
        this.aiPlayer=aiPlayer;
        this.netClient=netClient;
    }

    //===================================Getter&Setter==============================================
    public AIPlayer getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(AIPlayer aiPlayer) {
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
