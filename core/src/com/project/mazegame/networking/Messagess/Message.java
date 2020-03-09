package com.project.mazegame.networking.Messagess;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Message {

<<<<<<< HEAD
    public static final int PLAYER_NEW_MSG = 1;
    public static final int PLAYER_MOVE_MSG= 2;
=======
     int PLAYER_NEW_MSG = 1;
     int PLAYER_MOVE_MSG= 2;
     int PLAYER_COLLECT_MSG = 3;
>>>>>>> origin/yueyi1

    void send(DatagramSocket ds, String serverIP, int serverUDPPort);
    void process(DataInputStream dis);
}
