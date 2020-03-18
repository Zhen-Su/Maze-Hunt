package com.project.mazegame.networking.Messagess;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public class DeadMessage implements Message {
    private int msgType = Message.DEAD_MSG;

    @Override
    public void send(DatagramSocket ds, String serverIP, int serverUDPPort) {

    }

    @Override
    public void process(DataInputStream dis) {

    }
}
