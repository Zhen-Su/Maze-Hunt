package com.project.mazegame.networking.Messagess;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Message {

     int PLAYER_NEW_MSG = 1;
     int PLAYER_MOVE_MSG = 2;
     boolean DEBUG = true;

    void send(DatagramSocket ds, String serverIP, int serverUDPPort);
    void process(DataInputStream dis);
}
