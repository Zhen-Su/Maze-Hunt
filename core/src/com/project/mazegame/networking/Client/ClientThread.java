package com.project.mazegame.networking.Client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Receive and send thread for client
 */
public class ClientThread implements Runnable {


    private NetClient netClient;
    byte[] receiveBuf = new byte[1024];

    public ClientThread(NetClient netClient) {
        this.netClient=netClient;
    }

    @Override
    public void run() {
        while(null != netClient.ds){
            DatagramPacket dp = new DatagramPacket(receiveBuf, receiveBuf.length);
            try{
                System.out.println("I've received a packet from server");
                netClient.ds.receive(dp);
                process(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Process the data which rececive from server
     * @param dp
     */
    private void process(DatagramPacket dp) {
        ByteArrayInputStream bais = new ByteArrayInputStream(receiveBuf, 0, dp.getLength());
        DataInputStream dis = new DataInputStream(bais);

        int msgType = 0;
        try {
            msgType = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Message msg = null;
//        switch (msgType){
//            case Message.PLAYER_NEW_MSG:
//                msg = new NewMessage(gc);
//                msg.process(dis);
//                break;
//            case  Message.PLAYER_MOVE_MSG:
//                msg = new MoveMessage(gc);
//                msg.process(dis);
//                break;
        }
    }




