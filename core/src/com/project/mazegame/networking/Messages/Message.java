package com.project.mazegame.networking.Messages;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Message {

    public static final int PLAYER_NEW_MSG = 1;
    public static final int PLAYER_MOVE_MSG= 2;

    void send(DatagramSocket ds, String serverIP, int serverUDPPort);
    void process(DataInputStream dis);
}