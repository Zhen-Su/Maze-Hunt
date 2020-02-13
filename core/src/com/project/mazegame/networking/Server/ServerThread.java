package com.project.mazegame.networking.Server;

import com.project.mazegame.networking.Client.ClientInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Receive and send thread for server
 */
public class ServerThread implements Runnable{

    private DatagramPacket dp;
    private DatagramSocket ds;
    private GameServer gameServer;
    byte[] receiveBuf = new byte[1024];

    public ServerThread(GameServer gameServer){
        this.gameServer=gameServer;
    }


    @Override
    public void run() {
        DatagramSocket ds =null;
        try {
            ds = new DatagramSocket(GameServer.SERVER_UDP_PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        printMsg("Server UDP thread started at port: "+GameServer.SERVER_UDP_PORT);
        while(ds!=null) {
            DatagramPacket dp = new DatagramPacket(receiveBuf,receiveBuf.length);
            try {
                ds.receive(dp);
                printMsg("I received a packet from a client, and i will broadcast to all clients!!!");
                sendToAllClients();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Sever receive a packet and then broadcast to all clients
     */
    public void sendToAllClients() {
        for (ClientInfo c : gameServer.clients) {
            if (c != null) {
                dp.setSocketAddress(new InetSocketAddress(c.IP, c.UDP_PORT));
                try {
                    ds.send(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printMsg("I've broadcast to client");
            }
        }
    }


    /**
     * Only for debug (print message)
     * @param msg
     */
    public void printMsg(String msg){
        System.out.println(msg);
    }

}
