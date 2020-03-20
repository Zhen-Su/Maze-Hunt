package com.project.mazegame.networking.Messagess;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Message {

    public static final int PLAYER_NEW_MSG = 1;
    public static final int PLAYER_MOVE_MSG= 2;
    public static final int PLAYER_COLLECT_MSG = 3;
    public static final int PLAYER_EXIT_MSG=4;
    public static final int HOST_START = 5;
    public static final int ITEMS_CREATE = 6;
    public static final int ATTACK_MSG = 7;
    public static final int DEAD_MSG = 8;
    public static final int AI_NEW_MSG=9;
    public static final int REMOVE_MSG=10;

    void send(DatagramSocket ds, String serverIP, int serverUDPPort);
    void process(DataInputStream dis);
    void process(DataInputStream dis,int aiIndex);
}
